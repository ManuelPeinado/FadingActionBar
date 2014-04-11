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
package com.manuelpeinado.fadingactionbar.extras.actionbarsherlock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListActivity;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelperBase;

public final class FadingActionBarHelper extends FadingActionBarHelperBase {

    private ActionBar mActionBar;

    @Override
    public void initActionBar(Activity activity) {
        mActionBar = getActionBar(activity);
        super.initActionBar(activity);
    }

    private ActionBar getActionBar(Activity activity) {
        if (activity instanceof SherlockActivity) {
            return ((SherlockActivity) activity).getSupportActionBar();
        }
        if (activity instanceof SherlockFragmentActivity) {
            return ((SherlockFragmentActivity) activity).getSupportActionBar();
        }
        if (activity instanceof SherlockListActivity) {
            return ((SherlockListActivity) activity).getSupportActionBar();
        }
        ActionBar actionBar = getActionBarWithReflection(activity, "getSupportActionBar");
        if (actionBar == null) {
            throw new RuntimeException("Activity should derive from one of the ActionBarSherlock activities "
                + "or implement a method called getSupportActionBar");
        }
        return actionBar;
    }

    @Override
    protected int getActionBarHeight() {
        return mActionBar.getHeight();
    }

    @Override
    protected boolean isActionBarNull() {
        return mActionBar == null;
    }

    @Override
    protected void setActionBarBackgroundDrawable(Drawable drawable) {
        mActionBar.setBackgroundDrawable(drawable);
    }
}
