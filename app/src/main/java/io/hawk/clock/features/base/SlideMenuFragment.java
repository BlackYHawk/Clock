package io.hawk.clock.features.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.hawk.clock.R;
import io.hawk.clock.features.MainActivity;
import io.hawk.clock.interfaces.AntiShakeClickListener;
import io.hawk.clock.util.Globals;
import io.hawk.clock.widget.CircleImageView;


/**
 * Created by lan on 2016/7/30.
 */
public class SlideMenuFragment extends BaseFragment {
    private final String TAG = SlideMenuFragment.class.getSimpleName();
    private MainActivity activity;
    private RelativeLayout layHead;
    private CircleImageView cvHead;
    private TextView tvUsername, tvName;

    private String faceStr = "";
    private static final int req_INFO = 1;

    public static SlideMenuFragment newInstance() {
        SlideMenuFragment fragment = new SlideMenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ac_ui_sidebar, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        layHead = (RelativeLayout) view.findViewById(R.id.layHead);
        cvHead = (CircleImageView) view.findViewById(R.id.cvHead);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvName = (TextView) view.findViewById(R.id.tvName);

        tvUsername.setText(Globals.USERNAME);

        layHead.setOnClickListener(onClickListener);
    }

    /**
     * 点击事件
     * @param v
     */
    private AntiShakeClickListener onClickListener = new AntiShakeClickListener() {
        @Override
        public void onAntiShakeClick(View v) {
            Intent intent = null;
            switch (v.getId()) {

                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case req_INFO:

                    break;
            }
        }
        else if(resultCode == Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case req_INFO :
                    int type = data.getIntExtra("type", 0);

                    if(type == 1) {

                    }
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        logger.e(TAG, "onResume");
    }
}
