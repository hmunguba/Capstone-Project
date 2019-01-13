package com.community.hmunguba.condominium.service.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.firebase.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.service.model.Event;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static void displayNoConnectionToast(Context context) {
        Toast.makeText(context, context.getString(R.string.check_internet_connection_toast),
                Toast.LENGTH_SHORT).show();
    }

    public static String getPreferenceFileName(Context context) {
        String fileName = context.getString(R.string.file_key_pref) +
                FirebaseUserAuthentication.getInstance().getUserEmail();
        return fileName;
    }

    public static String getProfileTypePreference(Context context) {
        String prefFileName = getPreferenceFileName(context);
        SharedPreferences prefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        String profileType = prefs.getString(context.getString(R.string.profile_type_pref),
                context.getString(R.string.no_profile_type_set));

        return profileType;
    }

    public static String getCondIdPreference(Context context) {
        String prefFileName = getPreferenceFileName(context);
        SharedPreferences prefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        String condIdPref = prefs.getString(context.getString(R.string.cond_id_pref),
                context.getString(R.string.no_cond_id_set));

        return condIdPref;
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

    public static Date getDateFromString(String simpleDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
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

    public static boolean isOldEvent(Date today, Event event) {
        Date eventDate = Utils.getDateFromString(event.getSimpleDate());

        if (eventDate.before(today)) {
            return true;
        }
        return false;
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

    public static int[] getResidentMenuItemDrawables() {
        int[] drawablesImageIds = {
                R.drawable.ic_menu_resident, R.drawable.ic_menu_events,
                R.drawable.ic_menu_concierge,
        };
        return drawablesImageIds;
    }

    public static int[] getCondominiumMenuItemDrawables() {
        int[] drawablesImageIds = {
                R.drawable.ic_menu_building, R.drawable.ic_menu_events,
                R.drawable.ic_menu_concierge,
        };
        return drawablesImageIds;
    }

    public static boolean isPossibleTime(String time) {
        if (time.length() == 1) {
            return true;
        }
        int hour = Integer.parseInt(time.substring(0,2));
        int minutes = Integer.parseInt(time.substring(3,5));
        String twoPoints = time.substring(2, 3);

        if (time.length() == 5 && hour <= 23 && minutes < 60 && twoPoints.equals(":")) {
            return true;
        }
        return false;
    }

    public static boolean isValidZipCode(String zipCode) {
        if (zipCode.length() < 9) {
            return false;
        }
        String hyphen = zipCode.substring(5, 6);
        if (zipCode.length() == 9 && hyphen.equals("-")) {
            return true;
        }
        return false;
    }

    public static int getCorrectEventIcon(Event event) {
        if (event.getReservedArea().isHasGourmetArea()) {
            return R.drawable.ic_gourmet;
        } else if (event.getReservedArea().isHasPoolArea()) {
            return R.drawable.ic_pool;
        } else if (event.getReservedArea().isHasBarbecueArea()) {
            return R.drawable.ic_barbecue;
        } else if (event.getReservedArea().isHasMoviesArea()) {
            return R.drawable.ic_movies;
        } else if (event.getReservedArea().isHasPartyRoomArea()) {
            return R.drawable.ic_partyroom;
        } else {
            return R.drawable.ic_sports;
        }
    }
}
