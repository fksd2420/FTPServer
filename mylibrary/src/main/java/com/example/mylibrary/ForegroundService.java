package com.example.mylibrary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.telecom.Call;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;


public class ForegroundService extends Service {
    private static final String CHANNEL_ID = "FolderSyncService";
    private static final int NOTIFICATION_ID = 1;
    public static boolean IsRunning = false;
    public static Callbacks Callbacks;
    public static String NotificationTitle;
    public static String NotificationContent;
    public static String NotificationContentMore;
    public static ForegroundService RunningInstance = null;
    Thread mainTaskThread;
    Thread monitoringTaskThread;
    public static Object WaitObject = new Object();




    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (RunningInstance != null)
            return START_NOT_STICKY;

        createNotificationChannel();
        Notification notification = Callbacks.onBuildNotification(this, CHANNEL_ID);
        ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                notification,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
                        ? ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                        : 0
        );


        mainTaskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                PowerManager.WakeLock wakeLock = null;
                try {
                    long millis = System.currentTimeMillis();
                    IsRunning = true;
                    RunningInstance = ForegroundService.this;

                    PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

                    wakeLock = powerManager.newWakeLock(
                            PowerManager.PARTIAL_WAKE_LOCK,
                            "MyApp::MyWakeLockTag"
                    );
                    wakeLock.acquire();

                    Callbacks.onStart();
                    Callbacks.onMainTask();


                    if (System.currentTimeMillis() - millis < 5000) {
                        Thread.sleep(5000 - (System.currentTimeMillis() - millis));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (wakeLock != null)
                        wakeLock.release();
                    IsRunning = false;
                    RunningInstance = null;
                    Callbacks.onStop();
                    stopService();
                }
            }
        });
        mainTaskThread.start();
        //return START_STICKY;
        return START_NOT_STICKY;
    }



    public void StartMonitoringTask() {
        if (monitoringTaskThread != null && monitoringTaskThread.isAlive()) {
            monitoringTaskThread.interrupt();
        }

        monitoringTaskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Callbacks.onMonitoringTask();
            }
        });

        monitoringTaskThread.start();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void stopService() {
        stopForeground(STOP_FOREGROUND_REMOVE);
        stopSelf();

        IsRunning = false;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }



    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    //NotificationManager.IMPORTANCE_DEFAULT
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            serviceChannel.setSound(null, null);
            serviceChannel.enableVibration(false);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    public void updateNotification() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, Callbacks.onBuildNotification(this, CHANNEL_ID));
    }








    public interface Callbacks {
        public void onStart();
        public void onStop();

        public void onMainTask();
        public void onMonitoringTask();

        public Notification onBuildNotification(Context context, String channelID);
    }









    public static class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Retrieve data sent from the action button
            String action = intent.getAction();


        }
    }
    private final IBinder binder = new LocalBinder();

    // 2. Define the inner class that exposes this service
    public class LocalBinder extends Binder {
        public ForegroundService getService() {
            // Return this instance of MyLocalService so clients can call public methods
            return ForegroundService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
