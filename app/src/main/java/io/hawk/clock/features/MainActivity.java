package io.hawk.clock.features;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import butterknife.OnClick;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import io.hawk.clock.R;
import io.hawk.clock.features.alarm.AlarmAddActivity;
import io.hawk.clock.features.base.BaseActivity;
import io.hawk.clock.features.base.SlideMenuFragment;
import io.hawk.clock.util.Globals;
import io.hawk.clock.util.UIHelper;



/**
 * Created by lan on 2016/6/29.
 */
public class MainActivity extends BaseActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    private SlideMenuFragment menuFragment;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayNone();
        setTitle(R.string.home_title);
        initPush();
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void initPush(){
        JPushInterface.init(getAppContext().getApplication());
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.mipmap.ic_launcher;
        JPushInterface.setPushNotificationBuilder(1, builder);

        String devicetoken = JPushInterface.getRegistrationID(getApplicationContext());
    }

    protected void bindView() {
        super.bindView();
        menuFragment = SlideMenuFragment.newInstance();
        addMenuFragment(R.id.slidemenu, menuFragment);

        mDrawer = (DrawerLayout) findViewById(R.id.drawLayout);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }

        };
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @OnClick(R.id.fabBtn)
    void onFabOnClick(View view) {
        Intent intent = new Intent(MainActivity.this, AlarmAddActivity.class);
        startActivity(intent);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.ac_ui_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this.getAppContext().getApplication());
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this.getAppContext().getApplication());
    }

    private void clearData() {
        Globals.USERNAME = null;
        if(Globals.notificationMap != null) {
            Globals.notificationMap.clear();
            Globals.notificationMap = null;
        }
    }

    @Override
    protected void onDestroy() {
        logger.e(TAG, "onDestroy");
        clearData();
        super.onDestroy();
    }

    public boolean isDrawerOpen() {
        return mDrawer.isDrawerOpen(GravityCompat.START);
    }

    public void closeDrawer() {
        mDrawer.closeDrawer(GravityCompat.START);
    }

    private long mExitTime = 0;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        boolean status = true;

        if(isDrawerOpen()) {
            closeDrawer();
            status = false;
        }

        if(status) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                UIHelper.showToast(this, "再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }

}






