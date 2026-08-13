package com.flipper.control;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView tvOutput;
    private Button btnTabIr, btnTabNfc, btnTabBadUsb;
    private LinearLayout irButtonsContainer;
    private IRManager irManager;

    // Универсальный поиск
    private boolean isSearching = false;
    private int currentBrandIndex = 0;
    private String[] tvBrands = IrCodeDatabase.getAllBrands();
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private String foundBrand = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOutput = findViewById(R.id.tvOutput);
        btnTabIr = findViewById(R.id.btnTabIr);
        btnTabNfc = findViewById(R.id.btnTabNfc);
        btnTabBadUsb = findViewById(R.id.btnTabBadUsb);
        irButtonsContainer = findViewById(R.id.irButtonsContainer);

        irManager = new IRManager(this);

        // ====== ВКЛАДКА IR ======
        btnTabIr.setOnClickListener(v -> {
            tvOutput.setText("📡 IR РЕЖИМ АКТИВЕН\n\nНажми кнопку для отправки сигнала:");
            irButtonsContainer.setVisibility(View.VISIBLE);
        });

        // ====== ВКЛАДКА NFC ======
        btnTabNfc.setOnClickListener(v -> {
            tvOutput.setText("📱 NFC РЕЖИМ\n\n1. Чтение NFC-меток\n2. Запись данных на метки\n3. Эмуляция карты\n\n⚠️ Требуется NFC-модуль");
            irButtonsContainer.setVisibility(View.GONE);
        });

        // ====== ВКЛАДКА BADUSB ======
        btnTabBadUsb.setOnClickListener(v -> {
            tvOutput.setText("⌨️ BADUSB РЕЖИМ\n\n1. Эмуляция Bluetooth-клавиатуры\n2. Отправка скриптов на ПК\n\n⚠️ Требуется Android 10+");
            irButtonsContainer.setVisibility(View.GONE);
        });

        // ====== КНОПКИ ТЕЛЕВИЗОРА (работают с найденным сигналом) ======
        findViewById(R.id.btnTvPower).setOnClickListener(v -> {
            if (foundBrand == null) {
                tvOutput.append("\n❌ Сначала найдите свой телевизор через 'Найти ТВ'");
                return;
            }
            tvOutput.append("\n📤 ТВ Вкл/Выкл (" + foundBrand.toUpperCase() + ")");
            int[] pattern = IrCodeDatabase.getTvCodes(foundBrand).get("power");
            irManager.sendCustomSignal(pattern);
        });

        findViewById(R.id.btnTvVolUp).setOnClickListener(v -> {
            if (foundBrand == null) {
                tvOutput.append("\n❌ Сначала найдите свой телевизор через 'Найти ТВ'");
                return;
            }
            tvOutput.append("\n📤 Громкость + (" + foundBrand.toUpperCase() + ")");
            // Заглушка: отправляем тот же power (для демонстрации)
            int[] pattern = IrCodeDatabase.getTvCodes(foundBrand).get("power");
            irManager.sendCustomSignal(pattern);
        });

        findViewById(R.id.btnTvVolDown).setOnClickListener(v -> {
            if (foundBrand == null) {
                tvOutput.append("\n❌ Сначала найдите свой телевизор через 'Найти ТВ'");
                return;
            }
            tvOutput.append("\n📤 Громкость - (" + foundBrand.toUpperCase() + ")");
            int[] pattern = IrCodeDatabase.getTvCodes(foundBrand).get("power");
            irManager.sendCustomSignal(pattern);
        });

        // ====== УНИВЕРСАЛЬНЫЙ ПОИСК ======
        findViewById(R.id.btnSearchTv).setOnClickListener(v -> {
            startTvSearch();
        });

        findViewById(R.id.btnStopSearch).setOnClickListener(v -> {
            stopTvSearch();
        });
    }

    // ====== УНИВЕРСАЛЬНЫЙ ПОИСК ======
    private void startTvSearch() {
        if (isSearching) return;
        isSearching = true;
        currentBrandIndex = 0;
        foundBrand = null;
        tvOutput.append("\n🔍 Универсальный поиск ТВ...\n");
        tvOutput.append("Направьте телефон на телевизор\n");
        tvOutput.append("Когда ТВ отреагирует, нажмите СТОП\n\n");

        searchRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isSearching || currentBrandIndex >= tvBrands.length) {
                    stopTvSearch();
                    return;
                }
                String brand = tvBrands[currentBrandIndex];
                tvOutput.append("📤 " + brand.toUpperCase() + "... ");
                sendTvSignalByBrand(brand);
                currentBrandIndex++;
                searchHandler.postDelayed(this, 800);
            }
        };
        searchHandler.post(searchRunnable);
    }

    private void stopTvSearch() {
        isSearching = false;
        searchHandler.removeCallbacks(searchRunnable);
        if (currentBrandIndex > 0 && currentBrandIndex <= tvBrands.length) {
            foundBrand = tvBrands[currentBrandIndex - 1];
            tvOutput.append("\n✅ Найден сигнал для бренда: " + foundBrand.toUpperCase());
        } else {
            tvOutput.append("\n⏹ Поиск остановлен. Телевизор не найден.");
        }
    }

    private void sendTvSignalByBrand(String brand) {
        int[] pattern = IrCodeDatabase.getTvCodes(brand).get("power");
        if (pattern != null) {
            irManager.sendCustomSignal(pattern);
        }
    }
}
