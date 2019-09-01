package com.alphakiwi.projet_7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alphakiwi.projet_7.api.NotificationHelper;

import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        //if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
        NotificationHelper notificationHelper = new NotificationHelper(context);


        if (getUserCurrent().getNotification() == true) {
            int comparaison = getUserCurrent().getResto().name.compareTo("Pas encore choisit");
            if (comparaison != 0) {
                notificationHelper.createNotification();
            }
        }


        }

    //}
}