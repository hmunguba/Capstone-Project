package com.community.hmunguba.condominium.service.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

// From: http://www.gadgetsaint.com/android/no-internet-connectivity-android/#.XDPqqlVKiUk
public class ConnectionReceiver extends BroadcastReceiver {
    public static final String TAG = ConnectionReceiver.class.getSimpleName();
    public static ConnectionReceiverListener connectionListener;

    public ConnectionReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (connectionListener != null) {
            connectionListener.onNetworkConnectionChanged(isConnected);
        }

        Utils.displayNoConnectionToast(context);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectionReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
