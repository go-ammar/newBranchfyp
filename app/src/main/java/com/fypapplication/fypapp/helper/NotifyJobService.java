package com.fypapplication.fypapp.helper;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.fypapplication.fypapp.R;
import com.fypapplication.fypapp.ui.MainActivity;

public class NotifyJobService extends JobService {
    private static final int JOB_ID = 1;
    public static final long ONE_DAY_INTERVAL =  2 * 24 * 60 * 60 * 1000L; // 2 Day
    private static final long ONE_WEEK_INTERVAL = 7 * 24 * 60 * 60 * 1000L; // 1 Week
    private static final String TAG = "CartJobService";

    public static void schedule(Context context, long intervalMillis) {
        JobScheduler jobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName =
                new ComponentName(context, NotifyJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) builder.setMinimumLatency(5000);
        else builder.setPeriodic(5000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        jobScheduler.schedule(builder.build());
        Log.d(TAG, "actionViews: job1");
    }

    public static void cancel(Context context) {
        JobScheduler jobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        /* executing a task synchronously */
        Log.d(TAG, "onStartJob: ");
//        CartParser cartparser = new CartParser(this);
//        if(cartparser.getAllStoresFormattedInList().size() > 0)
            ChangesHelper change = new ChangesHelper(this);
            change.scheduler();
//            addNotification();

        // false when it is synchronous.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

//    private void addNotification() {
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo)
//                        .setContentTitle("Please check your cart")
//                        .setContentText("You have some items to order");
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0, builder.build());
//    }
}