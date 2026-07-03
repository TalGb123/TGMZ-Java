package com.example.myapplication.utils;

public class ValidatorUtils {
    /**
     * בודק האם תעודת הזהות תקינה לפי אלגוריתם ספרת ביקורת.
     *
     * @param id The ID string to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidIsraeliID(String id) {
        if (id == null) return false;
        String strId = id.trim();

        if (strId.length() > 9 || strId.length() < 5) return false;

        // Pad with leading zeros to make it 9 digits
        while (strId.length() < 9) {
            strId = "0" + strId;
        }

        // Ensure it contains only digits
        if (!strId.matches("\\d+")) return false;

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int num = Character.getNumericValue(strId.charAt(i));
            int step = num * ((i % 2) + 1);
            if (step > 9) step -= 9;
            sum += step;
        }
        return sum % 10 == 0;
    }
}
