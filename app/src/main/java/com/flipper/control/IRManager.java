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

    // БАЗОВЫЙ МЕТОД ОТПРАВКИ СИГНАЛА
    private void sendSignal(int frequency, int[] pattern) {
        if (hasIr()) {
            irManager.transmit(frequency, pattern);
            Toast.makeText(context, "✅ IR-сигнал отправлен", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "❌ IR-бластер недоступен", Toast.LENGTH_SHORT).show();
        }
    }

    // ========== КОМАНДЫ ДЛЯ ТВ ==========

    // ВКЛ/ВЫКЛ ТВ (Samsung)
    public void tvPowerSamsung() {
        int[] pattern = {4500, 4500, 500, 1600, 500, 1600, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 500};
        sendSignal(38000, pattern);
    }

    // ВКЛ/ВЫКЛ ТВ (LG)
    public void tvPowerLg() {
        int[] pattern = {9000, 4500, 600, 1600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600};
        sendSignal(38000, pattern);
    }

    // ГРОМКОСТЬ + (Samsung)
    public void tvVolumeUpSamsung() {
        int[] pattern = {4500, 4500, 500, 1600, 500, 1600, 500, 500, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500};
        sendSignal(38000, pattern);
    }

    // ГРОМКОСТЬ - (Samsung)
    public void tvVolumeDownSamsung() {
        int[] pattern = {4500, 4500, 500, 1600, 500, 1600, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 500, 500, 1600, 500, 500};
        sendSignal(38000, pattern);
    }

    // ========== КОМАНДЫ ДЛЯ КОНДИЦИОНЕРА ==========

    // ВКЛ/ВЫКЛ кондиционера (General / Gree)
    public void acPowerGree() {
        int[] pattern = {9000, 4500, 600, 1600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600};
        sendSignal(38000, pattern);
    }

    // ТЕМПЕРАТУРА + (кондиционер)
    public void acTempUp() {
        int[] pattern = {9000, 4500, 600, 1600, 600, 1600, 600, 600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600};
        sendSignal(38000, pattern);
    }

    // ТЕМПЕРАТУРА - (кондиционер)
    public void acTempDown() {
        int[] pattern = {9000, 4500, 600, 1600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600, 600, 1600, 600, 600};
        sendSignal(38000, pattern);
    }

    // ========== УНИВЕРСАЛЬНЫЙ МЕТОД (для своих сигналов) ==========

    public void sendCustomSignal(int[] pattern) {
        sendSignal(38000, pattern);
    }
}
