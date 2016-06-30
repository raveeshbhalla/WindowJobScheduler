package co.haptik.windowjobscheduler.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.gcm.GcmNetworkManager;

import java.util.Calendar;

import co.haptik.windowjobscheduler.WindowTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar now = Calendar.getInstance();
        now.setLenient(true);
        new WindowTask()
                .setService(SyncService.class)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .setTag(SyncService.TAG)
                .setStartTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE) + 2)
                .setWindowHours(1)
                .schedule(GcmNetworkManager.getInstance(this));
    }
}
