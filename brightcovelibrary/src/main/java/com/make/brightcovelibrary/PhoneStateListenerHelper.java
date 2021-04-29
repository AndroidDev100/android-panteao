package com.make.brightcovelibrary;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.make.brightcovelibrary.callBacks.PhoneListenerCallBack;


public class PhoneStateListenerHelper extends PhoneStateListener {

    private static PhoneStateListener mInstance;
    private static PhoneListenerCallBack phoneListenerCallBack;

    private PhoneStateListenerHelper() {
    }

    public static PhoneStateListener getInstance(Context context) {


     //   BaseActivity mActivity = (BaseActivity) context.getActivity();
        phoneListenerCallBack = (PhoneListenerCallBack) context;
        if (mInstance == null)
            mInstance = new PhoneStateListenerHelper();
        return mInstance;
    }

    public static PhoneStateListener getInstance(BrightcovePlayerFragment context) {


        //   BaseActivity mActivity = (BaseActivity) context.getActivity();
        phoneListenerCallBack = (PhoneListenerCallBack) context;
        if (mInstance == null)
            mInstance = new PhoneStateListenerHelper();
        return mInstance;
    }
    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        if (state == TelephonyManager.CALL_STATE_RINGING) {
            phoneListenerCallBack.onCallStateRinging();
        } else if (state == TelephonyManager.CALL_STATE_IDLE) {
            phoneListenerCallBack.onCallStateIdle(state);
        } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
            //phoneListenerCallBack.onCallStateIdle(state);
            //A call is dialing, active or on hold
        }
        super.onCallStateChanged(state, phoneNumber);
    }
}
