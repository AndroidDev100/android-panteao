package panteao.make.ready.utils.helpers.carousel.indicators;

import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;

import panteao.make.ready.R;


/**
 * PRATIK RAO
 */
public abstract class IndicatorShape extends AppCompatImageView {
    public static final int CIRCLE = 0;
    public static final int SQUARE = 1;
    public static final int ROUND_SQUARE = 2;
    public static final int DASH = 3;
    private static final int ANIMATION_DURATION = 150;
    private boolean isChecked = false;
    private int indicatorSize;
    private boolean mustAnimateChange;

    public IndicatorShape(Context context, int indicatorSize, boolean mustAnimateChange) {
        super(context);
        this.indicatorSize = indicatorSize;
        this.mustAnimateChange = mustAnimateChange;
        setup();
    }


    private void setup() {
        if (this.indicatorSize == 0) {
            indicatorSize = (int) getResources().getDimension(R.dimen._8sdp);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(indicatorSize, indicatorSize);
        int margin = getResources().getDimensionPixelSize(R.dimen._5sdp);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.setMargins(margin, 0, margin, 0);
        setLayoutParams(layoutParams);
    }

    public void onCheckedChange(boolean isChecked) {
        if (this.isChecked != isChecked) {
//            if (mustAnimateChange) {
//                if (isChecked) {
//                    scale();
//                } else {
//                    descale();
//                }
//            }else {
//                if (isChecked) {
//                    scale(0);
//                } else {
//                    descale(0);
//                }
//            }
            this.isChecked = isChecked;
        }
    }

    private void scale() {
        scale(ANIMATION_DURATION);
    }

    private void scale(int duration) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.5f, 1f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillEnabled(true);
        startAnimation(scaleAnimation);
    }


    private void descale() {
        descale(ANIMATION_DURATION);
    }

    private void descale(int duration) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 1f, 1.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillEnabled(true);
        startAnimation(scaleAnimation);
    }

    public void setMustAnimateChange(boolean mustAnimateChange) {
        this.mustAnimateChange = mustAnimateChange;
    }


}
