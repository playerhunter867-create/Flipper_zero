package com.flipper.control;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Handler;
import android.os.Looper;
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
    private boolean isRecording = false;
    private ArrayList<Integer> recordedPattern = new ArrayList<>();

    public IRRecorder(Context context) {
        this.context = context;
        irManager = (ConsumerIrManager) context.getSystemService(Context.CONSUMER_IR_SERVICE);
    }

    // СТАРТ ЗАПИСИ (эмуляция приёма)
    public void startRecording() {
        if (irManager == null || !irManager.hasIrEmitter()) {
            Toast.makeText(context, "❌ IR-бластер не обнаружен", Toast.LENGTH_LONG).show();
            return;
        }

        isRecording = true;
        recordedPattern.clear();
        Toast.makeText(context, "📡 Наведите пульт и нажмите кнопку", Toast.LENGTH_LONG).show();

        // В реальном телефоне нет встроенного API для ПРИЁМА IR.
        // Это эмуляция: мы предлагаем пользователю ввести паттерн вручную
        // ИЛИ используем стороннюю библиотеку (например, ConsumerIrManager не умеет принимать).
        // ПОЭТОМУ МЫ ДЕЛАЕМ ТАК:
        showManualInputDialog();
    }

    // ВРУЧНОЙ ВВОД ПАТТЕРНА (для тестирования)
    private void showManualInputDialog() {
        // В реальном приложении здесь должен быть диалог с полем ввода
        // Например: "Введите числа через пробел: 4500 4500 500 1600 ..."
        // Мы упростим: просто покажем Toast
        Toast.makeText(context, 
            "⚠️ Ваш телефон не умеет принимать IR.\n" +
            "Введите паттерн вручную в коде или используйте готовые базы.", 
            Toast.LENGTH_LONG).show();
    }

    // СОХРАНИТЬ ПАТТЕРН В ФАЙЛ
    public void savePattern(String fileName, int[] pattern) {
        try {
            File file = new File(context.getExternalFilesDir(null), fileName + ".ir");
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

    // ЗАГРУЗИТЬ ПАТТЕРН ИЗ ФАЙЛА
    public int[] loadPattern(String fileName) {
        try {
            File file = new File(context.getExternalFilesDir(null), fileName + ".ir");
            if (!file.exists()) {
                Toast.makeText(context, "❌ Файл не найден", Toast.LENGTH_LONG).show();
                return null;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            reader.close();
            String[] parts = line.split(" ");
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
