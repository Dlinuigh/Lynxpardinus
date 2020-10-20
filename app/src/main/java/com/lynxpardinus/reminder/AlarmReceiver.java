package com.lynxpardinus.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("AlarmReceiver","--------收到提醒");
        //TODO 实现功能

        //重新计时第二天七点的
        Intent i = new Intent(context, RemindService.class);
        context.startService(i);
    }
}