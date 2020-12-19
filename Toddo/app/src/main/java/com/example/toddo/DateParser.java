package com.example.toddo;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateParser {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public static String parseDate(String dateStr) {
        //Date todayDate = new Date();
        String formatted = new String();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        Calendar calendar2 = (Calendar) calendar.clone();
        Calendar calendar3 = (Calendar) calendar.clone();
        Calendar today = (Calendar) calendar.clone();
        calendar.add(Calendar.DATE, 1);
        Calendar tomorrow = (Calendar) calendar.clone();
        calendar2.add(Calendar.DATE, 7);
        Calendar nextWeek = (Calendar) calendar2.clone();
        calendar3.add(Calendar.YEAR, 1);
        Calendar nextYear = (Calendar) calendar3.clone();

        //Log.d("date", dateStr);
        //sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateStr);
            Calendar dateC = Calendar.getInstance();
            dateC.setTime(date);
            Log.d("date", String.valueOf(today.getTime()));
            if(today.getTime().compareTo(date)==0) {
                formatted = "Today";
            } else if (tomorrow.getTime().compareTo(date)==0) {
                formatted = "Tomorrow";
            } else if(date.before(nextWeek.getTime())) {
//                Calendar week = Calendar.getInstance();
//                week.setTime(date);
                int dayWeek = dateC.get(Calendar.DAY_OF_WEEK);
                switch(dayWeek){
                    case 1:
                        formatted = "Sunday";
                        break;
                    case 2:
                        formatted = "Monday";
                        break;
                    case 3:
                        formatted = "Tuesday";
                        break;
                    case 4:
                        formatted = "Wednesday";
                        break;
                    case 5:
                        formatted = "Thursday";
                        break;
                    case 6:
                        formatted = "Friday";
                        break;
                    case 7:
                        formatted = "Saturday";
                        break;
                    default:
                        break;
                }
            } else {
                int month = dateC.get(Calendar.MONTH);
                String monthString = new String();
                switch(month){
                    case 0:
                        monthString = "Jan";
                        break;
                    case 1:
                        monthString = "Feb";
                        break;
                    case 2:
                        monthString = "Mar";
                        break;
                    case 3:
                        monthString = "Apr";
                        break;
                    case 4:
                        monthString = "May";
                        break;
                    case 5:
                        monthString = "Jun";
                        break;
                    case 6:
                        monthString = "Jul";
                        break;
                    case 7:
                        monthString = "Aug";
                        break;
                    case 8:
                        monthString = "Sep";
                        break;
                    case 9:
                        monthString = "Oct";
                        break;
                    case 10:
                        monthString = "Nov";
                        break;
                    case 11:
                        monthString = "Dec";
                        break;
                    default:
                        break;
                }
                if (date.before(nextYear.getTime())) {
                    formatted = dateC.get(Calendar.DAY_OF_MONTH) + " " + monthString;
                } else {
                    formatted = dateC.get(Calendar.DAY_OF_MONTH) + " " + monthString + " " + dateC.get(Calendar.YEAR);
                }
            }
        //return formatted;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatted;
    }
}
