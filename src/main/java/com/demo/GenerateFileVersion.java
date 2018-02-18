package com.demo;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class GenerateFileVersion {

    public static void main (String... args){
        GenerateFileVersion f=new GenerateFileVersion();
        f.fileVersion();
    }

    private String trimOrPad(String str, int length, char padChar) {
        String result;
        if (str == null) {
            result = "";
        } else {
            result = str;
        }

        if (result.length() > length) {
            return result.substring(0, length);
        } else {
            while (result.length() < length) {
                result = padChar+result;
            }

            return result;
        }
    }

    private String fileVersion(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST6CDT"));
        calendar.setTime(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        StringBuffer sb=new StringBuffer();
        sb.append(calendar.get(Calendar.YEAR)%100);
        sb.append(".");
        sb.append(this.trimOrPad(String.valueOf(calendar.get(Calendar.MONTH)+1),2,'0'));
        sb.append(".");
        sb.append(this.trimOrPad(String.valueOf(calendar.get(Calendar.DATE)),2,'0'));
        sb.append(".");
        sb.append(this.trimOrPad(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),2,'0'));
        sb.append(this.trimOrPad(String.valueOf(calendar.get(Calendar.MINUTE)),2,'0'));
        sb.append(this.trimOrPad(String.valueOf(calendar.get(Calendar.SECOND)),2,'0'));

        System.out.println("Choose Your Next File Name From below list...");

        int i=0;

        for(ENVIRONMENT env: ENVIRONMENT.values()){
            System.out.println("Next File Name for Making DDL Change : "+"V"+sb.toString()+this.trimOrPad(String.valueOf(i++),2,'0')+"__"+env.toString()+"_DDL.sql");
            System.out.println("Next File Name for Making DML Change : "+"V"+sb.toString()+this.trimOrPad(String.valueOf(i++),2,'0')+"__"+env.toString()+"_DML.sql");
        }

        return sb.toString();
    }

    private enum ENVIRONMENT{COMMON(1),LOCAL(9),STAGE(4),MTF(5),PERF(7),PROD(2);
        private int value;
        private ENVIRONMENT(int value) { this.value = value; }
    }


}
