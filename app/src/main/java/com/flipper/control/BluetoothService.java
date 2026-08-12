package com.flipper.control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private static final UUID UUID_FLIPPER = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public boolean connect(String macAddress) {
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter == null) return false;
            BluetoothDevice device = adapter.getRemoteDevice(macAddress);
            socket = device.createRfcommSocketToServiceRecord(UUID_FLIPPER);
            socket.connect();
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void sendCommand(String command) throws IOException {
        outputStream.write((command + "\r\n").getBytes());
        outputStream.flush();
    }

    public String readResponse() throws IOException {
        byte[] buffer = new byte[1024];
        int bytes = inputStream.read(buffer);
        return new String(buffer, 0, bytes);
    }

    public void disconnect() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }
}
