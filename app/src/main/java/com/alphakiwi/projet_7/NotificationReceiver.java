package com.alphakiwi.projet_7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alphakiwi.projet_7.api.NotificationHelper;

import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            MyService.enqueueWork(context, new Intent());
        }
        /*if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {


        }

    */}
}