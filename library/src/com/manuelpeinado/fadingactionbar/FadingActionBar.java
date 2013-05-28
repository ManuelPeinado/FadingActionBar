package com.manuelpeinado.fadingactionbar;

import android.content.Context;
import android.content.res.TypedArray;
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

    public static class Initializer {
        private Drawable mActionBarBackgroundDrawable;
        private FrameLayout mHeaderContainer;
        private int mActionBarBackgroundResId;
        private int mHeaderLayoutResId;
        private View mHeaderView;
        private int mContentLayoutResId;
        private View mContentView;
        private ActionBar mActionBar;
        private LayoutInflater mInflater;
        private boolean mLightActionBar;

        public Initializer actionBarBackground(int drawableResId) {
            mActionBarBackgroundResId = drawableResId;
            return this;
        }

        public Initializer actionBarBackground(Drawable drawable) {
            mActionBarBackgroundDrawable = drawable;
            return this;
        }

        public Initializer headerLayout(int layoutResId) {
            mHeaderLayoutResId = layoutResId;
            return this;
        }

        public Initializer headerView(View view) {
            mHeaderView = view;
            return this;
        }

        public Initializer contentLayout(int layoutResId) {
            mContentLayoutResId = layoutResId;
            return this;
        }

        public Initializer contentView(View view) {
            mContentView = view;
            return this;
        }

        public void apply(SherlockActivity activity) {
            //
            // Prepare everything

            parseAttrs(activity);

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

            //
            // See if we are in a ListView or ScrollView scenario

            ListView listView = (ListView) mContentView.findViewById(android.R.id.list);
            if (listView != null) {
                applyListView(activity, listView);
            } else {
                applyScrollView(activity);
            }
        }

        private void parseAttrs(Context context) {
            // Get defaults
            boolean lightActionBarDefault = context.getResources().getBoolean(R.bool.fab__default_light_action_bar);

            // Extract theme attributes
            TypedArray ta = context.obtainStyledAttributes(null, R.styleable.FadingActionBar, R.attr.fadingActionBarStyle,
                    0);
            mLightActionBar = ta.getBoolean(R.styleable.FadingActionBar_fabLightActionBar, lightActionBarDefault);
            ta.recycle();
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

        private void applyScrollView(SherlockActivity activity) {
            activity.setContentView(R.layout.fab__scrollview_container);

            NotifyingScrollView scrollView = (NotifyingScrollView) activity.findViewById(R.id.fab__scroll_view);
            scrollView.setOnScrollChangedListener(mOnScrollChangedListener);

            ViewGroup container = (ViewGroup) activity.findViewById(R.id.fab__container);
            container.addView(mContentView);
            mHeaderContainer = (FrameLayout) activity.findViewById(R.id.fab__header_container);
            initializeGradient(mHeaderContainer);
            mHeaderContainer.addView(mHeaderView, 0);
        }

        private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
            public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
                onNewScroll(t);
            }
        };

        private void applyListView(SherlockActivity activity, ListView listView) {
            activity.setContentView(mContentView);

            mHeaderContainer = (FrameLayout) mInflater.inflate(R.layout.fab__header_container, null);
            initializeGradient(mHeaderContainer);
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

        private void onNewScroll(int scrollPosition) {
            int headerHeight = mHeaderContainer.getMeasuredHeight() - mActionBar.getHeight();
            float ratio = (float) Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;
            int newAlpha = (int) (ratio * 255);
            mActionBarBackgroundDrawable.setAlpha(newAlpha);
        }

        private void initializeGradient(ViewGroup headerContainer) {
            View gradientView = headerContainer.findViewById(R.id.fab__gradient);
            int gradient = R.drawable.fab__gradient;
            if (mLightActionBar) {
                gradient = R.drawable.fab__gradient_light;
            }
            gradientView.setBackgroundResource(gradient);
        }
    }
}
