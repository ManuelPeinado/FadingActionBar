package com.manuelpeinado.fadingactionbar.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.cyrilmottier.android.translucentactionbar.NotifyingScrollView;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

public class SampleFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.activity_scrollview, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onAttach(getActivity());
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header_light, null);
        
        new FadingActionBarHelper()
        	.activity(getActivity())
            .actionBarBackground(R.drawable.ab_background_light)
            .headerView(header)
            .lightActionBar(true)
            .apply((NotifyingScrollView)getView().findViewById(R.id.scroller), (ViewGroup)getView().findViewById(R.id.container));
    }
}
