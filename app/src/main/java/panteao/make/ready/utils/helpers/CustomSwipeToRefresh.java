package panteao.make.ready.utils.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.R;


public class CustomSwipeToRefresh extends SwipeRefreshLayout {

    private final int mTouchSlop;
    private float mPrevX;


    public CustomSwipeToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
            setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.white));
            setColorSchemeResources(R.color.navy_blue);
        }else {
            setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.greyTextColor));
            setColorSchemeResources(R.color.moretitlecolor);
        }
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (xDiff > mTouchSlop) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(event);
    }

}