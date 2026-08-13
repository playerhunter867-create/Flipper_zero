package com.flipper.control;

import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TestIrActivity extends AppCompatActivity {

    private ConsumerIrManager irManager;
    private TextView tvStatus;
    private Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ir);

        tvStatus = findViewById(R.id.tvStatus);
        btnTest = findViewById(R.id.btnTest);

        // Получаем доступ к IR-системе
        irManager = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);

        btnTest.setOnClickListener(v -> {
            // Проверяем, есть ли IR-передатчик
            if (irManager == null || !irManager.hasIrEmitter()) {
                tvStatus.setText("❌ IR-передатчик НЕ НАЙДЕН!");
                return;
            }

            tvStatus.setText("📤 Отправка тестового сигнала...");

            // Простой тестовый паттерн: 1 импульс вкл, 1 импульс выкл
            int[] pattern = new int[]{1000, 1000};

            // Отправляем на частоте 38000 Гц (стандарт для пультов)
            irManager.transmit(38000, pattern);

            tvStatus.setText("✅ Сигнал отправлен!\n" +
                    "Направь телефон на другую камеру,\n" +
                    "чтобы увидеть фиолетовую вспышку.");
        });
    }
}
