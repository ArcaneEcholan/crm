package com.wc.utils.TEst;

import java.math.BigDecimal;

public class CalServiceImpl implements CalService {

    public void longTimeOperation() {
        System.out.println("start calculating....");
        BigDecimal sum = new BigDecimal(0) ;
        for (int i = 0; i < 1000000000L; i++) {
            sum = sum.add(new BigDecimal(2));
        }
        System.out.println("longTimeOperation finished,sum = " + sum);
    }

    public void longTimeSorting() {
        System.out.println("start sorting....");
        int[] arr = new int[100000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i + 1;
        }

        for(int i = 0; i < arr.length - 1; i ++) {
            for(int j = 0; j < arr.length - 1 - i; j++) { if(arr[j] > arr[j + 1]) {
                int t = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = t;
            }
            }
        }
        System.out.println("sorting finished");
    }
}