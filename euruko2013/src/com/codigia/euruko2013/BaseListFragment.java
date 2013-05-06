package com.codigia.euruko2013;

import com.codigia.euruko2013.R;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.devsmart.android.ui.HorizontalListView;

public abstract class BaseListFragment extends SherlockFragment {

	public static final int animationDuration = 450;

	private ViewGroup mViewGroup;
	private SparseArray<Parcelable> mViewState = null;
	private int mSparseKey;
	private boolean mIsHorizontal;
	private boolean mShowRefresh;
	MenuItem mRefreshMenuItem = null;
	View mRotatingImageView;
	Animation rotationAnimation;
	boolean isRotating;

	public BaseListFragment(boolean showRefresh) {
		mShowRefresh = showRefresh;
	}

	protected boolean isHorizontal() {
		return mIsHorizontal;
	}

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

		mIsHorizontal = getActivity().getResources().getConfiguration().orientation 
				== Configuration.ORIENTATION_LANDSCAPE;

        int resid = mIsHorizontal ? R.layout.horlist : R.layout.list;

    	mViewGroup = (ViewGroup) inflater.inflate(resid, null, false);
    	mViewGroup.setLayoutAnimation(new LayoutAnimationController(set));

		rotationAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.rotate);
		rotationAnimation.setRepeatCount(Animation.INFINITE);

        mRotatingImageView = inflater.inflate(R.layout.refreshing, null);

		return mViewGroup;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		super.onActivityCreated(savedInstanceState);
	}

	public void onRefreshCompleted() {
		if (mRefreshMenuItem != null) {
			View v = mRefreshMenuItem.getActionView();
			if (v != null) {
				v.clearAnimation();
				mRefreshMenuItem.setActionView(null);
			}
		}

		isRotating = false;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if (mShowRefresh) {
			inflater.inflate(R.menu.news_menu, menu);
			mRefreshMenuItem = menu.getItem(0);
		} else {
			mRefreshMenuItem = menu.add(Menu.NONE, 0, Menu.NONE, "");
			mRefreshMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			mRefreshMenuItem.setActionView(R.layout.empty);
		}
	}

	protected void onMenuRefresh() {
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
            	onMenuRefresh();
		    	rotateIcon();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void rotateIcon() {
    	if (isRotating) {
    		return;
    	}

    	isRotating = true;

        mRotatingImageView.startAnimation(rotationAnimation);

        mRefreshMenuItem.setActionView(mRotatingImageView);
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

	protected void setListAdapter(BaseAdapter adapter,
			OnItemClickListener clickListener) {
		if (mViewGroup instanceof ListView) {
			ListView lv = (ListView) mViewGroup;
			lv.setOnItemClickListener(clickListener);
			lv.setAdapter(adapter);
		} else if (mViewGroup instanceof HorizontalListView) {
			HorizontalListView hlv = (HorizontalListView) mViewGroup;
			hlv.setOnItemClickListener(clickListener);
			hlv.setAdapter(adapter);
		}

		onRefreshCompleted();
	}
}