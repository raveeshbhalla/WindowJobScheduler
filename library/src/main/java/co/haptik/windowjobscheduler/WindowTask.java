package co.haptik.windowjobscheduler;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Raveesh on 30/06/16.
 */
public class WindowTask {
    private Class<? extends GcmTaskService> service;
    private boolean requiresCharging = false;
    private int requiredNetworkState = Task.NETWORK_STATE_CONNECTED;
    private boolean persisted = false;
    private int startHour;
    private int startMinute;
    private int windowHours = 6;
    private String tag;
    private boolean updateCurrent = false;
    protected Bundle extras;
    public boolean strict = false;

    private static final String KEY_REQUIRES_CHARGING = "requires_charging";
    private static final String KEY_REQUIRED_NETWORK = "required_network";
    private static final String KEY_PERSISTED = "persisted";
    private static final String KEY_START_HOUR = "start_hour";
    private static final String KEY_START_MINUTE = "start_minute";
    private static final String KEY_WINDOW_HOURS = "window_hours";
    private static final String KEY_UPDATE_CURRENT = "update_current";
    private static final String KEY_STRICT = "strict";



    @IntDef({Task.NETWORK_STATE_ANY, Task.NETWORK_STATE_CONNECTED, Task.NETWORK_STATE_UNMETERED})
    public @interface NetworkState{}

    public WindowTask() {}

    WindowTask(Class<? extends SchedulingService> service, TaskParams params){
        setTag(params.getTag());
        Bundle extras = params.getExtras();
        //noinspection WrongConstant
        setRequiredNetwork(extras.getInt(KEY_REQUIRED_NETWORK, Task.NETWORK_STATE_CONNECTED));
        setPersisted(extras.getBoolean(KEY_PERSISTED, false));
        setRequiresCharging(extras.getBoolean(KEY_REQUIRES_CHARGING));
        setStartTime(extras.getInt(KEY_START_HOUR, 0), extras.getInt(KEY_START_MINUTE, 0));
        setWindowHours(extras.getInt(KEY_WINDOW_HOURS, 6));
        setUpdateCurrent(extras.getBoolean(KEY_UPDATE_CURRENT, false));
        setStrictMode(extras.getBoolean(KEY_STRICT, false));
        setExtras(extras);
        setService(service);
    }

    public WindowTask setService(Class<? extends SchedulingService> service){
        this.service = service;
        return this;
    }

    public WindowTask setRequiresCharging(boolean requiresCharging){
        this.requiresCharging = requiresCharging;
        return this;
    }

    public WindowTask setRequiredNetwork(@NetworkState int networkState){
        this.requiredNetworkState = networkState;
        return this;
    }
    
    public WindowTask setPersisted(boolean persisted){
        this.persisted = persisted;
        return this;
    }

    public WindowTask setStartTime(int startHour, int startMinute){
        this.startHour = startHour;
        this.startMinute = startMinute;
        return this;
    }

    public WindowTask setWindowHours(@IntRange(from = 1, to = 24) int hours){
        this.windowHours = hours;
        return this;
    }
    
    public WindowTask setTag(@NonNull String tag){
        this.tag = tag;
        return this;
    }
    
    public WindowTask setUpdateCurrent(boolean updateCurrent){
        this.updateCurrent = updateCurrent;
        return this;
    }

    public WindowTask setExtras(Bundle extras){
        this.extras = extras;
        return this;
    }

    public WindowTask setStrictMode(boolean strictMode){
        this.strict = strictMode;
        return this;
    }

    public void schedule(@NonNull GcmNetworkManager networkManager) throws IllegalStateException{
        if (tag == null) {
            throw new IllegalStateException("Tag cannot be null for WindowTask, it is used for rescheduling to the right time");
        }
        else if (windowHours == 0) {
            throw new IllegalStateException("Window hours not defined");
        }

        if (extras == null){
            extras = new Bundle();
        }
        extras.putBoolean(KEY_REQUIRES_CHARGING, requiresCharging);
        extras.putInt(KEY_REQUIRED_NETWORK, requiredNetworkState);
        extras.putInt(KEY_WINDOW_HOURS, windowHours);
        extras.putInt(KEY_START_MINUTE, startMinute);
        extras.putInt(KEY_START_HOUR, startHour);
        extras.putBoolean(KEY_PERSISTED, persisted);
        extras.putBoolean(KEY_UPDATE_CURRENT, updateCurrent);
        extras.putBoolean(KEY_STRICT, strict);

        networkManager.cancelTask(tag, service);
        PeriodicTask.Builder task = new PeriodicTask.Builder()
                .setService(service)
                .setPersisted(persisted)
                .setRequiresCharging(requiresCharging)
                .setRequiredNetwork(requiredNetworkState)
                .setTag(tag)
                .setUpdateCurrent(updateCurrent)
                .setExtras(extras)
                .setPeriod(getPeriod())
                .setFlex(windowHours * 60);
        networkManager.schedule(task.build());
    }

    protected long getPeriod() {
        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, startHour);
        start.set(Calendar.MINUTE, startMinute);
        if (start.before(now)) {
            start.setLenient(true);
            start.add(Calendar.DATE, 1);
        }

        start.setLenient(true);
        start.add(Calendar.HOUR_OF_DAY, windowHours);
        return (start.getTimeInMillis() - now.getTimeInMillis())/1000;
    }
}
