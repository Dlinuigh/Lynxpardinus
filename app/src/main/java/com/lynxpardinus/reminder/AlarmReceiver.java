package com.lynxpardinus.reminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class AlarmReceiver  extends BroadcastReceiver {
    public static final String CHANNEL_ID1 = "channel_id1";   //通道渠道id
    public static final String CHANEL_NAME1 = "chanel_name1"; //通道渠道名称
    private NotificationManager notificationManager1;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.e("--------收到提醒");
        //TODO 实现功能
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID1, CHANEL_NAME1, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);//是否在桌面icon右上角展示小红点
        channel.setLightColor(Color.GREEN);//小红点颜色
        channel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
        notificationManager1=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification=new Notification.Builder(context,CHANNEL_ID1);
        notification.setAutoCancel(true);
        notification.setContentTitle("该休息啦！")
                .setContentText("已经到休息的时间啦，开始你的夜生活吧！")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
        //.setTicker("巴士门");
        notificationManager1.createNotificationChannel(channel);
        notificationManager1.notify(2,notification.build());//发送通知
        Intent i = new Intent(context, LongRunningService.class);
        context.startService(i);
    }
}
