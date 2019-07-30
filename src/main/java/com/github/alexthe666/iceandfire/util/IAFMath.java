package com.github.alexthe666.iceandfire.util;

public class IAFMath {

    private static final double coeff_1 = Math.PI / 4;
    private static final double coeff_2 = coeff_1 * 3;

    public static final double atan2_accurate(double y, double x) {
        double r;
        if (y < 0) {
            y = -y;
            if (x > 0) {
                r = (x - y) / (x + y);
                return -(0.1963 * r * r * r - 0.9817 * r + coeff_1);
            } else {
                r = (x + y) / (y - x);
                return -(0.1963 * r * r * r - 0.9817 * r + coeff_2);
            }
        } else {
            if (y == 0) {
                y = 1.0E-25;
            }
            if (x > 0) {
                r = (x - y) / (x + y);
                return 0.1963 * r * r * r - 0.9817 * r + coeff_1;
            } else {
                r = (x + y) / (y - x);
                return 0.1963 * r * r * r - 0.9817 * r + coeff_2;
            }
        }
    }
}
