package io.hawk.clock.modules.library;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.hawk.clock.interfaces.Logger;
import io.hawk.clock.interfaces.StringFetcher;
import io.hawk.clock.interfaces.impl.AndroidLogger;
import io.hawk.clock.interfaces.impl.AndroidStringFetcher;
import io.hawk.clock.qualifiers.ApplicationContext;

/**
 * Created by lan on 2016/4/14.
 */
@Module
public class UtilModule {

    @Provides @Singleton
    public Gson provideGson() {
        return new Gson();
    }

    @Provides @Singleton
    public Logger provideLogger() {
        return new AndroidLogger();
    }

    @Provides @Singleton
    public StringFetcher provideStringFetcher(@ApplicationContext Context context) {
        return new AndroidStringFetcher(context);
    }

}
