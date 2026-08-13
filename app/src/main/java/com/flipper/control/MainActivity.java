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
    private IRRecorder irRecorder;

    // Универсальный поиск
    private boolean isSearching = false;
    private int currentBrandIndex = 0;
    private String[] tvBrands = {"samsung", "lg", "sony", "xiaomi", "philips", "panasonic", "tcl", "hisense", "sharp", "toshiba"};
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

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
        irRecorder = new IRRecorder(this);

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

        // ====== ТЕЛЕВИЗОР TOSHIBA (теперь работают!) ======
        findViewById(R.id.btnTvPower).setOnClickListener(v -> {
            tvOutput.append("\n📤 ТВ Вкл/Выкл (TOSHIBA)");
            irManager.sendToshibaPower();
        });

        findViewById(R.id.btnTvVolUp).setOnClickListener(v -> {
            tvOutput.append("\n📤 Громкость + (TOSHIBA)");
            irManager.sendToshibaVolUp();
        });

        findViewById(R.id.btnTvVolDown).setOnClickListener(v -> {
            tvOutput.append("\n📤 Громкость - (TOSHIBA)");
            irManager.sendToshibaVolDown();
        });

        // ====== КОНДИЦИОНЕР ======
        findViewById(R.id.btnAcPower).setOnClickListener(v -> {
            tvOutput.append("\n📤 Кондиционер Вкл/Выкл");
            irManager.acPowerGree();
        });

        findViewById(R.id.btnAcTempUp).setOnClickListener(v -> {
            tvOutput.append("\n📤 Температура +");
            irManager.acTempUp();
        });

        findViewById(R.id.btnAcTempDown).setOnClickListener(v -> {
            tvOutput.append("\n📤 Температура -");
            irManager.acTempDown();
        });

        // ====== XIAOMI BOX ======
        findViewById(R.id.btnBoxPower).setOnClickListener(v -> {
            tvOutput.append("\n📤 Xiaomi Box Вкл/Выкл");
            int[] pattern = IrCodeDatabase.getBoxCodes("xiaomi_box").get("power");
            irManager.sendCustomSignal(pattern);
        });

        // ====== УНИВЕРСАЛЬНЫЙ ПОИСК ======
        findViewById(R.id.btnSearchTv).setOnClickListener(v -> {
            startTvSearch();
        });

        findViewById(R.id.btnStopSearch).setOnClickListener(v -> {
            stopTvSearch();
        });

        // ====== ЗАПИСЬ IR ======
        findViewById(R.id.btnRecord).setOnClickListener(v -> {
            tvOutput.append("\n🔴 Запись сигнала...");
            irRecorder.startRecording();
            int[] testPattern = {4500, 4500, 500, 1600, 500, 1600, 500, 500, 500, 500};
            irRecorder.savePattern("my_signal", testPattern);
            tvOutput.append("\n✅ Тестовый паттерн сохранён как 'my_signal'");
        });

        findViewById(R.id.btnSendRecorded).setOnClickListener(v -> {
            tvOutput.append("\n📤 Отправка сохранённого сигнала...");
            irManager.sendSignalFromFile("my_signal");
        });
    }

    // ====== УНИВЕРСАЛЬНЫЙ ПОИСК ======
    private void startTvSearch() {
        if (isSearching) return;
        isSearching = true;
        currentBrandIndex = 0;
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
            tvOutput.append("\n✅ Найден сигнал для бренда: " + tvBrands[currentBrandIndex - 1].toUpperCase());
        }
        tvOutput.append("\n⏹ Поиск остановлен\n");
    }

    private void sendTvSignalByBrand(String brand) {
        int[] pattern = IrCodeDatabase.getTvCodes(brand).get("power");
        if (pattern != null) {
            irManager.sendCustomSignal(pattern);
        }
    }
}
