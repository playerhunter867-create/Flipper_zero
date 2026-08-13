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
    private IRRecorder irRecorder;

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

        // ====== ТЕЛЕВИЗОР ======
        findViewById(R.id.btnTvPower).setOnClickListener(v -> {
            tvOutput.append("\n📤 ТВ Вкл/Выкл (Samsung)");
            irManager.tvPowerSamsung();
        });

        findViewById(R.id.btnTvVolUp).setOnClickListener(v -> {
            tvOutput.append("\n📤 Громкость +");
            irManager.tvVolumeUpSamsung();
        });

        findViewById(R.id.btnTvVolDown).setOnClickListener(v -> {
            tvOutput.append("\n📤 Громкость -");
            irManager.tvVolumeDownSamsung();
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
}
