package com.knwedu.ourschool;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.fragments.TutorialFragment;

/**
 * Created by ddasgupta on 1/31/2017.
 */

public class TutorialActivity extends AppCompatActivity {
    private Fragment contentFragment;
    TutorialFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tutorial);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {

            homeFragment = (TutorialFragment) fragmentManager
                    .findFragmentByTag(TutorialFragment.ARG_ITEM_ID);
            contentFragment = homeFragment;

        } else {
            homeFragment = new TutorialFragment();
            switchContent(homeFragment, TutorialFragment.ARG_ITEM_ID);

        }




    }

    public void switchContent(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.popBackStackImmediate())
            ;

        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            transaction.replace(R.id.content_frame, fragment, tag);
            // Only ProductDetailFragment is added to the back stack.
            if (!(fragment instanceof TutorialFragment)) {
                transaction.addToBackStack(tag);
            }
            transaction.commit();
            contentFragment = fragment;
        }
    }

}
