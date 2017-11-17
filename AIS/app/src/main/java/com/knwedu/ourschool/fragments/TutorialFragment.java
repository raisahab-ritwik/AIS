package com.knwedu.ourschool.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.TutorialSlideAdapter;
import com.knwedu.ourschool.model.Product;
import com.knwedu.ourschool.utils.CirclePageIndicator;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.PageIndicator;
import com.knwedu.ourschool.utils.SchoolAppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddasgupta on 28/03/17.
 */

public class TutorialFragment extends Fragment {
    public static final String ARG_ITEM_ID = "home_fragment";
    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;
    private ViewPager mViewPager;
    TextView got_it;
    PageIndicator mIndicator;
    private Runnable animateViewPager;
    private Handler handler;
    FragmentActivity activity;
    boolean stopSliding = false;
    List<Product> products;
    private String appType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        activity = getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        findViewById(view);
        appType = SchoolAppUtils.GetSharedParameter(getActivity(),
                Constants.APP_TYPE);


        mIndicator.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {

                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_UP:
                        // calls when touch release on ViewPager
                        if (products != null && products.size() != 0) {
                            stopSliding = false;
                            runnable(products.size());
                            handler.postDelayed(animateViewPager,
                                    ANIM_VIEWPAGER_DELAY_USER_VIEW);
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // calls when ViewPager touch
                        if (handler != null && stopSliding == false) {
                            stopSliding = true;
                            handler.removeCallbacks(animateViewPager);
                        }
                        break;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onPause() {

        if (handler != null) {
            //Remove callback
            handler.removeCallbacks(animateViewPager);
        }
        super.onPause();
    }


    @Override
    public void onResume() {
        if (products == null) {
            sendRequest();

        } else {
            mViewPager.setAdapter(new TutorialSlideAdapter(activity, products,
                    TutorialFragment.this));
            mIndicator.setViewPager(mViewPager);

            runnable(products.size());
            handler.postDelayed(animateViewPager,
                    ANIM_VIEWPAGER_DELAY);

        }
        super.onResume();
    }


    private void findViewById(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.tu_view_pager);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        got_it = (TextView)view.findViewById(R.id.fal_got_it);

    }


    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (mViewPager.getCurrentItem() == size - 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem(
                                mViewPager.getCurrentItem() + 1, true);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }




    private void sendRequest() {
        Product temp,temp1,temp2,temp3,temp4,temp5;
        products = new ArrayList<Product>();
        //temp = new Product();
        temp1 = new Product();
        temp2 = new Product();
        temp3 = new Product();
        temp4 = new Product();
        temp5 = new Product();
        //temp.setImage_name("one");
        //temp.setId("1");

        if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
            temp1.setImage_name("one_ais");
            temp1.setId("2");
            temp2.setImage_name("two_ais");
            temp2.setId("3");
            temp3.setImage_name("three_ais");
            temp3.setId("4");
        }else{
            temp1.setImage_name("one_aims");
            temp1.setId("2");
            temp2.setImage_name("two_aims");
            temp2.setId("3");
            temp3.setImage_name("three_aims");
            temp3.setId("4");
        }
        /*temp4.setImage_name("five");
        temp4.setId("5");
        temp5.setImage_name("six");
        temp5.setId("6");*/

        //products.add(temp);
        products.add(temp1);
        products.add(temp2);
        products.add(temp3);
       /* products.add(temp4);
        products.add(temp5);*/

        if (products != null && products.size() != 0) {

            mViewPager.setAdapter(new TutorialSlideAdapter(
                    activity, products, TutorialFragment.this));
            mIndicator.setViewPager(mViewPager);

        } else {

        }

    }



    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (products != null) {
                    //imgNameTxt.setText(""
                    //);
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
