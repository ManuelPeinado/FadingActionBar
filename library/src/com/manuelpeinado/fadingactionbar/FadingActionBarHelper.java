package com.manuelpeinado.fadingactionbar;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListActivity;
import com.cyrilmottier.android.translucentactionbar.NotifyingScrollView;

public class FadingActionBarHelper {
	private Activity mActivity;
    private Drawable mActionBarBackgroundDrawable;
    private FrameLayout mHeaderContainer;
    private int mActionBarBackgroundResId;
    private View mHeaderView;
    private ActionBar mActionBar;
    private boolean mLightActionBar;
    private int mScrollPosition;

    public FadingActionBarHelper activity(Activity activity) {
    	mActivity = activity;
    	return this;
    }
    
    public FadingActionBarHelper actionBarBackground(int drawableResId) {
        mActionBarBackgroundResId = drawableResId;
        return this;
    }

    public FadingActionBarHelper actionBarBackground(Drawable drawable) {
        mActionBarBackgroundDrawable = drawable;
        return this;
    }

    public FadingActionBarHelper headerView(View view) {
        mHeaderView = view;
        return this;
    }

    public FadingActionBarHelper lightActionBar(boolean value) {
        mLightActionBar = value;
        return this;
    }

    public FadingActionBarHelper apply(ListView listView) {
        mHeaderContainer = new FrameLayout(mActivity);
        mHeaderContainer.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        
        View gradientOverlay = new View(mActivity);
        gradientOverlay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int)(100 * mActivity.getResources().getDisplayMetrics().density)));
        mHeaderContainer.addView(mHeaderView, 0);
        
        GradientDrawable gradient = new GradientDrawable(Orientation.TOP_BOTTOM, new int[] {0x77000000, 0x00000000});
        gradientOverlay.setBackgroundDrawable(gradient);
        mHeaderContainer.addView(gradientOverlay);
        
        listView.addHeaderView(mHeaderContainer, null, false);

        listView.setOnScrollListener(mOnScrollListener);
        updateActionBar(mActivity);
        
    	return this;
    }
    
    public FadingActionBarHelper apply(NotifyingScrollView scrollView, ViewGroup container) {
        mHeaderContainer = new FrameLayout(mActivity);
        mHeaderContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        
        View gradientOverlay = new View(mActivity);
        gradientOverlay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int)(100 * mActivity.getResources().getDisplayMetrics().density)));
        mHeaderContainer.addView(mHeaderView, 0);
        
        GradientDrawable gradient = buildGradient();
        gradientOverlay.setBackgroundDrawable(gradient);
        mHeaderContainer.addView(gradientOverlay);
        
        container.addView(mHeaderContainer, 0);
        scrollView.setOnScrollChangedListener(mOnScrollChangedListener);
        updateActionBar(mActivity);
        
    	return this;
    }

    public void updateActionBar(Activity activity) {
        mActionBar = getActionBar(activity);
        if (mActionBarBackgroundDrawable == null) {
            mActionBarBackgroundDrawable = activity.getResources().getDrawable(mActionBarBackgroundResId);
        }
        mActionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
        }
        onNewScroll(mScrollPosition);
    }
    
    private GradientDrawable buildGradient() {
    	int[] colors;
    	
    	if (mLightActionBar) {
    		colors = new int[] {0x99FFFFFF, 0x22FFFFFF, 0x00FFFFFF};
    	} else {
    		colors = new int[] {0x77000000, 0x00000000};
    	}
    	
    	return new GradientDrawable(Orientation.TOP_BOTTOM, colors);
    }

    private ActionBar getActionBar(Activity activity) {
        if (activity instanceof SherlockActivity) {
            return ((SherlockActivity)activity).getSupportActionBar();
        }
        if (activity instanceof SherlockFragmentActivity) {
            return ((SherlockFragmentActivity)activity).getSupportActionBar();
        }
        if (activity instanceof SherlockListActivity) {
            return ((SherlockListActivity)activity).getSupportActionBar();
        }
        throw new RuntimeException("Activity should derive from one of the ActionBarSherlock");
    }

    private Drawable.Callback mDrawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            mActionBar.setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
        }
    };

    private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        	onNewScroll(t);
        }
    };

    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            View topChild = view.getChildAt(0);
            if (topChild == null) {
                onNewScroll(0);
            } else if (topChild != mHeaderContainer) {
                onNewScroll(mHeaderContainer.getHeight());
            } else {
                onNewScroll(-topChild.getTop());
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    };

    private void onNewScroll(int scrollPosition) {
    	mScrollPosition = scrollPosition;
        if (mActionBar == null) {
            return;
        }
        int headerHeight = mHeaderContainer.getHeight() - mActionBar.getHeight();
        float ratio = (float) Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;
        int newAlpha = (int) (ratio * 255);
        mActionBarBackgroundDrawable.setAlpha(newAlpha);
    }
}