package wq.com.tcpdemo;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by dou_d on 2018/7/7.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
//        } else {
//            Timber.plant(new CrashReportingTree());
//        }
        Timber.plant(new Timber.DebugTree());
    }

    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {

        }
    }
}
