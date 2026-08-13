package com.flipper.control;

import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RealTest extends AppCompatActivity {
    private ConsumerIrManager irManager;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tvStatus = findViewById(R.id.tvStatus);
        Button btnTest = findViewById(R.id.btnTest);

        irManager = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);

        btnTest.setOnClickListener(v -> {
            if (irManager == null || !irManager.hasIrEmitter()) {
                tvStatus.setText("❌ IR-передатчик НЕ НАЙДЕН!");
                return;
            }

            tvStatus.setText("📤 Отправка тестового сигнала...");

            int[] pattern = new int[]{1000, 1000};
            irManager.transmit(38000, pattern);

            tvStatus.setText("✅ Сигнал отправлен! Направь телефон на камеру, чтобы увидеть вспышку.");
        });
    }
}
