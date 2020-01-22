package com.projet.easybusiness.helper_request;

import java.text.DateFormat;

public class HelperClass {
    public static String formatDate(long date){
        String dateFormat = DateFormat.getDateInstance().format(date);
        return dateFormat;
    }

    public static String[] changeToPng(String[] img){
        for(int i=0; i< img.length; i++){
            img[i]=img[i].replaceAll(".jpg",".png");
        }
        return img;
    }
}
