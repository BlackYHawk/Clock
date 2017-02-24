package io.hawk.clock.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import io.hawk.clock.modules.library.StateModule;
import io.hawk.clock.modules.library.UtilModule;
import io.hawk.clock.qualifiers.ApplicationContext;

/**
 * Created by lan on 2016/6/29.
 */
@Singleton
@Component (
        modules = {
                AppModule.class,
                UtilModule.class,
                StateModule.class
        }
)
public interface AppComponent {

        ActComponent plus(ActModule actModule);

        @ApplicationContext
        Context appContext();
}
