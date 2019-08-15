package com.alphakiwi.projet_7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alphakiwi.projet_7.api.NotificationHelper;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.createNotification();

    }
}