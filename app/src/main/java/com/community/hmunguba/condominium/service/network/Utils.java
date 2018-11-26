package com.community.hmunguba.condominium.service.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;

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
}
