package co.haptik.windowjobscheduler.demo;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.TaskParams;

import co.haptik.windowjobscheduler.SchedulingService;

/**
 * Created by Raveesh on 30/06/16.
 */
public class SyncService extends SchedulingService {
    public static final String TAG = "SyncService";

    @Override
    public int onRunTask(TaskParams taskParams) {
        /**
         * Call reschedule only if Task is success (or fail), and no exponential backoff needs to take
         * place
         */
        reschedule(SyncService.class, taskParams);
        return GcmNetworkManager.RESULT_SUCCESS;
    }
}
