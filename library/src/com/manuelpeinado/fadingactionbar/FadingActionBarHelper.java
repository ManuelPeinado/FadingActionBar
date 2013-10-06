/*
 * Copyright (C) 2013 Manuel Peinado
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.manuelpeinado.fadingactionbar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
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
    protected static final String TAG = "FadingActionBarHelper";
    private Drawable mActionBarBackgroundDrawable;
    private FrameLayout mHeaderContainer;
    private int mActionBarBackgroundResId;
    private int mHeaderLayoutResId;
    private View mHeaderView;
    private int mHeaderOverlayLayoutResId;
    private View mHeaderOverlayView;
    private int mContentLayoutResId;
    private View mContentView;
    private ActionBar mActionBar;
    private LayoutInflater mInflater;
    private boolean mLightActionBar;
    private boolean mUseParallax = true;
    private int mLastDampedScroll;
    private int mLastHeaderHeight = -1;
    private ViewGroup mContentContainer;
    private ViewGroup mScrollView;
    private boolean mFirstGlobalLayoutPerformed;
    private FrameLayout mMarginView;
    private View mListViewBackgroundView;


    public FadingActionBarHelper actionBarBackground(int drawableResId) {
        mActionBarBackgroundResId = drawableResId;
        return this;
    }

    public FadingActionBarHelper actionBarBackground(Drawable drawable) {
        mActionBarBackgroundDrawable = drawable;
        return this;
    }

    public FadingActionBarHelper headerLayout(int layoutResId) {
        mHeaderLayoutResId = layoutResId;
        return this;
    }

    public FadingActionBarHelper headerView(View view) {
        mHeaderView = view;
        return this;
    }

    /** 
     * A header overlay is placed on top of the header, and does not scroll with parallax effect. If you disable 
     * parallax, then you shouldn't bother to use the header overlay, just put everything in the header.
     */
    public FadingActionBarHelper headerOverlayLayout(int layoutResId) {
        mHeaderOverlayLayoutResId = layoutResId;
        return this;
    }

    /**
     * See {@link #headerOverlayLayout(int)}.
     */
    public FadingActionBarHelper headerOverlayView(View view) {
        mHeaderOverlayView = view;
        return this;
    }

    public FadingActionBarHelper contentLayout(int layoutResId) {
        mContentLayoutResId = layoutResId;
        return this;
    }

    public FadingActionBarHelper contentView(View view) {
        mContentView = view;
        return this;
    }

    public FadingActionBarHelper lightActionBar(boolean value) {
        mLightActionBar = value;
        return this;
    }

    public FadingActionBarHelper parallax(boolean value) {
        mUseParallax = value;
        return this;
    }

    public View createView(Context context) {
        return createView(LayoutInflater.from(context));
    }

    public View createView(LayoutInflater inflater) {
        //
        // Prepare everything

        mInflater = inflater;
        if (mContentView == null) {
            mContentView = inflater.inflate(mContentLayoutResId, null);
        }
        if (mHeaderView == null) {
            mHeaderView = inflater.inflate(mHeaderLayoutResId, null, false);
        }

        //
        // See if we are in a ListView or ScrollView scenario

        ListView listView = (ListView) mContentView.findViewById(android.R.id.list);
        View root;
        if (listView != null) {
            root = createListView(listView);
        } else {
            root = createScrollView();
        }

        //
        // Add header overlay (a special header that scrolls without parallax) 

        if (mHeaderOverlayView == null && mHeaderOverlayLayoutResId != 0) {
            mHeaderOverlayView = inflater.inflate(mHeaderOverlayLayoutResId, mMarginView, false);
        }
        if (mHeaderOverlayView != null) {
            mMarginView.addView(mHeaderOverlayView);
        }

        // Use measured height here as an estimate of the header height, later on after the layout is complete 
        // we'll use the actual height
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.EXACTLY);
        mHeaderView.measure(widthMeasureSpec, heightMeasureSpec);
        updateHeaderHeight(mHeaderView.getMeasuredHeight());

        root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int headerHeight = mHeaderContainer.getHeight();
                if (!mFirstGlobalLayoutPerformed && headerHeight != 0) {
                    updateHeaderHeight(headerHeight);
                    mFirstGlobalLayoutPerformed = true;
                }
            }
        });
        return root;
    }

    public void initActionBar(Activity activity) {
        mActionBar = getActionBar(activity);
        if (mActionBarBackgroundDrawable == null) {
            mActionBarBackgroundDrawable = activity.getResources().getDrawable(mActionBarBackgroundResId);
        }
        mActionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
        }
        mActionBarBackgroundDrawable.setAlpha(0);
    }

    protected ActionBar getActionBar(Activity activity) {
        if (activity instanceof SherlockActivity) {
            return ((SherlockActivity) activity).getSupportActionBar();
        }
        if (activity instanceof SherlockFragmentActivity) {
            return ((SherlockFragmentActivity) activity).getSupportActionBar();
        }
        if (activity instanceof SherlockListActivity) {
            return ((SherlockListActivity) activity).getSupportActionBar();
        }
        try {
            Method method = activity.getClass().getMethod("getSupportActionBar");
            return (ActionBar) method.invoke(activity);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Activity should derive from one of the ActionBarSherlock activities "
                + "or implement a method called getSupportActionBar");
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

    private View createScrollView() {
        mScrollView = (ViewGroup) mInflater.inflate(R.layout.fab__scrollview_container, null);

        NotifyingScrollView scrollView = (NotifyingScrollView) mScrollView.findViewById(R.id.fab__scroll_view);
        scrollView.setOnScrollChangedListener(mOnScrollChangedListener);

        mContentContainer = (ViewGroup) mScrollView.findViewById(R.id.fab__container);
        mContentContainer.addView(mContentView);
        mHeaderContainer = (FrameLayout) mScrollView.findViewById(R.id.fab__header_container);
        initializeGradient(mHeaderContainer);
        mHeaderContainer.addView(mHeaderView, 0);
        mMarginView = (FrameLayout) mContentContainer.findViewById(R.id.fab__content_top_margin);

        return mScrollView;
    }

    private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            onNewScroll(t);
        }
    };

    private View createListView(ListView listView) {
        mContentContainer = (ViewGroup) mInflater.inflate(R.layout.fab__listview_container, null);
        mContentContainer.addView(mContentView);

        mHeaderContainer = (FrameLayout) mContentContainer.findViewById(R.id.fab__header_container);
        initializeGradient(mHeaderContainer);
        mHeaderContainer.addView(mHeaderView, 0);

        mMarginView = new FrameLayout(listView.getContext());
        mMarginView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 0));
        listView.addHeaderView(mMarginView, null, false);

        // Make the background as high as the screen so that it fills regardless of the amount of scroll. 
        mListViewBackgroundView = mContentContainer.findViewById(R.id.fab__listview_background);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mListViewBackgroundView.getLayoutParams();
        params.height = Utils.getDisplayHeight(listView.getContext());
        mListViewBackgroundView.setLayoutParams(params);

        listView.setOnScrollListener(mOnScrollListener);
        return mContentContainer;
    }

    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            View topChild = view.getChildAt(0);
            if (topChild == null) {
                onNewScroll(0);
            } else if (topChild != mMarginView) {
                onNewScroll(mHeaderContainer.getHeight());
            } else {
                onNewScroll(-topChild.getTop());
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    };
    private int mLastScrollPosition;

    private void onNewScroll(int scrollPosition) {
        if (mActionBar == null) {
            return;
        }

        int currentHeaderHeight = mHeaderContainer.getHeight();
        if (currentHeaderHeight != mLastHeaderHeight) {
            updateHeaderHeight(currentHeaderHeight);
        }

        int headerHeight = currentHeaderHeight - mActionBar.getHeight();
        float ratio = (float) Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;
        int newAlpha = (int) (ratio * 255);
        mActionBarBackgroundDrawable.setAlpha(newAlpha);

        addParallaxEffect(scrollPosition);
    }

    private void addParallaxEffect(int scrollPosition) {
        float damping = mUseParallax ? 0.5f : 1.0f;
        int dampedScroll = (int) (scrollPosition * damping);
        int offset = mLastDampedScroll - dampedScroll;
        mHeaderContainer.offsetTopAndBottom(offset);

        if (mListViewBackgroundView != null) {
            offset = mLastScrollPosition - scrollPosition;
            mListViewBackgroundView.offsetTopAndBottom(offset);
        }

        if (mFirstGlobalLayoutPerformed) {
            mLastScrollPosition = scrollPosition;
            mLastDampedScroll = dampedScroll;
        }
    }

    private void updateHeaderHeight(int headerHeight) {
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) mMarginView.getLayoutParams();
        params.height = headerHeight;
        mMarginView.setLayoutParams(params);
        if (mListViewBackgroundView != null) {
            FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) mListViewBackgroundView.getLayoutParams();
            params2.topMargin = headerHeight;
            mListViewBackgroundView.setLayoutParams(params2);
        }
        mLastHeaderHeight = headerHeight;
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
