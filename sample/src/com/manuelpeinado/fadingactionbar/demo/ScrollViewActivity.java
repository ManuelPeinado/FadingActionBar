package com.manuelpeinado.fadingactionbar.demo;

import android.os.Bundle;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.cyrilmottier.android.translucentactionbar.NotifyingScrollView;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

public class ScrollViewActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        
        new FadingActionBarHelper()
            .actionBarBackground(R.drawable.ab_background)
            .headerView(getLayoutInflater().inflate(R.layout.header, null))
            .activity(this)
            .apply((NotifyingScrollView)findViewById(R.id.scroller), (ViewGroup)findViewById(R.id.container));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }
}
