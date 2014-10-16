package com.ricky.encounterassistant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by Ricky on 8/3/2014.
 */
public class CharacterListActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_encounter);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.list_container);

        if (fragment == null) {
            fragment = new CharacterListFragment();
            fm.beginTransaction()
                    .add(R.id.list_container, fragment)
                    .commit();
        }
    }
}
