package com.onehana.server_ilogu.util;

public class VerificationCode {
    private final String code;
    private long expirationTime;

    public VerificationCode(String code, long expirationTime) {
        this.code = code;
        this.expirationTime = expirationTime;
    }

    public String getCode() {
        return code;
    }

    public boolean isValid() {
        return System.currentTimeMillis() <= expirationTime;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }
}
