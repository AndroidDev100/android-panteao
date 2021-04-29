package panteao.make.ready.utils;

import android.content.Context;
import android.widget.Scroller;

public class ViewPagerScrollable extends Scroller {

    private final int mScrollDuration = 700;

    public ViewPagerScrollable(Context context) {
        super(context);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }
}
