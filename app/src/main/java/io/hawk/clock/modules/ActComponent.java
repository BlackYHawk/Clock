package io.hawk.clock.modules;


import dagger.Subcomponent;
import io.hawk.clock.features.base.BaseActivity;
import io.hawk.clock.features.base.BaseFragment;
import io.hawk.clock.qualifiers.ActivityScope;

/**
 * Created by lan on 2016/6/29.
 */
@ActivityScope
@Subcomponent(modules = ActModule.class)
public interface ActComponent {

    void inject(BaseActivity baseActivity);

    void inject(BaseFragment baseFragment);

    BaseActivity getActivity();

}
