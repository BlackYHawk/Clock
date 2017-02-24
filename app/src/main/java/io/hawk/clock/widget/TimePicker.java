package io.hawk.clock.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;

import io.hawk.clock.R;
import io.hawk.clock.widget.wheel.OnWheelChangedListener;
import io.hawk.clock.widget.wheel.WheelView;
import io.hawk.clock.widget.wheel.adapters.ArrayWheelAdapter;

/**
 * Created by lan on 2016/9/21.
 */
public class TimePicker extends LinearLayout {
    private final int visibleItems = 5;
    private Calendar calendar = Calendar.getInstance();
    private Context mContext;
    private WheelView wvZone, wvHour, wvMinute;
    private ArrayWheelAdapter<String> mZoneAdapter, mHourAdapter, mMinuteAdapter;
    private ArrayList<String> zoneList, hourList, minuteList;
    private int mCurrentZone, mCurrentHour, mCurrentMinute;
    private OnTimeListener onTimeListener;

    public TimePicker(Context context) {
        super(context);
        initView(context);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.widget_clock_picker, this, true);
        wvZone = (WheelView) findViewById(R.id.wvZone);
        wvHour = (WheelView) findViewById(R.id.wvHour);
        wvMinute = (WheelView) findViewById(R.id.wvMinute);

        wvZone.setCyclic(false);
        wvZone.setVisibleItems(visibleItems);
        wvHour.setCyclic(true);
        wvHour.setVisibleItems(visibleItems);
        wvMinute.setCyclic(true);
        wvMinute.setVisibleItems(visibleItems);
        wvZone.addChangingListener(onWheelChangedListener);
        wvHour.addChangingListener(onWheelChangedListener);
        wvMinute.addChangingListener(onWheelChangedListener);

        initData(context);
    }

    private void initData(Context context) {
        mCurrentHour = calendar.get(Calendar.HOUR_OF_DAY);
        mCurrentMinute = calendar.get(Calendar.MINUTE);
        mCurrentZone = mCurrentHour / 12;    //当前时间区域

        zoneList = new ArrayList<>();
        hourList = new ArrayList<>();
        minuteList = new ArrayList<>();

        for(int i=0; i < 2; i++) {
            zoneList.add( formatZone(i) );
        }
        for(int i=1; i <= 12; i++) {
            hourList.add( formatHour(i) );
        }
        for(int i=0; i < 60; i++) {
            minuteList.add( formatMinute(i) );
        }
        mZoneAdapter = new ArrayWheelAdapter<>(context, (String[]) zoneList.toArray(new String[zoneList.size()]));
        wvZone.setViewAdapter(mZoneAdapter);
        wvZone.setCurrentItem(mCurrentZone);

        mHourAdapter = new ArrayWheelAdapter<>(context, (String[]) hourList.toArray(new String[hourList.size()]));
        wvHour.setViewAdapter(mHourAdapter);
        wvHour.setCurrentItem(mCurrentHour - 1);

        mMinuteAdapter = new ArrayWheelAdapter<>(mContext, (String[]) minuteList.toArray(new String[minuteList.size()]));
        wvMinute.setViewAdapter(mMinuteAdapter);
        wvMinute.setCurrentItem(mCurrentMinute - 1);
    }

    private OnWheelChangedListener onWheelChangedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (wheel == wvZone) {
                mCurrentZone = formatZone(zoneList.get(wvZone.getCurrentItem()));
            } else if (wheel == wvHour) {
                mCurrentHour = formatHour(hourList.get(wvHour.getCurrentItem()));
            } else if (wheel == wvMinute) {
                mCurrentMinute = formatMinute(minuteList.get(wvMinute.getCurrentItem()));
            }

            if (onTimeListener != null) {
                onTimeListener.onTimeChose(mCurrentZone*12 + mCurrentHour, mCurrentMinute);
            }
        }
    };

    public interface OnTimeListener {
        void onTimeChose(int hour, int minute);
    }

    public void setOnTimeListener(OnTimeListener onTimeListener) {
        this.onTimeListener = onTimeListener;
    }

    public String getDate() {
        return mCurrentZone + "-" + mCurrentHour + "-" + mCurrentMinute;
    }

    private String formatZone(int zone) {
        if (zone == 0) {
            return "上午";
        }
        else {
            return "下午";
        }
    }

    private String formatHour(int hour) {
        return hour + "时";
    }

    private String formatMinute(int minute) {
        if (minute < 10) {
            return "0" + minute + "分";
        }
        else {
            return minute + "分";
        }
    }

    private Integer formatZone(String zone) {
        if ("上午".equals(zone)) {
            return 0;
        }
        else {
            return 1;
        }
    }

    private Integer formatHour(String hour) {
        String iHour = hour.substring(0, hour.indexOf("时"));

        return Integer.valueOf(iHour);
    }

    private Integer formatMinute(String minute) {
        String iMinute = minute.substring(0, minute.indexOf("分"));

        return Integer.valueOf(iMinute);
    }
}
