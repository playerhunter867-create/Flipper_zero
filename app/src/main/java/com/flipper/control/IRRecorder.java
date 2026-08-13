package com.flipper.control;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IRRecorder {
    private Context context;
    private ConsumerIrManager irManager;
    private ArrayList<Integer> recordedPattern = new ArrayList<>();

    public IRRecorder(Context context) {
        this.context = context;
        irManager = (ConsumerIrManager) context.getSystemService(Context.CONSUMER_IR_SERVICE);
    }

    public void startRecording() {
        if (irManager == null || !irManager.hasIrEmitter()) {
            Toast.makeText(context, "❌ IR-бластер не обнаружен", Toast.LENGTH_LONG).show();
            return;
        }
        recordedPattern.clear();
        Toast.makeText(context, "📡 Наведите пульт и нажмите кнопку (эмуляция)", Toast.LENGTH_LONG).show();
    }

    public void savePattern(String fileName, int[] pattern) {
        try {
            File dir = context.getExternalFilesDir(null);
            if (dir != null && !dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName + ".ir");
            FileWriter writer = new FileWriter(file);
            for (int value : pattern) {
                writer.write(value + " ");
            }
            writer.close();
            Toast.makeText(context, "✅ Паттерн сохранён: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, "❌ Ошибка сохранения: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public int[] loadPattern(String fileName) {
        try {
            File file = new File(context.getExternalFilesDir(null), fileName + ".ir");
            if (!file.exists()) {
                Toast.makeText(context, "❌ Файл не найден: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                return null;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            reader.close();
            String[] parts = line.trim().split(" ");
            int[] pattern = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                pattern[i] = Integer.parseInt(parts[i]);
            }
            return pattern;
        } catch (Exception e) {
            Toast.makeText(context, "❌ Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
