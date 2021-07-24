package com.cas.enums;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2021/7/24 9:13 下午
 * @desc
 */
public enum DelayTypeEnum {
    /**
     *
     */
    DELAY_10s(),
    DELAY_60s(),
    ;

    public static DelayTypeEnum getDelayTypeEnumByValue(Integer delayType) {
        if (delayType == 1) {
            return DELAY_10s;
        } else {
            return DELAY_60s;
        }
    }
}
