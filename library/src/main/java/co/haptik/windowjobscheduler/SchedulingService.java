package co.haptik.windowjobscheduler;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by Raveesh on 30/06/16.
 */
public abstract class SchedulingService extends GcmTaskService {

    public void reschedule(Class<? extends SchedulingService> service, TaskParams params) {
        GcmNetworkManager networkManager = GcmNetworkManager.getInstance(this);
        WindowTask windowTask = new WindowTask(service, params);
        windowTask.schedule(networkManager);
    }
}
