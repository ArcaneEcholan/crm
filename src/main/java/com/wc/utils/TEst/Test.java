package com.wc.utils.TEst;

public class Test {

    public static void main(String[] args) {
        try{
            throw new RuntimeException("你好");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
