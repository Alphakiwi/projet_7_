package com.alphakiwi.projet_7.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alphakiwi.projet_7.R;
import com.alphakiwi.projet_7.api.NotificationHelper;

import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        //if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
        NotificationHelper notificationHelper = new NotificationHelper(context);


        if (getUserCurrent().getNotification() == true) {
            int comparison = getUserCurrent().getResto().name.compareTo(context.getString(R.string.no_choice));
            if (comparison != 0) {
                notificationHelper.createNotification();
            }
        }


        }

    //}
}