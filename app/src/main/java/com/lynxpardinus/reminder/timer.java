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

import com.lynxpardinus.R;

public class timer extends BroadcastReceiver {
    public static final String CHANNEL_ID = "channel_id";   //通道渠道id
    public static final String CHANEL_NAME = "chanel_name"; //通道渠道名称
    private NotificationManager notificationManager;
    //String ns = Context.NOTIFICATION_SERVICE;
    final int NOTIFYID=0x123;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        //protected void onCreate(Bundle savedInstanceState) {
        //设置通知内容并在onReceive()这个函数执行时开启
        //String ns = Context.NOTIFICATION_SERVICE;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);//是否在桌面icon右上角展示小红点
        channel.setLightColor(Color.GREEN);//小红点颜色
        channel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
        notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification=new Notification.Builder(context,CHANNEL_ID);
        notification.setAutoCancel(true);
        notification.setContentTitle("学习时间小提示")
                .setContentText("你已经学习5分钟啦！快玩半小时游戏休息一下吧！")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(NOTIFYID,notification.build());//发送通知
        //再次开启LongRunningService这个服务，从而可以
        Intent i = new Intent(context, LongRunningService.class);
        context.startService(i);
    }


}