package panteao.make.ready.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.exoplayer2.ui.DefaultTimeBar;

public class NontonTimeBar extends DefaultTimeBar {

    public NontonTimeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
// TODO Auto-generated constructor stub
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);


    }
}