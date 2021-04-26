package com.wc.settings.domain;

import lombok.Data;

@Data
public class DicValue implements Comparable<DicValue> {
    private String id;
    private String value;
    private String text;
    private String orderNo;
    private String typeCode;


    @Override
    public int compareTo(DicValue o) {
        if(this.typeCode.compareTo(o.typeCode)!=0) {
            return this.typeCode.compareTo(o.typeCode);
        } else {
            return Integer.parseInt(this.orderNo) - (Integer.parseInt(o.orderNo));
        }
    }
}
