package com.fpoly.java5.utils;


public class RandomPass {
    public int randompass() { 
        int min = 100000;
        int max = 999999;
        return (int) ((Math.random() * (max - min + 1)) + min);
    }

}
