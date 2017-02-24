package io.hawk.clock.modules.library;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.hawk.clock.state.ApplicationState;

/**
 * Created by lan on 2016/4/14.
 */
@Module
public class StateModule {

    @Provides
    @Singleton
    public Bus provideEventBus() {
        return new Bus();
    }

    @Provides @Singleton
    public ApplicationState provideApplicationState(Bus bus) {
        return new ApplicationState(bus);
    }

}
