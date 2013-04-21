package org.hypest.erk13;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

public abstract class BaseListFragment extends ListFragment {

	public static final int animationDuration = 450;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		AnimationSet set = new AnimationSet(true);
		set.setInterpolator(new DecelerateInterpolator(1.2f));

		Animation animation;
        animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(animationDuration);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.4f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(animationDuration);
        set.addAnimation(animation);

//		animation = new RotateAnimation(
//				-15.0f,
//				0.0f,
//				Animation.RELATIVE_TO_SELF, 1.0f,
//				Animation.RELATIVE_TO_SELF, 0.0f);
//        animation.setDuration(animationDuration);
//        set.addAnimation(animation);

		ListView lv = (ListView) inflater.inflate(R.layout.list, null);
		lv.setLayoutAnimation(new LayoutAnimationController(set));
		return lv;
	}

	public abstract void networkRefresh();
}