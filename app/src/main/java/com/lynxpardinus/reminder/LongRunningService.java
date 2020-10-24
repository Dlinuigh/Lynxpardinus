package com.lynxpardinus.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.preference.PreferenceManager;

import java.util.Calendar;
import java.util.TimeZone;

public class LongRunningService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int min;
    public int hour ;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        hour = preferences.getInt("hour",8);
        min = preferences.getInt("minute", 30);
        int hourOfDay = preferences.getInt("hour2",8);
        int minute = preferences.getInt("minute2",0);
        int time2 = minute+ 60*hourOfDay;
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //读者可以修改此处的Minutes从而改变提醒间隔时间
        //此处是设置每隔90分钟启动一次
        //这是90分钟的毫秒数
        //Log.e("LongRunningService", "hello");

        int Minutes = time2*60*1000;
        //SystemClock.elapsedRealtime()表示1970年1月1日0点至今所经历的时间
        long triggerAtTime = SystemClock.elapsedRealtime() + Minutes;
        //此处设置开启AlarmReceiver这个Service
        Long secondsNextEarlyMorning = getSecondsNextEarlyMorning(hour,min);
        Intent i1 = new Intent(this, AlarmReceiver.class);
        //PendingIntent pi1 = PendingIntent.getBroadcast(this, 0, i1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pi1 = PendingIntent.getBroadcast(this, 0, i1, 0);
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + secondsNextEarlyMorning, pi1);
        Intent i = new Intent(this, timer.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        //ELAPSED_REALTIME_WAKEUP表示让定时任务的出发时间从系统开机算起，并且会唤醒CPU。
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    public static Long getSecondsNextEarlyMorning(int num1,int num2) {
        //得到与第二天的时间差

        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        if (cal.get(Calendar.HOUR_OF_DAY) - num1 > 0) {
            //如果当前时间大于等于8点 就计算第二天的8点的
            cal.add(Calendar.DAY_OF_YEAR, 1);
        } else {
            if(cal.get(Calendar.MINUTE)-num2 > 0){
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            cal.add(Calendar.DAY_OF_YEAR, 0);
        }
        cal.set(Calendar.HOUR_OF_DAY, num1);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, num2);
        cal.set(Calendar.MILLISECOND, 0);
        Long seconds = (cal.getTimeInMillis() - System.currentTimeMillis());

        return seconds.longValue();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        //在Service结束后关闭AlarmManager
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, timer.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        Intent i1 = new Intent(this, AlarmReceiver.class);
        PendingIntent pi1 = PendingIntent.getBroadcast(this, 0, i1, 0);
        manager.cancel(pi);

    }


}
