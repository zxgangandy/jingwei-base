package io.jingwei.base.utils.math;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {
    /**
     * 计算精度
     */
    public static Integer SCALE = 16;

    /**
     * 进位规则, 默认全部舍弃
     */
    public static RoundingMode MODE = RoundingMode.DOWN;

    public static BigDecimal toScale(int scale, String value) {
        if (StringUtils.isEmpty(value)) {
            return  BigDecimal.ZERO;
        }
        return new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal toScale(int scale, BigDecimal value) {
        if (value == null) {
            return  BigDecimal.ZERO;
        }
        return value.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public static String toPlainString(BigDecimal value) {
        if (value == null) {
            return  null;
        }

        return value.stripTrailingZeros().toPlainString();
    }

    public static BigDecimal div (BigDecimal a,BigDecimal b) {
        return a.divide(b, SCALE, MODE);
    }

    public static BigDecimal mul (BigDecimal a, BigDecimal b) {
        return a.multiply(b).setScale(SCALE,MODE);
    }

    public static BigDecimal add (BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    public static BigDecimal sub(BigDecimal a,BigDecimal b) {
        return a.subtract(b);
    }

    public static BigDecimal round (BigDecimal val) {
        return val.setScale(SCALE, MODE);
    }
}
