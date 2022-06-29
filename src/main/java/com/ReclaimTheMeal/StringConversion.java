package com.ReclaimTheMeal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class StringConversion {
    public Date StringToDateTime(String st)throws Exception {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = time.parse(st);
        return date;
    }

    public String DateTimeToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String st = dateFormat.format(date);
        return st;
    }
}
