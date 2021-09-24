package panteao.make.ready.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import panteao.make.ready.R;

public class CustomProgressBarWhite extends ProgressBar {
    public CustomProgressBarWhite(Context context) {
        super(context);
        this.setIndeterminate(true);
        this.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
    }

    public CustomProgressBarWhite(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
    }

    public CustomProgressBarWhite(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
