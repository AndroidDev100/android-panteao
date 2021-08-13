package panteao.make.ready.utils.helpers;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import panteao.make.ready.baseModels.BaseActivity;
import panteao.make.ready.callbacks.commonCallbacks.PhoneListenerCallBack;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;


public class PhoneStateListenerHelper extends PhoneStateListener {

    private static PhoneStateListener mInstance;
    private static PhoneListenerCallBack phoneListenerCallBack;

    private PhoneStateListenerHelper() {
    }

    public static PhoneStateListener getInstance(Fragment context) {


        BaseActivity mActivity = (BaseActivity) context.getActivity();
        phoneListenerCallBack = (PhoneListenerCallBack) context;
        if (mInstance == null)
            mInstance = new PhoneStateListenerHelper();
        return mInstance;
    }

    public static PhoneStateListener getInstance(Activity context) {


        //BaseActivity mActivity = (BaseActivity) context;
        phoneListenerCallBack = (PhoneListenerCallBack) context;
        if (mInstance == null)
            mInstance = new PhoneStateListenerHelper();
        return mInstance;
    }

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        PrintLogging.printLog(" ","claaingState"+state);
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
