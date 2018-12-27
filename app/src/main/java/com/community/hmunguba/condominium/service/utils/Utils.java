package com.community.hmunguba.condominium.service.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.firebase.FirebaseUserAuthentication;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE
        );
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void displayNoConnectionToast(Context context) {
        Toast.makeText(context, context.getString(R.string.check_internet_connection_toast),
                Toast.LENGTH_SHORT).show();
    }

    public static String getProfileType(Context context) {
        String userEmail = FirebaseUserAuthentication.getInstance().getUserEmail();
        String prefFileName = context.getString(R.string.file_key_pref) + userEmail;

        SharedPreferences prefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        String profileType = prefs.getString(context.getString(R.string.profile_type_pref),
                context.getString(R.string.no_profile_type_set));

        return profileType;
    }

    public static String removeSpecialCharacters(String input) {
        String text = input.replaceAll("[^a-zA-Z0-9]+", "");
        return text;
    }

    public static String normalizeAndLowcaseName(String input) {
        String name = Normalizer.normalize(input, Normalizer.Form.NFD);
        name = name.replaceAll("[^\\p{ASCII}]", "");
        return name.toLowerCase();
    }

    public static Date getDateFromString(String fullDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String simpleDate = getSimpleDateAsString(fullDate);
        Date date = new Date();

        try {
            date = formatter.parse(simpleDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getSimpleDateAsString(String fullDate) {
        String monthName = fullDate.substring(4,7);
        String day = fullDate.substring(8, 10);
        String year = fullDate.substring(fullDate.length()-4, fullDate.length());

        String month = getMonthNumberCorrepondant(monthName);
        String simpleDate = day + "-" + month + "-" + year;
        return simpleDate;
    }

    public static String getMonthNumberCorrepondant(String monthText) {
        switch (monthText) {
            case "Jan":
                return "01";
            case "Feb":
                return "02";
            case "Mar":
                return "03";
            case "Apr":
                return "04";
            case "May":
                return "05";
            case "Jun":
                return "06";
            case "Jul":
                return "07";
            case "Aug":
                return "08";
            case "Sep":
                return "09";
            case "Oct":
                return "10";
            case "Nov":
                return "11";
            case "Dec":
                return "12";
            default:
                return "00";
        }
    }
}
