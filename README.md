# WindowJobScheduler
Need a periodic jobscheduler that runs every day between a certain timeframe?

The JobScheduler APIs (and the GcmNetworkManager backport) are a great way to write code that needs to run in the background while at the same time being memory and battery efficient. However, several people have a requirement to run code in the background between a certain time frame (or window) repeatedly, which isn't possible by default.

WindowJobScheduler provides this capability by setting the appropriate values for PeriodicTask.Builder the first time to ensure it is run at the proper time, and then allowing you to reschedule every time a job is completed for future runs. You get all the features from PeriodicTask that you might want, such as network or battery status.

The ideal use case is for daily background syncing of app data, particularly when developers might want this to happen in a window when their servers don't receive much load.

## How to use
Scheduling tasks is fairly simple:

    new WindowTask()
                .setService(SyncService.class)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .setTag(SyncService.TAG)
                .setStartTime(windowStartHour, windowStartMinute)
                .setWindowHours(windowLength)
                .schedule(gcmNetworkManager);

Your GcmTaskService needs to extend WindowService instead. When your task runs, and you're about to return either success or failure (i.e., not calling for an exponential backoff), request WindowJobScheduler to reschedule so as to ensure the next sync runs at the right time. Do this as follows:

    reschedule(SyncService.class, taskParams);
  
## Known issue
If, for any reason, a task fails to run in the required window, it might be rescheduled incorrectly. I have not added any inbuilt method to test for this and how to handle it, leaving it (for now) to library's users. I would love to hear your thoughts. I intend to provide an ability to set a "strict" mode which could be used going forward to test for these circumstances and skip running, if a project so requires.
