package io.hawk.clock.features.base;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.hawk.clock.AppContext;
import io.hawk.clock.R;
import io.hawk.clock.interfaces.Logger;
import io.hawk.clock.interfaces.StringFetcher;
import io.hawk.clock.modules.ActComponent;
import io.hawk.clock.modules.ActModule;
import io.hawk.clock.receiver.BroadcastConfig;
import io.hawk.clock.state.ApplicationState;
import io.hawk.clock.util.Globals;
import io.hawk.clock.util.StringUtil;
import io.hawk.clock.util.UIHelper;


public abstract class BaseActivity extends AppCompatActivity {

	// 当有Fragment Attach到这个Activity的时候，就会保存
	private SystemBarTintManager tintManager;
	private Map<String, WeakReference<BaseFragment>> fragmentRefs;
	private ActComponent actComponent;
	protected @Inject ApplicationState mState;
	protected @Inject Logger logger;
	protected @Inject StringFetcher mStringFetcher;
	protected Toolbar mToolbar;
	private TextView tvTitle;
	private Unbinder mUnBinder;

	private PushReceiver pushReceiver;
	private boolean first = false;           //首次加载activity，加载数据
	private boolean show = true;             //标题栏显示控制
	protected static final int CLICK_INTERVAL_TIME = 1000;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(getLayoutRes());
		component().inject(this);

		initWindow();
		bindView();
		fragmentRefs = new ArrayMap<String, WeakReference<BaseFragment>>();
		first = true;
		mState.registerEvent(this);
	}

	@TargetApi(19)
	private void initWindow(){
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintColor(ContextCompat.getColor(this, R.color.colorPrimary));
			tintManager.setStatusBarTintEnabled(true);
		}
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			onWindowAnimations();
		}
	}

	@TargetApi(21)
	protected void onWindowAnimations(){}

	@CallSuper
	protected void bindView() {
		if (autoBindViews()) {
			mUnBinder = ButterKnife.bind(this);
		}

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
		}
	}

	protected boolean autoBindViews() {
		return true;
	}

	protected void unbindView() {
		if (autoBindViews() && mUnBinder != null) {
			mUnBinder.unbind();
		}
	}

	public void setTitle(int strId) {
		if(tvTitle != null) {
			tvTitle.setText(strId);
		}
	}

	public void setTitle(String text) {
		if(tvTitle != null) {
			tvTitle.setText(text);
		}
	}

	public void setToolbarAlpha(int alpha) {
		if(mToolbar != null) {
			mToolbar.getBackground().setAlpha(alpha);
		}
	}

	public void toogleToolbar() {
		if (mToolbar != null) {
			if(show) {
				mToolbar.setVisibility(View.GONE);
			}
			else {
				mToolbar.setVisibility(View.VISIBLE);
			}
			show = !show;
		}
	}

	protected int getLayoutRes() {
		return 0;
	}

	protected void setDisplayNone() {
		if(getSupportActionBar() != null) {
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		}
	}

	protected void setDisplayBack() {
		if(getSupportActionBar() != null) {
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	protected void setViewVisiable(View v, int visibility) {
		if (v != null)
			v.setVisibility(visibility);
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

	private void showPushMsg(final String msgId, final String message, final String extras) {
		String title = "";

		if (!StringUtil.isEmpty(extras)) {
			try {
				JSONObject jsonObject = new JSONObject(extras);

				if (jsonObject.length() > 0) {
					title = jsonObject.optString("title");
				}
			} catch (JSONException e) {

			}
		}

		UIHelper.showPushDialog(this, title, message, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(Globals.notificationMap != null && Globals.notificationMap.containsKey(msgId)) {
					Integer notificationId = Globals.notificationMap.get(msgId);
					UIHelper.cancelNotification(BaseActivity.this.getAppContext().getApplication(), notificationId);
					Globals.notificationMap.remove(msgId);
				}

				String type = "", url = "", keyId = "";
				if (!StringUtil.isEmpty(extras)) {
					try {
						JSONObject jsonObject = new JSONObject(extras);

						if (jsonObject.length() > 0) {
							type = jsonObject.optString("type");
							url = jsonObject.optString("url");
							keyId = jsonObject.optString("keyId");
						}
					} catch (JSONException e) {

					}
				}
			}
		}, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(Globals.notificationMap != null && Globals.notificationMap.containsKey(msgId)) {
					Integer notificationId = Globals.notificationMap.get(msgId);
					UIHelper.cancelNotification(BaseActivity.this.getAppContext().getApplication(), notificationId);
					Globals.notificationMap.remove(msgId);
				}

				dialog.cancel();
			}
		});
	}

	public void showActivity(Class<? extends AppCompatActivity> activity) {
		Intent intent = new Intent(this, activity);
		startActivity(intent);
	}

	public void showActivity(Class<? extends AppCompatActivity> activity, Bundle bundle) {
		Intent intent = new Intent(this, activity);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void addFragment(String tag, BaseFragment fragment) {
		if(fragment != null && fragmentRefs != null) {
			fragmentRefs.put(tag, new WeakReference<BaseFragment>(fragment));
		}
	}

	public void removeFragment(String tag) {
		if(fragmentRefs != null) {
			fragmentRefs.remove(tag);
		}
	}

	protected void addMenuFragment(int layId, BaseFragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().add(layId, fragment).commit();
	}

	public void backFragment() {
		getSupportFragmentManager().popBackStack();
	}

	//首次加载，需要请求网络，在onResume周期
	protected void onInitRequestData(){}

	@Override
	protected void onResume() {
		super.onResume();

		if(pushReceiver == null) {
			pushReceiver = new PushReceiver();
			UIHelper.registBroadCast(this, pushReceiver, BroadcastConfig.PUSH_MSG);
		}

		if(first) {
			onInitRequestData();
		}
	}

	@Override
	protected void onPause() {
		if(pushReceiver != null) {
			UIHelper.unRegistBroadCast(this, pushReceiver);
			pushReceiver = null;
		}
		first = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mState.unregisterEvent(this);
		UIHelper.cancelToast();
		UIHelper.dismiss();
		UIHelper.cancelAlertDialog();
		UIHelper.cancelPushDialog();
		super.onDestroy();
		unbindView();
	}

	@Override
	public void onBackPressed() {
		if (onBackClick()) {
			finish();
		}
		else {
			super.onBackPressed();
		}
	}

	private boolean onBackClick() {
		Set<String> keys = fragmentRefs.keySet();
		for (String key : keys) {
			WeakReference<BaseFragment> fragmentRef = fragmentRefs.get(key);
			BaseFragment fragment = fragmentRef.get();
			if (fragment != null && fragment.isVisible() && fragment.onBackClick()) {
				return true;
			}
		}

		return false;
	}

	protected AppContext getAppContext() {
		return AppContext.getInstance();
	}

	public ActComponent component() {
		if(actComponent == null) {
			actComponent = getAppContext().component()
					.plus(new ActModule(this));
		}

		return actComponent;
	}

	public ApplicationState getState() {
		return mState;
	}

	public class PushReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(BroadcastConfig.PUSH_MSG)) {
				Bundle bundle = intent.getExtras();
				String messageId = bundle.getString("messageId");
				String message = bundle.getString("message");
				String extras = bundle.getString("extras");

				logger.e("Jpush", "messageId:" + messageId + " message:" + message + " extras:" + extras);

				if(context instanceof BaseActivity) {
					((BaseActivity) context).showPushMsg(messageId, message, extras);
				}
			}
		}
	}

}
