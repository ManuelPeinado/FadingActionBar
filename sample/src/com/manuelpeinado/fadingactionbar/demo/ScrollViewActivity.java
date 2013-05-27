package com.manuelpeinado.fadingactionbar.demo;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.manuelpeinado.fadingactionbar.FadingActionBar;

public class ScrollViewActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new FadingActionBar.ScrollViewHelper()
            .actionBarBackground(R.drawable.ab_background)
            .headerLayout(R.layout.header)
            .contentLayout(R.layout.activity_scrollview)
            .apply(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }
}
