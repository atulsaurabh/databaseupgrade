package com.technoride.server.databaseupgrade.setting;

public interface ScheduleTime
{
    public static final long ONE_SECOND=1000;
    public static final long ONE_MINUTE=60*ONE_SECOND;
    public static final long ONE_HOUR=60*ONE_MINUTE;
    public static final long ONE_DAY=24*ONE_HOUR;
    public static final long ONE_WEEK=7*ONE_DAY;
    public static final long ONE_MONTH=4*ONE_WEEK;
    public static final long ONE_QUARTER=3*ONE_MONTH;
    public static final long HALF_YEAR=2*ONE_QUARTER;
    public static final long ONE_YEAR=2*HALF_YEAR;
}
