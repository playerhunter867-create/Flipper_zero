package com.flipper.control;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView tvOutput;
    private Button btnTabIr, btnTabNfc, btnTabBadUsb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOutput = findViewById(R.id.tvOutput);
        btnTabIr = findViewById(R.id.btnTabIr);
        btnTabNfc = findViewById(R.id.btnTabNfc);
        btnTabBadUsb = findViewById(R.id.btnTabBadUsb);

        btnTabIr.setOnClickListener(v -> {
            tvOutput.setText("📡 IR РЕЖИМ\n\n" +
                    "1. Управление ТВ: кнопки вкл/выкл, громкость\n" +
                    "2. Управление кондиционером: температура, режим\n" +
                    "3. Запись сигнала с пульта\n\n" +
                    "⚠️ Требуется IR-бластер на телефоне");
        });

        btnTabNfc.setOnClickListener(v -> {
            tvOutput.setText("📱 NFC РЕЖИМ\n\n" +
                    "1. Чтение NFC-меток (транспорт, пропуски)\n" +
                    "2. Запись данных на чистые метки\n" +
                    "3. Эмуляция метки (телефон как карта)\n\n" +
                    "⚠️ Требуется NFC-модуль на телефоне");
        });

        btnTabBadUsb.setOnClickListener(v -> {
            tvOutput.setText("⌨️ BADUSB РЕЖИМ\n\n" +
                    "1. Эмуляция Bluetooth-клавиатуры\n" +
                    "2. Готовые скрипты для Windows/Linux\n" +
                    "3. Автоматизация действий на ПК\n\n" +
                    "⚠️ Требуется Android 10+");
        });
    }
}
