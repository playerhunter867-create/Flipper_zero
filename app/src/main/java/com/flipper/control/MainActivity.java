package com.flipper.control;

import android.os.Bundle;
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
            tvOutput.setText("📡 IR РЕЖИМ АКТИВЕН\n\n" +
                    "Нажми кнопку для отправки сигнала:");
            irButtonsContainer.setVisibility(View.VISIBLE);
        });

        // ====== ВКЛАДКА NFC ======
        btnTabNfc.setOnClickListener(v -> {
            tvOutput.setText("📱 NFC РЕЖИМ\n\n" +
                    "1. Чтение NFC-меток\n" +
                    "2. Запись данных на метки\n" +
                    "3. Эмуляция карты\n\n" +
                    "⚠️ Требуется NFC-модуль");
            irButtonsContainer.setVisibility(View.GONE);
        });

        // ====== ВКЛАДКА BADUSB ======
        btnTabBadUsb.setOnClickListener(v -> {
            tvOutput.setText("⌨️ BADUSB РЕЖИМ\n\n" +
                    "1. Эмуляция Bluetooth-клавиатуры\n" +
                    "2. Отправка скриптов на ПК\n\n" +
                    "⚠️ Требуется Android 10+");
            irButtonsContainer.setVisibility(View.GONE);
        });

        // ====== IR КНОПКИ ======

        // ТЕЛЕВИЗОР
        findViewById(R.id.btnTvPower).setOnClickListener(v -> {
            tvOutput.append("\n📤 Отправка: ТВ Вкл/Выкл (Samsung)");
            irManager.tvPowerSamsung();
        });

        findViewById(R.id.btnTvVolUp).setOnClickListener(v -> {
            tvOutput.append("\n📤 Отправка: Громкость +");
            irManager.tvVolumeUpSamsung();
        });

        findViewById(R.id.btnTvVolDown).setOnClickListener(v -> {
            tvOutput.append("\n📤 Отправка: Громкость -");
            irManager.tvVolumeDownSamsung();
        });

        // КОНДИЦИОНЕР
        findViewById(R.id.btnAcPower).setOnClickListener(v -> {
            tvOutput.append("\n📤 Отправка: Кондиционер Вкл/Выкл");
            irManager.acPowerGree();
        });

        findViewById(R.id.btnAcTempUp).setOnClickListener(v -> {
            tvOutput.append("\n📤 Отправка: Температура +");
            irManager.acTempUp();
        });

        findViewById(R.id.btnAcTempDown).setOnClickListener(v -> {
            tvOutput.append("\n📤 Отправка: Температура -");
            irManager.acTempDown();
        });

        // XIAOMI BOX
        findViewById(R.id.btnBoxPower).setOnClickListener(v -> {
            tvOutput.append("\n📤 Отправка: Xiaomi Mi Box Вкл/Выкл");
            int[] pattern = IrCodeDatabase.getBoxCodes("xiaomi_box").get("power");
            irManager.sendCustomSignal(pattern);
        });
    }
}
