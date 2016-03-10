package com.hahazql.tools.helper;

import com.hahazql.tools.time.TimeService;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class TimeUtil {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        TIME_ZONE = TimeZone.getDefault();
    }

    /**
     * 毫秒
     */
    public static final long MILLI_SECOND = TimeUnit.MILLISECONDS.toMillis(1);
    /**
     * 秒
     */
    public static final long SECOND = TimeUnit.SECONDS.toMillis(1);
    /**
     * 分
     */
    public static final long MIN = TimeUnit.MINUTES.toMillis(1);
    /**
     * 时
     */
    public static final long HOUR = TimeUnit.HOURS.toMillis(1);
    /**
     * 天
     */
    public static final long DAY = TimeUnit.DAYS.toMillis(1);

    /**
     * 每分钟秒数
     */
    public static final int SECONDS_MIN = (int) (MIN / SECOND);
    /**
     * 每小时秒数
     */
    public static final int SECONDS_HOUR = (int) (HOUR / SECOND);
    /**
     * 每天小时数
     */
    public static final int HOUR_DAY = (int) (DAY / HOUR);
    /**
     * 一周的天数
     */
    private static final int DAYOFWEEK_CARDINALITY = 7;

    /**
     * 年月日 时分, 格式如: 2011-01-11 01:10
     */
    private static final DateFormat ymdhmFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    /**
     * 年月日，格式如1970-07-10
     */
    private static final DateFormat ymdFormat = new SimpleDateFormat(
            "yyyy-MM-dd");
    /**
     * 小时和分钟数，格式如10:20
     */
    private static final DateFormat hmFormat = new SimpleDateFormat("HH:mm");
    private static final Calendar calendar = Calendar.getInstance();
    public static final TimeZone TIME_ZONE;

    /**
     * 判断是否合法的时间格式(HH:mm:ss)
     *
     * @param dayTime
     * @return
     */
    public static boolean isValidDayTime(String dayTime) {
        try {
            String[] _timeStr = dayTime.split(":");
            int _hour = Integer.parseInt(_timeStr[0]);
            int _minute = Integer.parseInt(_timeStr[1]);
            int _second = Integer.parseInt(_timeStr[2]);
            if (_hour < 0 || _hour > 23) {
                return false;
            }

            if (_minute < 0 || _minute > 59) {
                return false;
            }

            if (_second < 0 || _second > 59) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否合法的时间格式(HH:mm)
     *
     * @param hhmm
     * @return
     */
    public static boolean isValidHhMmTime(String hhmm) {
        try {
            String[] _timeStr = hhmm.split(":");
            int _hour = Integer.parseInt(_timeStr[0]);
            int _minute = Integer.parseInt(_timeStr[1]);
            if (_hour < 0 || _hour > 23) {
                return false;
            }

            if (_minute < 0 || _minute > 59) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据创建时间和有效时间计算截止时间
     *
     * @param start     物品的创建时间
     * @param validTime 物品的有效时间长度
     * @param timeUnit  有效时间的单位 {@link TimeUtil#MILLI_SECOND} ~ {@link TimeUtil#DAY}
     * @return 物品的截止时间
     */
    public static long getDeadLine(Timestamp start, long validTime,
                                   long timeUnit) {
        return TimeUtil.getDeadLine(start.getTime(), validTime, timeUnit);
    }

    /**
     * 根据创建时间和有效时间计算截止时间
     *
     * @param start     物品的创建时间
     * @param validTime 物品的有效时间长度
     * @param timeUnit  有效时间的单位 {@link TimeUtil#MILLI_SECOND} ~ {@link TimeUtil#DAY}
     * @return 物品的截止时间
     */
    public static long getDeadLine(long start, long validTime, long timeUnit) {
        return start + validTime * timeUnit;
    }

    /**
     * 获取当天零点时间
     *
     * @return
     */
    public static long getTodayBegin(TimeService timeService) {
        Calendar _calendar = Calendar.getInstance();
        _calendar.setTimeInMillis(timeService.now());
        _calendar.set(Calendar.HOUR_OF_DAY, 0);
        _calendar.set(Calendar.MINUTE, 0);
        _calendar.set(Calendar.SECOND, 0);
        _calendar.set(Calendar.MILLISECOND, 0);
        return _calendar.getTimeInMillis();
    }

    /**
     * 获取特定日期当天的零点时间
     *
     * @return
     */
    public static long getBeginOfDay(long time) {
        Calendar _calendar = Calendar.getInstance();
        _calendar.setTimeInMillis(time);
        _calendar.set(Calendar.HOUR_OF_DAY, 0);
        _calendar.set(Calendar.MINUTE, 0);
        _calendar.set(Calendar.SECOND, 0);
        _calendar.set(Calendar.MILLISECOND, 0);
        return _calendar.getTimeInMillis();
    }

    /**
     * 获取时间戳字符串
     *
     * @param date
     * @return
     */
    public static String getUrlTimeStamp(Date date) {
        DateFormat _format = new SimpleDateFormat("yyyyMMddHHmmss");
        return _format.format(date);
    }

    /**
     * 是否是同一天
     *
     * @param src
     * @param target
     * @return
     */
    public static boolean isSameDay(long src, long target) {
        int offset = TIME_ZONE.getRawOffset(); // 只考虑了时区，没考虑夏令时
        return (src + offset) / DAY == (target + offset) / DAY;
    }

    /**
     * 将分钟数转换为小时数和分钟数的数组 如80分钟转换为1小时20分
     *
     * @param mins
     * @return
     */
    public static int[] toTimeArray(int mins) {
        int[] _result = new int[2];
        _result[0] = (int) (mins * MIN / HOUR);
        _result[1] = (int) (mins - _result[0] * HOUR / MIN);
        return _result;
    }

    /**
     * 以格式{@link TimeUtil#hmFormat}解析数据，返回其表示的毫秒数
     *
     * @param source
     * @return
     * @throws ParseException
     */
    public static long getHMTime(String source) throws ParseException {
        Date date = hmFormat.parse(source);
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return hour * TimeUtil.HOUR + minute * TimeUtil.MIN;
    }

    /**
     * 返回小时：分钟格式的时间
     *
     * @param time
     * @return
     */
    public static String formatHMTime(long time) {
        return hmFormat.format(new Date(time));
    }

    /**
     * 以格式{@link TimeUtil#ymdFormat}解析数据，返回其表示的毫秒数
     *
     * @param source
     * @return
     * @throws ParseException
     */
    public static long getYMDTime(String source) throws ParseException {
        Date date = ymdFormat.parse(source);
        return date.getTime();
    }

    /**
     * 以格式{@link TimeUtil#hmFormat}解析数据，返回其表示的毫秒数,从1970至今
     *
     * @author xf
     */
    public static long parseHMTime(String source) {
        Date date;
        try {
            date = hmFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return date.getTime();
    }

    /**
     * 返回 <b>年份-月份-日期</b> 格式的时间. 例如: 2012-12-24
     *
     * @param time
     * @return
     */
    public static String formatYMDTime(long time) {
        return ymdFormat.format(time);
    }

    /**
     * 以格式{@link TimeUtil#ymdhmFormat}解析数据，返回其表示的毫秒数
     *
     * @param source
     * @return
     * @throws ParseException
     */
    public static long getYMDHMTime(String source) throws ParseException {
        Date date = ymdhmFormat.parse(source);
        return date.getTime();
    }

    /**
     * 返回 <b>年份-月份-日期 小时:分钟</b> 格式的时间. 例如: 2012-12-24 15:01
     *
     * @param time
     * @return
     */
    public static String formatYMDHMTime(long time) {
        return ymdhmFormat.format(time);
    }

    /**
     * 返回按时间单位计算后的ms时间，该时间必须足够小以致可用整型表示
     *
     * @param time
     * @param fromTimeUnit
     * @return
     */
    public static long translateTime(int time, long fromTimeUnit) {
        return TimeUtil.translateTime(time, fromTimeUnit, MILLI_SECOND);
    }

    /**
     * 将指定的时间值转化为期望单位的时间值
     *
     * @param time
     * @param fromTimeUnit
     * @param toTimeUnit
     * @return
     */
    public static long translateTime(long time, long fromTimeUnit,
                                     long toTimeUnit) {
        Assert.isTrue(time >= 0);
        long milliTime = time * fromTimeUnit / toTimeUnit;
        Assert.isTrue(milliTime <= Long.MAX_VALUE, String.format(
                "The com.hahazql.util.time value %d is too big!", time));
        return milliTime;
    }

    /**
     * 获得指定时间的小时数
     *
     * @param time
     * @return
     */
    public static int getHourTime(long time) {
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 设置指定时间的设置为给定的时间数(不改变的时间数可填-1)
     *
     * @param time
     * @param year
     * @param month
     * @param day    (月中的天数)
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static long getTime(long time, int year, int month, int day,
                               int hour, int minute, int second) {
        calendar.setTimeInMillis(time);
        int _unChange = -1;
        if (year != _unChange) {
            calendar.set(Calendar.YEAR, year);
        }
        if (month != _unChange) {
            calendar.set(Calendar.MONTH, month);
        }
        if (day != _unChange) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }
        if (hour != _unChange) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute != _unChange) {
            calendar.set(Calendar.MINUTE, minute);
        }
        if (second != _unChange) {
            calendar.set(Calendar.SECOND, second);
        }
        return calendar.getTimeInMillis();
    }

    /**
     * 获得修正后的时间
     *
     * @param originTime
     * @param changeYear
     * @param changeMonth
     * @param changeDay
     * @param changeHour
     * @param changeMinute
     * @param changeSecond
     * @return
     */
    public static long getChangeTime(long originTime, int changeYear,
                                     int changeMonth, int changeDay, int changeHour, int changeMinute,
                                     int changeSecond) {
        calendar.setTimeInMillis(originTime);
        int _unChange = 0;
        if (changeYear != _unChange) {
            calendar.add(Calendar.YEAR, changeYear);
        }
        if (changeMonth != _unChange) {
            calendar.add(Calendar.MONTH, changeMonth);
        }
        if (changeDay != _unChange) {
            calendar.add(Calendar.DAY_OF_MONTH, changeDay);
        }
        if (changeHour != _unChange) {
            calendar.add(Calendar.HOUR_OF_DAY, changeHour);
        }
        if (changeMinute != _unChange) {
            calendar.add(Calendar.MINUTE, changeMinute);
        }
        if (changeSecond != _unChange) {
            calendar.add(Calendar.SECOND, changeSecond);
        }
        return calendar.getTimeInMillis();
    }

    /**
     * 判断start和end是否在同一个星期内(周一为一周开始)
     *
     * @param start
     * @param end
     * @return
     * @author GuoHuang
     * @date 2009-02-04
     */
    public static boolean isInSameWeek(long start, long end) {
        Calendar st = Calendar.getInstance();
        st.setTimeInMillis(start);
        Calendar et = Calendar.getInstance();
        et.setTimeInMillis(end);
        int days = Math.abs(TimeUtil.getSoFarWentDays(st, et));
        if (days < TimeUtil.DAYOFWEEK_CARDINALITY) {
            // 设置Monday为一周的开始
            st.setFirstDayOfWeek(Calendar.MONDAY);
            et.setFirstDayOfWeek(Calendar.MONDAY);
            if (st.get(Calendar.WEEK_OF_YEAR) == et.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 以日期中的日为实际计算单位，计算两个时间点实际日的差距 比如 12-1 23:00 和12-2 01:00，相差1天，而不是小于24小时就算做0天
     * 如果(now - st)为正，则表示now在st之后
     *
     * @param st
     * @param now
     * @return
     */
    public static int getSoFarWentDays(Calendar st, Calendar now) {

        int sign = st.before(now) ? 1 : -1;
        if (now.before(st)) {
            Calendar tmp = now;
            now = st;
            st = tmp;
        }
        int days = now.get(Calendar.DAY_OF_YEAR) - st.get(Calendar.DAY_OF_YEAR);
        if (st.get(Calendar.YEAR) != now.get(Calendar.YEAR)) {
            Calendar cloneSt = (Calendar) st.clone();
            while (cloneSt.get(Calendar.YEAR) != now.get(Calendar.YEAR)) {
                days += cloneSt.getActualMaximum(Calendar.DAY_OF_YEAR);
                cloneSt.add(Calendar.YEAR, 1);
            }
        }

        return days * sign;
    }


    public static int getSoFarWentHours(long time1, long time2) {
        Calendar st = Calendar.getInstance();
        st.setTimeInMillis(time1);

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(time2);


        if (now.before(st)) {
            Calendar tmp = now;
            now = st;
            st = tmp;
        }

        st.clear(Calendar.MILLISECOND);
        st.clear(Calendar.SECOND);
        st.clear(Calendar.MINUTE);

        int diffHour = 0;
        Calendar cloneSt = (Calendar) st.clone();
        while (cloneSt.before(now)) {
            cloneSt.add(Calendar.HOUR, 1);
            diffHour++;
        }

        if (diffHour != 0) {
            return diffHour - 1;
        } else {
            return diffHour;
        }
    }

    /**
     * specTime is in [st,now] or not?
     *
     * @param st
     * @param now
     * @param specTime
     * @return
     */
    private static boolean hasSpecTimeBetween(long st, long now, long specTime) {
        if (st <= specTime && specTime <= now) {
            return true;
        }
        return false;
    }

    /**
     * 得到从time1 到time2 中,specTime所指定的时分秒的时刻,有几次
     *
     * @param time1
     * @param time2
     * @param specTime
     * @return
     */
    public static int getSpecTimeCountBetween(long time1, long time2, long specTime) {
        Calendar st = Calendar.getInstance();
        st.setTimeInMillis(time1);

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(time2);

        Calendar spec = Calendar.getInstance();
        spec.setTimeInMillis(specTime);

        if (now.before(st)) {
            Calendar tmp = now;
            now = st;
            st = tmp;
        }

        //第一个时间的年月日和被比较时间的时间部分合成
        Calendar st_spec = mergeDateAndTime(st, spec);

        if (isSameDay(time1, time2)) {
            if (hasSpecTimeBetween(time1, time2, st_spec.getTimeInMillis())) {
                return 1;
            } else {
                return 0;
            }
        }

        int diffDay = 0;
        Calendar cloneSt = (Calendar) st_spec.clone();
        while (cloneSt.before(now)) {
            cloneSt.add(Calendar.DATE, 1);
            diffDay++;
        }

        if (st.after(st_spec)) {
            diffDay--;
        }

        return diffDay;
    }


    /**
     * 把日期和时间合并
     *
     * @param date 代表一个日期，方法其只取日期部分
     * @param time 代表一个时间，方法其只取时间部分
     * @return
     */
    public static Calendar mergeDateAndTime(Calendar date, Calendar time) {
        Calendar cal = Calendar.getInstance();
        cal.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date
                .get(Calendar.DATE), time.get(Calendar.HOUR_OF_DAY), time
                .get(Calendar.MINUTE), time.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    /**
     * 获取几天后的当前时间点
     *
     * @param day
     * @return
     */
    public static Date getAfterToday(int day) {
        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, day);

        return c.getTime();
    }

    /**
     * 设置几分钟之后的时间点
     *
     * @param minutes
     * @return
     */
    public static Date getAfterMinutes(int minutes) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + minutes);

        return c.getTime();
    }
}