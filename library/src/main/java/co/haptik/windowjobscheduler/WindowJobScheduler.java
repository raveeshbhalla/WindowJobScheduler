package co.haptik.windowjobscheduler;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.gcm.PeriodicTask;

import java.util.GregorianCalendar;

/**
 * Created by Raveesh on 29/06/16.
 */
public class WindowJobScheduler {

    public static void schedule(Context context, PeriodicTask task, GregorianCalendar start, int windowHours){
        Intent intent = new Intent(context, ScheduleReceiver.class);

    }
}
