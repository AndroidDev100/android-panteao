package panteao.make.ready.utils.helpers.carousel.indicators;


import android.content.Context;

import androidx.core.content.res.ResourcesCompat;

import panteao.make.ready.R;


/**
 * PRATIK RAO
 */

public class CircleIndicator extends IndicatorShape {

    public CircleIndicator(Context context, int indicatorSize, boolean mustAnimateChanges) {
        super(context, indicatorSize, mustAnimateChanges);
        setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indicator_circle_unselected, null));
    }

    @Override
    public void onCheckedChange(boolean isChecked) {
        super.onCheckedChange(isChecked);
        if (isChecked) {
            setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indicator_circle_selected, null));
        } else {
            setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.indicator_circle_unselected, null));
        }
    }
}
