package com.dgp.job.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CronUtil {

    /**
     * cron表达式格式校验
     */
    private static final String CRON_DATE_FORMAT = "ss mm HH dd MM ? yyyy";

    /**
     * 获取cron表达式
     *
     * @param date {@link Date} 时间
     * @return {@link String} cron表达式
     */
    public static String getCron(final Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
        String formatTimeStr = "";
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

}
