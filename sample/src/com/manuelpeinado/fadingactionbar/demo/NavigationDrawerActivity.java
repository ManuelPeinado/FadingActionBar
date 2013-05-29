package com.manuelpeinado.fadingactionbar.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class NavigationDrawerActivity extends SherlockFragmentActivity {

    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();

        if (mFragmentManager.findFragmentById(R.id.fragmentLayout) ==  null){
            ft.add(R.id.fragmentLayout, new SampleFragment());
        }

        ft.commit();
    }
}
