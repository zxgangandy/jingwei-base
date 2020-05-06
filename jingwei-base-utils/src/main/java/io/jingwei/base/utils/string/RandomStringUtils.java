package io.jingwei.base.utils.string;

public class RandomStringUtils {

    public static String randomAlphanumeric(int len) {
        return org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(len);
    }

    public static String randomNumeric(int len) {
        return org.apache.commons.lang.RandomStringUtils.randomNumeric(len);
    }

    public static String randomAlphabetic(int len) {
        return org.apache.commons.lang.RandomStringUtils.randomAlphabetic(len);
    }

}
