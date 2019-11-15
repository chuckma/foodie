package com.imooc.enums;

/**
 * @author mcg
 * @create 2019-11-15-11:59
 *
 *  @Desc: 是否 枚举
 */
public enum YesOrNo {
    NO(0, "否"),
    YES(1, "是");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
