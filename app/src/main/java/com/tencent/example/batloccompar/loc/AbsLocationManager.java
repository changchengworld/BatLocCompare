package com.tencent.example.batloccompar.loc;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.tencent.example.batloccompar.R;

abstract class AbsLocationManager {
    private static final String NOTIFICATION_CHANNEL_NAME = "locationdemoBackgroundLocation";
    protected static final int LOC_NOTIFICATIONID = 100;
    Context mCtx;
    private boolean isCreateChannel;
    private NotificationManager notificationManager;

    AbsLocationManager(Context context) {
        mCtx = context;
    }

    abstract void startLocation(ILocationListener listener);

    abstract void stopLocation();

    protected Notification buildNotification() {
        Notification.Builder builder = null;
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (notificationManager == null) {
                notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            String channelId = mCtx.getPackageName();
            if (!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(mCtx.getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(mCtx.getApplicationContext());
        }
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("BatLocCompare")
                .setContentText("正在后台运行")
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis());

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
        return notification;
    }
}
