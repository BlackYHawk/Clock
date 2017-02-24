package io.hawk.clock.util;

import android.util.Log;

import io.hawk.clock.BuildConfig;


public class LOG {
	public static final boolean D = BuildConfig.DEBUG;

	public static final void D(String tag, String debug) {
		if (D) {
			Log.d(tag, debug);
		}
	}

	public static final void I(String tag, String debug) {
		if (D) {
			Log.i(tag, debug);
		}
	}

	public static final void W(String tag, String debug) {
		if (D) {
			Log.w(tag, debug);
		}
	}

    public static final void E(String tag, String debug) {
        if (D) {
            Log.e(tag, debug);
        }
    }

	public static final void exec(Exception e) {
		if (D) {
			e.printStackTrace();
		}
	}

}
