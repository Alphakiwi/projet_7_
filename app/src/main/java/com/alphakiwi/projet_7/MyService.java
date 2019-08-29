package com.alphakiwi.projet_7;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.alphakiwi.projet_7.api.NotificationHelper;

import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;

public class MyService extends JobIntentService {

    public static final int JOB_ID = 0x01;


    public static void enqueueWork(Context context, Intent work) {

        enqueueWork(context, MyService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
        if (getUserCurrent().getNotification() == true) {
            int comparaison = getUserCurrent().getResto().name.compareTo("Pas encore choisit");
            if (comparaison != 0) {
                notificationHelper.createNotification();
            }
        }

    }

}
