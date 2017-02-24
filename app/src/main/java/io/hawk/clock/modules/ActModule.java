package io.hawk.clock.modules;


import dagger.Module;
import dagger.Provides;
import io.hawk.clock.features.base.BaseActivity;
import io.hawk.clock.qualifiers.ActivityScope;

/**
 * Created by lan on 2016/6/29.
 */
@Module
public class ActModule {
    private final BaseActivity activity;

    public ActModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides @ActivityScope
    public BaseActivity provideActivity() {
        return activity;
    }

}
