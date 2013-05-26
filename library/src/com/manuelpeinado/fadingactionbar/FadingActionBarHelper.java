package com.manuelpeinado.fadingactionbar;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.cyrilmottier.android.translucentactionbar.NotifyingScrollView;

public class FadingActionBarHelper {

    private final SherlockActivity mActivity;
    private ActionBar mActionBar;
    private Drawable mActionBarBackgroundDrawable;
    private FrameLayout mHeaderContainer;

    public FadingActionBarHelper(SherlockActivity owner, int actionBarBackgroundResId) {
        this.mActivity = owner;
        mActionBar = owner.getSupportActionBar();
        mActionBarBackgroundDrawable = mActivity.getResources().getDrawable(actionBarBackgroundResId);
        mActionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        mActionBarBackgroundDrawable.setAlpha(0);
    }

    public void setScrollViewContent(int headerResId, int contentResId) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        mActivity.setContentView(R.layout.scrollview_container);

        NotifyingScrollView scrollView = (NotifyingScrollView) mActivity.findViewById(R.id.scroll_view);
        scrollView.setOnScrollChangedListener(mOnScrollChangedListener);

        ViewGroup container = (ViewGroup) mActivity.findViewById(R.id.container);
        View content = inflater.inflate(contentResId, container, false);
        container.addView(content);
        mHeaderContainer = (FrameLayout) mActivity.findViewById(R.id.header_container);
        View header = inflater.inflate(headerResId, mHeaderContainer, false);
        mHeaderContainer.addView(header, 0);
        
        addDrawableCallback();
    }

    private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            onNewScroll(t);
        }
    };

    public void setListViewContent(int headerResId, int contentResId) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        mActivity.setContentView(contentResId);

        ListView listView = (ListView) mActivity.findViewById(android.R.id.list);
        mHeaderContainer = (FrameLayout) inflater.inflate(R.layout.header_container, null);
        View headerView = inflater.inflate(headerResId, mHeaderContainer, false);
        mHeaderContainer.addView(headerView, 0);
        listView.addHeaderView(mHeaderContainer, null, false);

        listView.setOnScrollListener(mOnScrollListener);
        
        addDrawableCallback();
    }

    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            View topChild = view.getChildAt(0);
            if (topChild == null) {
                onNewScroll(0);
            } else if (topChild != mHeaderContainer) {
                onNewScroll(mHeaderContainer.getMeasuredHeight());
            } else {
                onNewScroll(-topChild.getTop());
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    };

    private void onNewScroll(int scrollPosition) {
        int headerHeight = mHeaderContainer.getMeasuredHeight() - mActionBar.getHeight();
        float ratio = (float) Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;
        int newAlpha = (int) (ratio * 255);
        mActionBarBackgroundDrawable.setAlpha(newAlpha);
    }

    private void addDrawableCallback() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
        }
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
}
