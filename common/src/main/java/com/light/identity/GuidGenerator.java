package com.light.identity;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 基于UUID的ID生成器，生成32位UUID+4位随机数的ID
 * @author DaLiu
 */
public final class GuidGenerator {

    private static final Pattern UUID_DASH_PATTERN = Pattern.compile("-");
    private static final SecureRandom RANDOM = new SecureRandom();

    private GuidGenerator() {
    }

    public static String generate() {
        return UUID_DASH_PATTERN.matcher(UUID.randomUUID().toString()).replaceAll("") + (RANDOM.nextInt(8999) + 1000);
    }

    public static void main(String[] args) {
    	System.out.println(GuidGenerator.generate().length());
    }
}
