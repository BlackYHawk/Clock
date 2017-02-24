package io.hawk.clock.interfaces;

import android.view.View;

/**
 * Created by lan on 2016/9/7.
 */
public abstract class AntiShakeClickListener implements View.OnClickListener {

    protected abstract void onAntiShakeClick(View view);

    private static final int CLICK_INTERVAL_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > CLICK_INTERVAL_TIME) {
            lastClickTime = currentTime;
            onAntiShakeClick(v);
        }
    }
}
