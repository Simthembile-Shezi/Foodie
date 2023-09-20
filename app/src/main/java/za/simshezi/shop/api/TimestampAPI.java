package za.simshezi.shop.api;


import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Date;

public class TimestampAPI {
    public static String getTime(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    public static String getDate(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}
