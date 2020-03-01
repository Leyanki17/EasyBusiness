package com.projet.easybusiness.helper_request;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class HelperClass {
    public static String formatDate(long date){
        String dateString = new SimpleDateFormat("MM/dd/yyyy").format( date);
        return dateString;
    }

    public static Long dateToLong(Date date){
        long longDate=0;
        try {
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            date = (Date)formatter.parse(""+date+"");
            longDate=date.getTime();
        }
        catch (ParseException e){
            System.out.println("Exception :"+e);
        }
        return longDate;
    }

    public static Date stringToDate(String s){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date d = null;
        try {
             d = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static String[] changeToPng(String[] img){
        for(int i=0; i< img.length; i++){
            img[i]=img[i].replaceAll(".jpg",".png");
        }
        return img;
    }

    public static float stringToFloat(String str){
        float f=0;
        try
        {
             f = Float.valueOf(str.trim()).floatValue();
        }
        catch (NumberFormatException nfe)
        {
            System.err.println("NumberFormatException: " + nfe.getMessage());
        }
        return f;
    }

    public static Date fromLongToDate(Long valeur){
        String dateAsString = String.valueOf(valeur);
        Date date = null;
        try{
            date = new SimpleDateFormat("MM/dd/yyyy").parse(dateAsString);
        }catch(Exception e){
            e.printStackTrace();
        }
        return date ;
          }
}
