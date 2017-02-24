package io.hawk.clock.features.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import javax.inject.Inject;

import io.hawk.clock.interfaces.Logger;
import io.hawk.clock.interfaces.StringFetcher;
import io.hawk.clock.state.ApplicationState;
import io.hawk.clock.util.LOG;

/**
 * Created by lan on 2016/6/29.
 */
public abstract class BaseFragment extends Fragment {
    protected @Inject ApplicationState mState;
    protected @Inject Logger logger;
    protected @Inject StringFetcher mStringFetcher;
    protected int titleId = -1;
    protected static final int CLICK_INTERVAL_TIME = 1000;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BaseActivity)
            ((BaseActivity) context).addFragment(toString(), this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).component().inject(this);

            if(titleId != -1) {
                ((BaseActivity) getActivity()).setTitle(titleId);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mState.registerEvent(this);
    }

    @Override
    public void onPause() {
        mState.unregisterEvent(this);
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (getActivity() != null && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).removeFragment(this.toString());
    }

    protected void setTitle(int strId) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setTitle(strId);
        }
    }

    private long lastClickTime = 0;

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > CLICK_INTERVAL_TIME) {
            lastClickTime = currentTime;
            onOptionsShakeItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onOptionsShakeItemSelected(MenuItem item) {};

    protected void dismiss() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity)getActivity()).backFragment();
        }
    }

    protected void finish() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    /**
     * 返回按键被点击了
     *
     * @return
     */
    public boolean onBackClick() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!LOG.D) {
            return;
        }

    }
}
