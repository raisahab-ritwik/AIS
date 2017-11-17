package com.knwedu.ourschool.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.SigninActivity;
import com.knwedu.ourschool.fragments.TutorialFragment;
import com.knwedu.ourschool.model.Product;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

import java.util.List;

/**
 * Created by ddasgupta on 28/03/17.
 */

public class TutorialSlideAdapter extends PagerAdapter {
    FragmentActivity activity;
    List<Product> products;
    TutorialFragment homeFragment;

    public TutorialSlideAdapter(FragmentActivity activity, List<Product> products,
                                TutorialFragment homeFragment) {
        this.activity = activity;
        this.homeFragment = homeFragment;
        this.products = products;
    }


    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tutorial_vp_image, container, false);
        ImageView mImageView = (ImageView) view
                .findViewById(R.id.image_display);
        TextView got_it = (TextView) view.findViewById(R.id.got_it);


        int image = activity.getResources().getIdentifier( products.get(position).getImage_name(), "drawable",activity.getPackageName());
        mImageView.setImageResource(image);
        if(products.get(position).getId().equals("4") ){
            got_it.setVisibility(View.VISIBLE);
        }else{
            got_it.setVisibility(View.GONE);
        }
        got_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SchoolAppUtils.SetSharedParameter(
                        activity,
                        Constants.TUTORIAL_COUNTER,
                        "1");
                ((Activity)activity).finish();
               /* YourAdmissionsUtils.SetSharedBoolParameter(activity, Constants.TUTORIAL_STATUS,
                       true);
                Intent in_main = new Intent(activity, HomeActivity.class);
                activity.startActivity(in_main);*/
            }
        });


        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
