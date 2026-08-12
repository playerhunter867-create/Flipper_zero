package com.flipper.control;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText etMac, etCommand;
    private TextView tvOutput;
    private Button btnConnect, btnSend;
    private BluetoothService btService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMac = findViewById(R.id.etMac);
        etCommand = findViewById(R.id.etCommand);
        tvOutput = findViewById(R.id.tvOutput);
        btnConnect = findViewById(R.id.btnConnect);
        btnSend = findViewById(R.id.btnSend);

        btService = new BluetoothService();

        btnConnect.setOnClickListener(v -> {
            String mac = etMac.getText().toString().trim();
            if (mac.isEmpty()) {
                tvOutput.append("❌ Введите MAC адрес\n");
                return;
            }
            tvOutput.append("⏳ Подключение к " + mac + "...\n");
            boolean connected = btService.connect(mac);
            if (connected) {
                tvOutput.append("✅ Подключено к Flipper Zero\n");
            } else {
                tvOutput.append("❌ Ошибка подключения. Проверь MAC и Bluetooth\n");
            }
        });

        btnSend.setOnClickListener(v -> {
            String cmd = etCommand.getText().toString().trim();
            if (cmd.isEmpty()) {
                tvOutput.append("❌ Введите команду\n");
                return;
            }

            // ПРОВЕРКА: подключён ли Flipper
            if (btService == null) {
                tvOutput.append("❌ BluetoothService не инициализирован\n");
                return;
            }

            if (!btService.isConnected()) {
                tvOutput.append("❌ Нет подключения к Flipper. Нажми 'Подключиться'\n");
                return;
            }

            try {
                tvOutput.append("📤 Отправка: " + cmd + "\n");
                btService.sendCommand(cmd);
                String response = btService.readResponse();
                tvOutput.append("📥 Ответ: " + response + "\n");
            } catch (IOException e) {
                tvOutput.append("❌ Ошибка: " + e.getMessage() + "\n");
                e.printStackTrace();
            } catch (Exception e) {
                tvOutput.append("❌ Критическая ошибка: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (btService != null) {
            btService.disconnect();
        }
    }
}
