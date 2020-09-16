package io.jingwei.base.utils.math;

import io.jingwei.base.utils.string.StringUtils;

import java.math.BigDecimal;

public class MathUtils {

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
}
