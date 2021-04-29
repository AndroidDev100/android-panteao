package panteao.make.ready.utils.helpers;

import android.app.Activity;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.R;


public class RecyclerAnimator {
    final Activity activity;

    public RecyclerAnimator(Activity ctx) {
        this.activity = ctx;
    }

    public void animate(RecyclerView myRecyclerView) {
        int resId = R.anim.layout_animation_fall_down;
        if (activity == null){
            return;
        }
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(activity, resId);
        myRecyclerView.setLayoutAnimation(animation);
    }

    public void fromRightAnimate(RecyclerView myRecyclerView) {
        int resId = R.anim.layout_animation_from_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(activity, resId);
        myRecyclerView.setLayoutAnimation(animation);
    }
}
