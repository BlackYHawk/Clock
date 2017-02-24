package io.hawk.clock;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

import io.hawk.clock.modules.AppComponent;
import io.hawk.clock.modules.AppModule;
import io.hawk.clock.modules.DaggerAppComponent;
import io.hawk.clock.util.Constant;


/**
 * Created by heyong on 16/7/10.
 */
public class AppContext extends DefaultApplicationLike {

    private static Application _context;
    private static AppContext _appContext;
    private AppComponent appComponent;

    public AppContext(Application application, int tinkerFlags,
                                 boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                                 long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplication(), "d234968672", false);
        _context = getApplication();
        _appContext = this;

        initComponent();
        Fresco.initialize(_context, createFrescoConfig());

        if(BuildConfig.DEBUG) {
            enabledStrictMode();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // TODO: 安装tinker
        Beta.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    private void initComponent() {
        this.appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this.getApplication()))
                .build();
    }
    private void enabledStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }

    private ImagePipelineConfig createFrescoConfig() {
        DiskCacheConfig mainDiskCacheConfig = DiskCacheConfig.newBuilder(_context)
                .setBaseDirectoryPath(_context.getExternalCacheDir())
                .setBaseDirectoryName(Constant.DEFAULT_CACHE_DIR)
                .setMaxCacheSize(100 * 1024 * 1024)
                .setMaxCacheSizeOnLowDiskSpace(10 * 1024 * 1024)
                .setMaxCacheSizeOnVeryLowDiskSpace(5 * 1024 * 1024)
                .setVersion(1)
                .build();
        return ImagePipelineConfig.newBuilder(_context)
                .setDownsampleEnabled(true)
                .setMainDiskCacheConfig(mainDiskCacheConfig)
                .build();
    }

    public static AppContext getInstance() {
        return _appContext;
    }

    public AppComponent component() {
        return appComponent;
    }
}
