package io.hawk.clock.features.alarm;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.hawk.clock.R;
import io.hawk.clock.features.base.BaseActivity;

/**
 * Created by lan on 2017/2/24.
 */

public class AlarmAddActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayBack();
        setTitle(R.string.alarm_add_title);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.ac_ui_alarm_add;
    }
}
