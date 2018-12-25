package com.community.hmunguba.condominium.service.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.repo.FirebaseUserAuthentication;

import java.text.Normalizer;

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
}
