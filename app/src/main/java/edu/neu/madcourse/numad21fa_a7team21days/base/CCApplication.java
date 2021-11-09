package edu.neu.madcourse.numad21fa_a7team21days.base;

import com.blankj.utilcode.util.Utils;

import java.util.UUID;

import androidx.multidex.MultiDexApplication;

public class CCApplication extends MultiDexApplication {

    private static final String TAG = CCApplication.class.getSimpleName();

    private static CCApplication INSTANCE;
    private int activityCount;
    private boolean mConfigUpdated = false;
    private String sessionId;

    public static CCApplication getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            INSTANCE = new CCApplication();
            INSTANCE.onCreate();
            return INSTANCE;
        }
    }

    public CCApplication() {
        INSTANCE = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mConfigUpdated = false;
        INSTANCE = this;
        sessionId = getUUID();
        Utils.init(this);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }


    public boolean isInForeground() {
        return (activityCount > 0);
    }

    public String getSessionId() {
        return sessionId;
    }
}
