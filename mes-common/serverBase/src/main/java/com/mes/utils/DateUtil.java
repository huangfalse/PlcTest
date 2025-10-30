package com.mes.utils;

import java.util.Calendar;
import java.util.Date;
/**
 * DateUtil class
 *
 * @author zsh
 * @date 2025/07/04
 */
public class DateUtil {
    public static Date getBeginDate() {
        Calendar now = Calendar.getInstance();
        Calendar startOfDay = (Calendar) now.clone();
        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
        startOfDay.set(Calendar.MINUTE, 0);
        startOfDay.set(Calendar.SECOND, 0);
        return startOfDay.getTime();
    }

    public static Date getEndDate() {
        Calendar now = Calendar.getInstance();
        Calendar endOfDay = (Calendar) now.clone();
        endOfDay.set(Calendar.HOUR_OF_DAY, 23);
        endOfDay.set(Calendar.MINUTE, 59);
        endOfDay.set(Calendar.SECOND, 59);
        return endOfDay.getTime();
    }
}
