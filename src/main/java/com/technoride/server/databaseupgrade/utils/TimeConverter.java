package com.technoride.server.databaseupgrade.utils;

import com.technoride.server.databaseupgrade.setting.ScheduleTime;

import java.lang.reflect.Field;

public class TimeConverter
{
    public long getScheduleTime(String time)
    {
        try {
            ScheduleTime scheduleTime = new ScheduleTime() {};
            Class scheduleTimeClasss = scheduleTime.getClass();
            Field [] allFields = scheduleTimeClasss.getFields();
            long ltime=0l;
            for (Field f : allFields)
            {
                if (f.getName().equals(time))
                    ltime = (long)f.get(scheduleTime);
            }
            return ltime;
        }
        catch (IllegalAccessException iae)
        {
            iae.printStackTrace();
        }
     return 0l;
    }
}
