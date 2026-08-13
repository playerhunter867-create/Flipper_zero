package com.flipper.control;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.widget.Toast;

public class IRManager {
    private ConsumerIrManager irManager;
    private Context context;

    public IRManager(Context context) {
        this.context = context;
        irManager = (ConsumerIrManager) context.getSystemService(Context.CONSUMER_IR_SERVICE);
        if (irManager == null || !irManager.hasIrEmitter()) {
            Toast.makeText(context, "❌ IR-бластер не обнаружен", Toast.LENGTH_LONG).show();
        }
    }

    public boolean hasIr() {
        return irManager != null && irManager.hasIrEmitter();
    }

    private void sendSignal(int frequency, int[] pattern) {
        if (hasIr()) {
            irManager.transmit(frequency, pattern);
            Toast.makeText(context, "✅ IR-сигнал отправлен", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "❌ IR-бластер недоступен", Toast.LENGTH_SHORT).show();
        }
    }

    // ====== ТВ SAMSUNG ======
    public void tvPowerSamsung() {
        sendSignal(38000, new int[]{4500, 4500, 500, 1600, 500, 1600, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 500});
    }

    public void tvVolumeUpSamsung() {
        sendSignal(38000, new int[]{4500, 4500, 500, 1600, 500, 1600, 500, 500, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500});
    }

    public void tvVolumeDownSamsung() {
        sendSignal(38000, new int[]{4500, 4500, 500, 1600, 500, 1600, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 500, 500, 1600, 500, 500});
    }

    // ====== КОНДИЦИОНЕР GREE ======
    public void acPowerGree() {
        sendSignal(38000, new int[]{9000, 4500, 600, 1600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600});
    }

    public void acTempUp() {
        sendSignal(38000, new int[]{9000, 4500, 600, 1600, 600, 1600, 600, 600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600});
    }

    public void acTempDown() {
        sendSignal(38000, new int[]{9000, 4500, 600, 1600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600, 600, 1600, 600, 600});
    }

    // ====== УНИВЕРСАЛЬНЫЙ ======
    public void sendCustomSignal(int[] pattern) {
        sendSignal(38000, pattern);
    }

    // ====== ОТПРАВКА ИЗ ФАЙЛА ======
    public void sendSignalFromFile(String fileName) {
        IRRecorder recorder = new IRRecorder(context);
        int[] pattern = recorder.loadPattern(fileName);
        if (pattern != null) {
            sendSignal(38000, pattern);
        }
    }
}
