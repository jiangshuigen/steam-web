package com.example.demo.enumpakage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

public enum Status {
    JACKSON_EXCEPTION(5104, "Jackson异常");

    private final Integer code;
    private final String msg;
    private static final int SUCCESS = 2000;
    public static final int SUCCESS_ZERO = 0;
    private static final int WEIGHT = 80000;
    private static final int DONET_WEIGHT = 100000;
    private static final String DONET_STATUS_PREFIX = "DONET_";
    private static MessageSource messageSource;

    private Status(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        String msg = null;

        try {
            msg = messageSource.getMessage("status." + this.code, (Object[])null, LocaleContextHolder.getLocale());
        } catch (Exception var3) {
            msg = this.msg;
        }

        return msg;
    }

    public Integer getCode() {
        if (this.code == 2000) {
            return 0;
        } else {
            return this.name().startsWith("DONET_") ? Math.subtractExact(this.code, 100000) : Math.addExact(this.code, 80000);
        }
    }

    public Integer getOriginCode() {
        return this.code;
    }

//    public static Status getByCode(Integer code) {
//        if (null == code) {
//            return UNKNOWN;
//        } else {
//            Status[] var1 = values();
//            int var2 = var1.length;
//
//            for(int var3 = 0; var3 < var2; ++var3) {
//                Status status = var1[var3];
//                if (status.getCode().equals(code)) {
//                    return status;
//                }
//            }
//
//            return UNKNOWN;
//        }
//    }

    @Component
    public static class Builder {
        public Builder() {
        }

        @Autowired
        public void setMessageResource(MessageSource messageSource) {
            Status.messageSource = messageSource;
        }
    }
}