package com.lynxpardinus.reminder;

import java.util.Calendar;

public class Utils {
    public static Long getSecondsNextEarlyMorning(int num) {
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.HOUR_OF_DAY) - num >= 0) {
            //如果当前时间大于等于8点 就计算第二天的8点的
            cal.add(Calendar.DAY_OF_YEAR, 1);
        } else {
            cal.add(Calendar.DAY_OF_YEAR, 0);
        }
        cal.set(Calendar.HOUR_OF_DAY, num);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Long seconds = (cal.getTimeInMillis() - System.currentTimeMillis());

        return seconds.longValue();
    }
}
