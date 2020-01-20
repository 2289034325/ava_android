package com.acxca.ava.presentation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PresentationUtil {
    public static String Date2ShortStr(Date date){
        DateFormat fmt = new SimpleDateFormat("MM/dd HH:mm");

        return fmt.format(date);
    }
}
