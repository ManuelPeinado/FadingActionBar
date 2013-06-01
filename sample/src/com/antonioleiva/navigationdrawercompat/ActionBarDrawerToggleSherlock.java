package com.antonioleiva.navigationdrawercompat;/*
 * Copyright (C) 2010 The Android Open Source Project
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


import java.lang.reflect.Method;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.ActionBar;

/**
 * This class encapsulates some awful hacks.
 * <p/>
 * Before JB-MR2 (API 18) it was not possible to change the home-as-up indicator glyph
 * in an action bar without some really gross hacks. Since the MR2 SDK is not published as of
 * this writing, the new API is accessed via reflection here if available.
 */
class ActionBarDrawerToggleSherlock {
    private static final String TAG = "ActionBarDrawerToggleHoneycomb";

    private static final int[] THEME_ATTRS = new int[]{
            android.R.attr.homeAsUpIndicator,
            R.attr.homeAsUpIndicator
    };

    public static Object setActionBarUpIndicator(Object info, Activity activity,
                                                 Drawable drawable, int contentDescRes) {
        if (info == null) {
            info = new SetIndicatorInfo(activity);
        }
        final SetIndicatorInfo sii = (SetIndicatorInfo) info;
        if (sii.upIndicatorView != null) {
            sii.upIndicatorView.setImageDrawable(drawable);
        } else {
            Log.w(TAG, "Couldn't set home-as-up indicator");
        }
        return info;
    }

    public static Object setActionBarDescription(Object info, Activity activity,
                                                 int contentDescRes) {
        if (info == null) {
            info = new SetIndicatorInfo(activity);
        }
        final SetIndicatorInfo sii = (SetIndicatorInfo) info;
        if (sii.setHomeAsUpIndicator != null) {
            try {
                // TODO With ABS it will never be executed. Clean code.
                //final ActionBar actionBar = activity.getSupportActionBar();
                //sii.setHomeActionContentDescription.invoke(actionBar, contentDescRes);
            } catch (Exception e) {
                Log.w(TAG, "Couldn't set content description via JB-MR2 API", e);
            }
        }
        return info;
    }

    public static Drawable getThemeUpIndicator(Activity activity) {
        final TypedArray a = activity.obtainStyledAttributes(THEME_ATTRS);

        for (int i = 0; i < THEME_ATTRS.length; i++) {
            final Drawable result = a.getDrawable(i);
            if (result != null) {
                a.recycle();
                return result;
            }
        }

        return null;
    }

    private static class SetIndicatorInfo {
        public Method setHomeAsUpIndicator;
        public Method setHomeActionContentDescription;
        public ImageView upIndicatorView;

        SetIndicatorInfo(Activity activity) {
            try {
                setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator",
                        Drawable.class);
                setHomeActionContentDescription = ActionBar.class.getDeclaredMethod(
                        "setHomeActionContentDescription", Integer.TYPE);

                // If we got the method we won't need the stuff below.
                return;
            } catch (NoSuchMethodException e) {
                // Oh well. We'll use the other mechanism below instead.
            }

            int homeRes = android.R.id.home;
            View home = activity.findViewById(homeRes);

            if (home == null) {
                home = activity.findViewById(R.id.abs__home);
                homeRes = R.id.abs__home;
            }
            final ViewGroup parent = (ViewGroup) home.getParent();
            final int childCount = parent.getChildCount();
            if (childCount != 2) {
                // No idea which one will be the right one, an OEM messed with things.
                return;
            }

            final View first = parent.getChildAt(0);
            final View second = parent.getChildAt(1);
            final View up = first.getId() == homeRes ? second : first;


            if ((up != null) && (up instanceof ImageView)) {
                // Jackpot! (Probably...)
                upIndicatorView = (ImageView) up;
            }
        }
    }
}
