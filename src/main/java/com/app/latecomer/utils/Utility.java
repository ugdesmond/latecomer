package com.app.latecomer.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class Utility {
    public  Date convertToDate(String dateTime) {
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date dateObj = null;
        try {
            dateObj = date.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateObj;
    }
    public  String formateFullDateTime(Date dateTime) {
        DateFormat time = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        time.format(dateTime);
        return time.format(dateTime);
    }

}
