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

public class FadingActionBar {

    private static class HelperBase {
        protected Drawable mActionBarBackgroundDrawable;
        protected FrameLayout mHeaderContainer;
        protected int mActionBarBackgroundResId;
        protected int mHeaderLayoutResId;
        protected View mHeaderView;
        protected int mContentLayoutResId;
        protected View mContentView;
        protected ActionBar mActionBar;
        protected LayoutInflater mInflater;

        protected void apply(SherlockActivity activity) {
            mInflater = LayoutInflater.from(activity);
            mActionBar = activity.getSupportActionBar();
            if (mActionBarBackgroundDrawable == null) {
                mActionBarBackgroundDrawable = activity.getResources().getDrawable(mActionBarBackgroundResId);
            }
            mActionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
            }
            mActionBarBackgroundDrawable.setAlpha(0);

            LayoutInflater inflater = LayoutInflater.from(activity);
            if (mContentView == null) {
                mContentView = inflater.inflate(mContentLayoutResId, null);
            }
            if (mHeaderView == null) {
                mHeaderView = inflater.inflate(mHeaderLayoutResId, mHeaderContainer, false);
            }

        }

        protected void onNewScroll(int scrollPosition) {
            int headerHeight = mHeaderContainer.getMeasuredHeight() - mActionBar.getHeight();
            float ratio = (float) Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;
            int newAlpha = (int) (ratio * 255);
            mActionBarBackgroundDrawable.setAlpha(newAlpha);
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

    public static class ScrollViewHelper extends HelperBase {
        public ScrollViewHelper actionBarBackground(int drawableResId) {
            mActionBarBackgroundResId = drawableResId;
            return this;
        }

        public ScrollViewHelper actionBarBackground(Drawable drawable) {
            mActionBarBackgroundDrawable = drawable;
            return this;
        }

        public ScrollViewHelper headerLayout(int layoutResId) {
            mHeaderLayoutResId = layoutResId;
            return this;
        }

        public ScrollViewHelper headerView(View view) {
            mHeaderView = view;
            return this;
        }

        public ScrollViewHelper contentLayout(int layoutResId) {
            mContentLayoutResId = layoutResId;
            return this;
        }

        public ScrollViewHelper contentView(View view) {
            mContentView = view;
            return this;
        }

        public void apply(SherlockActivity activity) {
            super.apply(activity);
            activity.setContentView(R.layout.scrollview_container);

            NotifyingScrollView scrollView = (NotifyingScrollView) activity.findViewById(R.id.scroll_view);
            scrollView.setOnScrollChangedListener(mOnScrollChangedListener);

            ViewGroup container = (ViewGroup) activity.findViewById(R.id.container);
            container.addView(mContentView);
            mHeaderContainer = (FrameLayout) activity.findViewById(R.id.header_container);
            mHeaderContainer.addView(mHeaderView, 0);
        }

        private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
            public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
                onNewScroll(t);
            }
        };
    }

    public static class ListViewHelper extends HelperBase {
        public ListViewHelper actionBarBackground(int drawableResId) {
            mActionBarBackgroundResId = drawableResId;
            return this;
        }

        public ListViewHelper actionBarBackground(Drawable drawable) {
            mActionBarBackgroundDrawable = drawable;
            return this;
        }

        public ListViewHelper headerLayout(int layoutResId) {
            mHeaderLayoutResId = layoutResId;
            return this;
        }

        public ListViewHelper headerView(View view) {
            mHeaderView = view;
            return this;
        }

        public ListViewHelper contentLayout(int layoutResId) {
            mContentLayoutResId = layoutResId;
            return this;
        }

        public ListViewHelper contentView(View view) {
            mContentView = view;
            return this;
        }

        public void apply(SherlockActivity activity) {
            super.apply(activity);
            activity.setContentView(mContentView);

            ListView listView = (ListView) activity.findViewById(android.R.id.list);
            mHeaderContainer = (FrameLayout) mInflater.inflate(R.layout.header_container, null);
            mHeaderContainer.addView(mHeaderView, 0);
            listView.addHeaderView(mHeaderContainer, null, false);

            listView.setOnScrollListener(mOnScrollListener);
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
    }
}
