package com.glevel.dungeonhero;

import android.app.Application;
import android.graphics.Typeface;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;

import io.fabric.sdk.android.Fabric;

public class BaseApplication extends Application {

    public static String sPackageName;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            // memory leak detector
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }

            LeakCanary.install(this);

            // setup strict mode
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }


        // fonts caching
        loadFonts();

        Fabric.with(getApplicationContext(), new Crashlytics());

        sPackageName = getPackageName();
    }

    /**
     * Loads the required fonts.
     */
    private void loadFonts() {
        FONTS.splash = Typeface.createFromAsset(getAssets(), "fonts/font_splash.ttf");
        FONTS.main = Typeface.createFromAsset(getAssets(), "fonts/font_main.ttf");
        FONTS.text = Typeface.createFromAsset(getAssets(), "fonts/font_text.otf");
    }

    public static class FONTS {
        public static Typeface splash, main, text;
    }

}
