package com.fypapplication.fypapp.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.ui.DashBoardFragment;
import com.fypapplication.fypapp.ui.MapMarkerActivity;
import com.google.firebase.BuildConfig;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class FirebaseMessaging extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessaging";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        try {

            Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());

            JSONObject body = new JSONObject(remoteMessage.getData().get("body"));
            String lat = body.getString("lat");
            String lng = body.getString("lng");

            Intent intent = new Intent(this, MapMarkerActivity.class);
            intent.putExtra("lat",lat);
            intent.putExtra("lng", lng);
            try {

            } catch (Exception ex) {
                if (BuildConfig.DEBUG) Log.d(TAG, "onMessageReceived: " + ex);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            final String channelId = "Default";
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("msg"))
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                assert manager != null;
                manager.createNotificationChannel(channel);
            }
            assert manager != null;
            manager.notify(0, builder.build());


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
