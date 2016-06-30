package co.haptik.windowjobscheduler;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Created by Raveesh on 30/06/16.
 */
public class WindowTaskTest {

    @Test
    public void testGetPeriod() throws Exception {
        Calendar now = Calendar.getInstance();
        now.setLenient(true);
        WindowTask task = new WindowTask()
                .setWindowHours(1)
                .setStartTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE) + 1);

        long period = task.getPeriod();

        /**
         * 1 hour = 60 minutes = 3600 seconds, + 60 seconds for starting a minute later
         */
        assertTrue("Period:"+period, period == 3660);

        task = new WindowTask()
                .setWindowHours(6)
                .setStartTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE) + 5);
        period = task.getPeriod();

        /**
         * 6 hours = 360 minutes = 21600 seconds, + 300 seconds for 5 minutes later
         */
        assertTrue("Period:"+period, period == 21900);

        task = new WindowTask()
                .setWindowHours(6)
                .setStartTime(now.get(Calendar.HOUR_OF_DAY)-3, now.get(Calendar.MINUTE));
        period = task.getPeriod();

        /**
         * 21 hours (24-3) = 1260 minutes = 75600 seconds, + 21600 for 6 hours later
         */
        assertTrue("Period:"+period, period == 97200);
    }
}