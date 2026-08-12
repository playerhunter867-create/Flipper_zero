package com.flipper.control;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Build;
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

    // Отправить IR-сигнал (частота 38 кГц, паттерн)
    public void sendIrSignal(int[] pattern) {
        if (hasIr()) {
            irManager.transmit(38000, pattern);
            Toast.makeText(context, "✅ IR-сигнал отправлен", Toast.LENGTH_SHORT).show();
        }
    }

    // Готовые сигналы для ТВ (вкл/выкл для популярных брендов)
    public void sendTvPower(String brand) {
        int[] pattern;
        switch (brand.toLowerCase()) {
            case "samsung":
                pattern = new int[]{4500, 4500, 500, 1600, 500, 1600, 500, 500};
                break;
            case "lg":
                pattern = new int[]{9000, 4500, 600, 1600, 600, 1600, 600, 600};
                break;
            case "sony":
                pattern = new int[]{2400, 600, 1200, 600, 1200, 600, 600, 600};
                break;
            default:
                pattern = new int[]{4500, 4500, 500, 1600, 500, 1600, 500, 500};
                break;
        }
        sendIrSignal(pattern);
    }

    // Для кондиционера (вкл/выкл)
    public void sendAcPower() {
        int[] pattern = {9000, 4500, 600, 1600, 600, 1600, 600, 600, 600, 600, 600, 1600};
        sendIrSignal(pattern);
    }
}
