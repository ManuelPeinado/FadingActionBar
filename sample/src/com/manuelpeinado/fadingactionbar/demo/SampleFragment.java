package com.manuelpeinado.fadingactionbar.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import com.actionbarsherlock.app.SherlockFragment;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

public class SampleFragment extends SherlockFragment {
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
}
