package com.hahazql.tools.helper;

import java.math.BigDecimal;

public class MathUtil {
    /**
     * @param v1
     * @param v2
     * @return
     */
    public static long add(long v1, long v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).longValue();
    }

    /**
     * @param v1
     * @param v2
     * @return
     */
    public static long div(long v1, long v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, 0, BigDecimal.ROUND_HALF_UP).longValue();
    }
}
