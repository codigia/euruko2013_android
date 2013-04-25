package org.hypest.erk13;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.devsmart.android.ui.HorizontalListView;

public abstract class BaseListFragment extends SherlockFragment {

	public static final int animationDuration = 450;

	private ViewGroup mViewGroup;
	private SparseArray<Parcelable> mViewState = null;
	private int mSparseKey;

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

    	mViewGroup = (ViewGroup) inflater.inflate(R.layout.list, null, false);
    	mViewGroup.setLayoutAnimation(new LayoutAnimationController(set));

		return mViewGroup;
	}

	@Override
	public void onPause() {
		super.onPause();

        if (mViewGroup != null && mViewGroup instanceof HorizontalListView) {
    		mViewState = new SparseArray<Parcelable>();
            mViewGroup.saveHierarchyState(mViewState);
    		Bundle b = new Bundle();
    		((HorizontalListView) mViewGroup).saveState(b);
    		mSparseKey = mViewState.size();
    		mViewState.append(mViewState.size(), b);
        }
	}

	@Override
	public void onResume() {
        if (mViewState != null) {
        	mViewGroup.restoreHierarchyState(mViewState);
            if (mViewGroup instanceof HorizontalListView) {
				((HorizontalListView) mViewGroup)
						.restoreState((Bundle) mViewState.get(mSparseKey));
            }
        	mViewState = null;
        }

		super.onResume();
	}

	public abstract void networkRefresh();

	protected void setListAdapter(BaseAdapter adapter) {
		if (mViewGroup instanceof ListView) {
			((ListView) mViewGroup).setAdapter(adapter);
		} else if (mViewGroup instanceof HorizontalListView) {
			((HorizontalListView) mViewGroup).setAdapter(adapter);
		}
	}
}