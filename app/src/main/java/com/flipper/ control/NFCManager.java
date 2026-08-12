package com.flipper.control;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NFCManager {
    private NfcAdapter nfcAdapter;
    private AppCompatActivity activity;

    public NFCManager(AppCompatActivity activity) {
        this.activity = activity;
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        if (nfcAdapter == null) {
            Toast.makeText(activity, "❌ NFC не обнаружен", Toast.LENGTH_LONG).show();
        }
    }

    public boolean hasNfc() {
        return nfcAdapter != null;
    }

    // Включить режим чтения NFC (вызывать в onResume)
    public void enableNfcReading() {
        if (hasNfc()) {
            // Для Android Beam (устаревшее) — лучше использовать HostApduService
            Toast.makeText(activity, "📱 NFC режим включён. Поднесите метку.", Toast.LENGTH_SHORT).show();
        }
    }

    // Чтение NFC-метки (при получении Intent)
    public String readTag(Tag tag) {
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                byte[] data = ndef.getNdefMessage().toByteArray();
                ndef.close();
                return new String(data);
            }
        } catch (Exception e) {
            return "❌ Ошибка чтения: " + e.getMessage();
        }
        return "❌ Метка не поддерживает NDEF";
    }
}
