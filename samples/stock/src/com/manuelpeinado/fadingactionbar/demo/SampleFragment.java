/*
 * Copyright (C) 2013 Manuel Peinado
 * Copyright (C) 2013 Antonio Leiva
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
package com.manuelpeinado.fadingactionbar.demo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

public class SampleFragment extends Fragment implements NavigationDrawerActivity.ActionBarHolder {
    private FadingActionBarHelper mFadingHelper;
    private Bundle mArguments;

    public static final String ARG_IMAGE_RES = "image_source";
    public static final String ARG_ACTION_BG_RES = "image_action_bs_res";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = mFadingHelper.createView(inflater);

        if (mArguments != null){
            ImageView img = (ImageView) view.findViewById(R.id.image_header);
            img.setImageResource(mArguments.getInt(ARG_IMAGE_RES));
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mArguments = getArguments();
        int actionBarBg = mArguments != null ? mArguments.getInt(ARG_ACTION_BG_RES) : R.drawable.ab_background_light;

        mFadingHelper = new FadingActionBarHelper()
            .actionBarBackground(actionBarBg)
            .headerLayout(R.layout.header_light)
            .contentLayout(R.layout.activity_scrollview)
            .lightActionBar(actionBarBg == R.drawable.ab_background_light);
        mFadingHelper.initActionBar(activity);
    }

    @Override
    public void overrideAlpha(int alpha) {
        if (mFadingHelper != null){
            mFadingHelper.setActionBarAlpha(alpha);
        }
    }

    @Override
    public int getAlpha(){
        if (mFadingHelper != null){
            return mFadingHelper.getActionbarAlpha();
        } else {
            return 0;
        }
    }
}
