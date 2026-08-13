package com.flipper.control;

import java.util.HashMap;
import java.util.Map;

public class IrCodeDatabase {

    public static Map<String, int[]> getTvCodes(String brand) {
        Map<String, int[]> codes = new HashMap<>();
        switch (brand.toLowerCase()) {
            case "samsung":
                codes.put("power", new int[]{4500, 4500, 500, 1600, 500, 1600, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 1600, 500, 500, 500, 500, 500, 500});
                break;
            case "xiaomi":
                codes.put("power", new int[]{3500, 1750, 450, 1300, 450, 1300, 450, 450, 450, 450, 450, 1300, 450, 450, 450, 450, 450, 450, 450, 450, 450, 1300, 450, 450, 450, 450, 450, 1300, 450, 450, 450, 450, 450, 450});
                break;
            default:
                codes.put("power", new int[]{4500, 4500, 500, 1500, 500, 1500, 500, 500, 500, 500, 500, 1500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 1500, 500, 500, 500, 500, 500, 1500, 500, 500, 500, 500, 500, 500});
                break;
        }
        return codes;
    }

    public static Map<String, int[]> getBoxCodes(String brand) {
        Map<String, int[]> codes = new HashMap<>();
        switch (brand.toLowerCase()) {
            case "xiaomi_box":
                codes.put("power", new int[]{3200, 1650, 420, 1250, 420, 1250, 420, 420, 420, 420, 420, 1250, 420, 420, 420, 420, 420, 420, 420, 420, 420, 1250, 420, 420, 420, 420, 420, 1250, 420, 420, 420, 420, 420, 420});
                break;
            default:
                codes.put("power", new int[]{3200, 1650, 420, 1250, 420, 1250, 420, 420, 420, 420, 420, 1250, 420, 420, 420, 420, 420, 420, 420, 420, 420, 1250, 420, 420, 420, 420, 420, 1250, 420, 420, 420, 420, 420, 420});
                break;
        }
        return codes;
    }

    public static Map<String, int[]> getAcCodes(String brand) {
        Map<String, int[]> codes = new HashMap<>();
        switch (brand.toLowerCase()) {
            case "gree":
                codes.put("power", new int[]{9000, 4500, 600, 1600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600});
                break;
            default:
                codes.put("power", new int[]{9000, 4500, 600, 1600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 1600, 600, 600, 600, 600, 600, 600});
                break;
        }
        return codes;
    }
}
