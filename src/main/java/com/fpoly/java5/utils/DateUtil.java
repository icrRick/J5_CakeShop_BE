package com.fpoly.java5.utils;

import java.time.LocalDate;

public class DateUtil {
	public static int getCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.getMonthValue(); 
    }
	public static int getCurrentYear() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.getYear(); 
    }
}
