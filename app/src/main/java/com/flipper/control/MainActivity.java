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
                tvOutput.append("Введите MAC адрес\n");
                return;
            }
            if (btService.connect(mac)) {
                tvOutput.append("Подключено к Flipper Zero\n");
            } else {
                tvOutput.append("Ошибка подключения\n");
            }
        });

        btnSend.setOnClickListener(v -> {
            String cmd = etCommand.getText().toString().trim();
            if (cmd.isEmpty()) {
                tvOutput.append("Введите команду\n");
                return;
            }
            try {
                btService.sendCommand(cmd);
                String response = btService.readResponse();
                tvOutput.append("Ответ: " + response + "\n");
            } catch (IOException e) {
                tvOutput.append("Ошибка: " + e.getMessage() + "\n");
            }
        });
    }
}
