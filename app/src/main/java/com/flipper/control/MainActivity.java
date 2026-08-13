package com.flipper.control;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView tvOutput;
    private Button btnTabIr, btnTabNfc, btnTabBadUsb;
    private LinearLayout irButtonsContainer;
    private IRManager irManager;

    // Поиск TV
    private boolean isSearchingTv = false;
    private int tvIndex = 0;
    private String[] tvBrands = IrCodeDatabase.getAllBrands();
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable tvRunnable;

    // Поиск AC
    private boolean isSearchingAc = false;
    private int acIndex = 0;
    private String[] acBrands = {"gree", "daikin", "midea", "panasonic", "lg", "samsung", "haier", "tcl", "mitsubishi"};
    private Runnable acRunnable;

    private String foundBrand = null;
    private String foundAcBrand = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOutput = findViewById(R.id.tvOutput);
        btnTabIr = findViewById(R.id.btnTabIr);
        btnTabNfc = findViewById(R.id.btnTabNfc);
        btnTabBadUsb = findViewById(R.id.btnTabBadUsb);
        irButtonsContainer = findViewById(R.id.irButtonsContainer);

        irManager = new IRManager(this);

        // ====== ВКЛАДКА IR ======
        btnTabIr.setOnClickListener(v -> {
            tvOutput.setText("> IR MODE ACTIVE\n> SELECT SCAN:\n");
            irButtonsContainer.setVisibility(View.VISIBLE);
        });

        btnTabNfc.setOnClickListener(v -> {
            tvOutput.setText("> NFC MODE\n> NOT IMPLEMENTED\n");
            irButtonsContainer.setVisibility(View.GONE);
        });

        btnTabBadUsb.setOnClickListener(v -> {
            tvOutput.setText("> BADUSB MODE\n> NOT IMPLEMENTED\n");
            irButtonsContainer.setVisibility(View.GONE);
        });

        // ====== ПОИСК КОНДИЦИОНЕРОВ ======
        findViewById(R.id.btnSearchAc).setOnClickListener(v -> {
            startAcSearch();
        });

        findViewById(R.id.btnStopSearch).setOnClickListener(v -> {
            stopAcSearch();
        });

        // ====== ПОИСК ТВ ======
        findViewById(R.id.btnSearchTv).setOnClickListener(v -> {
            startTvSearch();
        });

        findViewById(R.id.btnStopSearchTv).setOnClickListener(v -> {
            stopTvSearch();
        });

        // ====== КНОПКИ УПРАВЛЕНИЯ ======
        findViewById(R.id.btnPower).setOnClickListener(v -> {
            if (foundAcBrand != null) {
                tvOutput.append("\n> POWER (" + foundAcBrand.toUpperCase() + ")");
                int[] pattern = IrCodeDatabase.getAcCodes(foundAcBrand).get("power");
                irManager.sendCustomSignal(pattern);
            } else if (foundBrand != null) {
                tvOutput.append("\n> POWER (" + foundBrand.toUpperCase() + ")");
                int[] pattern = IrCodeDatabase.getTvCodes(foundBrand).get("power");
                irManager.sendCustomSignal(pattern);
            } else {
                tvOutput.append("\n> ERROR: NO DEVICE FOUND");
            }
        });

        findViewById(R.id.btnVolUp).setOnClickListener(v -> {
            if (foundAcBrand != null) {
                tvOutput.append("\n> TEMP+ (" + foundAcBrand.toUpperCase() + ")");
                int[] pattern = IrCodeDatabase.getAcCodes(foundAcBrand).get("temp_up");
                irManager.sendCustomSignal(pattern);
            } else if (foundBrand != null) {
                tvOutput.append("\n> VOL+ (" + foundBrand.toUpperCase() + ")");
                int[] pattern = IrCodeDatabase.getTvCodes(foundBrand).get("power");
                irManager.sendCustomSignal(pattern);
            } else {
                tvOutput.append("\n> ERROR: NO DEVICE FOUND");
            }
        });

        findViewById(R.id.btnVolDown).setOnClickListener(v -> {
            if (foundAcBrand != null) {
                tvOutput.append("\n> TEMP- (" + foundAcBrand.toUpperCase() + ")");
                int[] pattern = IrCodeDatabase.getAcCodes(foundAcBrand).get("temp_down");
                irManager.sendCustomSignal(pattern);
            } else if (foundBrand != null) {
                tvOutput.append("\n> VOL- (" + foundBrand.toUpperCase() + ")");
                int[] pattern = IrCodeDatabase.getTvCodes(foundBrand).get("power");
                irManager.sendCustomSignal(pattern);
            } else {
                tvOutput.append("\n> ERROR: NO DEVICE FOUND");
            }
        });
    }

    // ====== ПОИСК КОНДИЦИОНЕРОВ ======
    private void startAcSearch() {
        if (isSearchingAc) return;
        isSearchingAc = true;
        acIndex = 0;
        foundAcBrand = null;
        tvOutput.append("\n> SCANNING AC UNITS...\n");

        acRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isSearchingAc || acIndex >= acBrands.length) {
                    stopAcSearch();
                    return;
                }
                String brand = acBrands[acIndex];
                tvOutput.append("> " + brand.toUpperCase() + "... ");
                sendAcSignal(brand);
                acIndex++;
                handler.postDelayed(this, 800);
            }
        };
        handler.post(acRunnable);
    }

    private void stopAcSearch() {
        isSearchingAc = false;
        handler.removeCallbacks(acRunnable);
        if (acIndex > 0 && acIndex <= acBrands.length) {
            foundAcBrand = acBrands[acIndex - 1];
            tvOutput.append("\n> FOUND: " + foundAcBrand.toUpperCase());
        } else {
            tvOutput.append("\n> SEARCH STOPPED");
        }
    }

    private void sendAcSignal(String brand) {
        int[] pattern = IrCodeDatabase.getAcCodes(brand).get("power");
        if (pattern != null) {
            irManager.sendCustomSignal(pattern);
        }
    }

    // ====== ПОИСК ТВ ======
    private void startTvSearch() {
        if (isSearchingTv) return;
        isSearchingTv = true;
        tvIndex = 0;
        foundBrand = null;
        tvOutput.append("\n> SCANNING TV UNITS...\n");

        tvRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isSearchingTv || tvIndex >= tvBrands.length) {
                    stopTvSearch();
                    return;
                }
                String brand = tvBrands[tvIndex];
                tvOutput.append("> " + brand.toUpperCase() + "... ");
                sendTvSignal(brand);
                tvIndex++;
                handler.postDelayed(this, 800);
            }
        };
        handler.post(tvRunnable);
    }

    private void stopTvSearch() {
        isSearchingTv = false;
        handler.removeCallbacks(tvRunnable);
        if (tvIndex > 0 && tvIndex <= tvBrands.length) {
            foundBrand = tvBrands[tvIndex - 1];
            tvOutput.append("\n> FOUND: " + foundBrand.toUpperCase());
        } else {
            tvOutput.append("\n> SEARCH STOPPED");
        }
    }

    private void sendTvSignal(String brand) {
        int[] pattern = IrCodeDatabase.getTvCodes(brand).get("power");
        if (pattern != null) {
            irManager.sendCustomSignal(pattern);
        }
    }
}
