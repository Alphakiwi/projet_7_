package com.alphakiwi.projet_7.api;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.alphakiwi.projet_7.MainActivity;
import com.alphakiwi.projet_7.R;
import com.alphakiwi.projet_7.model.User;

import java.util.ArrayList;

import static com.alphakiwi.projet_7.api.UserHelper.getAllUser;
import static com.alphakiwi.projet_7.api.UserHelper.getAllUserWithoutMyself;
import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;

public class NotificationHelper {

    private Context mContext;

    private static final String NOTIFICATION_CHANNEL_ID = "10001";

    public NotificationHelper(Context context) {
        mContext = context;

    }

    public void createNotification()
    {
        String restoID = getUserCurrent().getResto().getId();

        ArrayList<User> listUser = new ArrayList<User>();

        for(int j = 0; j < getAllUserWithoutMyself().size(); j++){

            String restoUser = getAllUserWithoutMyself().get(j).getResto().getId();


            int comparaison = restoUser.compareTo(restoID);



            if (comparaison == 0){

                listUser.add(getAllUserWithoutMyself().get(j));
            }

        }

        String coworkers = "Personne d'autre n'y mange.";

        if (listUser.size()>0) {
            coworkers = "Vous mangez avec : ";
        }

        for(int j = 0; j < listUser.size(); j++){

            if (j!= 0){
                coworkers += ", ";
            }
            coworkers += listUser.get(j).getUsername();

        }






        Intent intent = new Intent(mContext , MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Vous avez choisi de manger à " + getUserCurrent().getResto().getName())
                .setContentText(coworkers)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }
}