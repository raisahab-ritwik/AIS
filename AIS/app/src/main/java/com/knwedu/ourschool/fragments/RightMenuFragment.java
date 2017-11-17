/* Created by Srikanth gr.
 */

package com.knwedu.ourschool.fragments;


import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.CustomizeWebViewActivity;
import com.knwedu.ourschool.MainActivity;
import com.knwedu.ourschool.adapter.CareerListAdapter;
import com.knwedu.ourschool.anim.AnimationFactory;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.PremiumServiceSubject;
import com.knwedu.ourschool.utils.DataStructureFramwork.PremiumServiceTotal;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.knwedu.ourschool.view.ClassPositionView;
import com.knwedu.ourschool.view.SubjectView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//Right Menu
public class RightMenuFragment extends Fragment {
    private View view;
    private Button viewDetails;
    private ProgressDialog dialog;
    private View chart;
    private LinearLayout subjectView, scoreLineView, rankView1, rankView2, rankView3;
    private ObjectAnimator animation, animation1;
    private ArrayList<PremiumServiceSubject> subjects = new ArrayList<PremiumServiceSubject>();
    private String termName, termNameTotal,studentName;
    private LinearLayout graphBody;
    private RelativeLayout emptyView;
    Timer timer;
    private int flag = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rightmenu_view, container, false);
        graphBody = (LinearLayout)view.findViewById(R.id.graphBody);
        emptyView = (RelativeLayout)view.findViewById(R.id.emptyView);

        graphBody.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onPause() {
        Log.e("RightMenuFragment", "OnPause of HomeFragment");

        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onResume() {
        Log.e("RightMenuFragment", "onResume of HomeFragment");
        super.onResume();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        initialize();
    }

    private void initialize() {
        studentName = SchoolAppUtils.GetSharedParameter(getActivity(),Constants.CHILD_NAME);
        subjectView = (LinearLayout) view.findViewById(R.id.subjectView);
        scoreLineView = (LinearLayout) view.findViewById(R.id.scoreLineView);

        rankView1 = (LinearLayout) view.findViewById(R.id.rankView1);
        rankView2 = (LinearLayout) view.findViewById(R.id.rankView2);
        //rankView3 = (LinearLayout)view.findViewById(R.id.rankView3);

        requetsForSubjects();


//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }


        //ViewFlipper subjectViewFlipper = (ViewFlipper)view.findViewById(R.id.subjectViewflipper);
        //AnimationFactory.flipTransition(subjectViewFlipper, AnimationFactory.FlipDirection.RIGHT_LEFT);

        animation = ObjectAnimator.ofFloat(subjectView, "rotationY", 0.0f, 360f);
        animation.setDuration(2400);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());


        animation1 = ObjectAnimator.ofFloat(scoreLineView, "rotationY", 0.0f, 360f);
        animation1.setDuration(2400);
        animation1.setStartDelay(animation.getDuration());
        animation1.setInterpolator(new AccelerateDecelerateInterpolator());


// 		To run multiple animations
//		Animation animFadeIn,animFadeOut,animRotate;
//		animFadeIn = AnimationUtils.loadAnimation(getActivity(),
//				R.anim.anim_fade_in);
//		animFadeOut = AnimationUtils.loadAnimation(getActivity(),
//				R.anim.anim_fade_out);
//		animRotate = AnimationUtils.loadAnimation(getActivity(),
//				R.anim.anim_rotate);
//		AnimationSet s = new AnimationSet(false);
//		s.addAnimation(animRotate);
//		animRotate.setDuration((long) 3600);
//		animFadeOut.setStartOffset(animFadeIn.getDuration() + animRotate.getDuration());
//		s.addAnimation(animFadeOut);
//		animRotate.setStartOffset(0);
//		s.addAnimation(animRotate);
//		s.setFillAfter(true);
//		scoreLineView.setAnimation(s);


        viewDetails = (Button) view.findViewById(R.id.buttonView);
        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //requetsForPremiumLogin();
                Intent intent = new Intent(getActivity(), CustomizeWebViewActivity.class);
                intent.putExtra("url", Urls.base_url + "premiumgraph/getLogin/" + SchoolAppUtils.GetSharedParameter(getActivity(), Constants.CHILD_ID));
                intent.putExtra("title", "Premium Service");
                startActivity(intent);
            }
        });
    }

    private void requetsForSubjects() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("child_id",
                SchoolAppUtils.GetSharedParameter(getActivity(), Constants.CHILD_ID)));
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
        nameValuePairs.add(new BasicNameValuePair("section_id",
                SchoolAppUtils.GetSharedParameter(getActivity(), Constants.SECTION_ID)));

        new RequestSubjectListAsyntask().execute(nameValuePairs);
    }

    private class RequestSubjectListAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(getActivity());
//            dialog.setTitle(getActivity().getTitle().toString());
//            dialog.setMessage(getResources().getString(R.string.please_wait));
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.setCancelable(false);
//            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            List<NameValuePair> nameValuePairs = params[0];
            // Log parameters:
            Log.d("url extension: ", Urls.api_premium_graph_sujectwise);
            String parameters = "";
            for (NameValuePair nvp : nameValuePairs) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_premium_graph_sujectwise);
            try {
                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        Log.d("result", json.getString("result"));
                        JSONObject dataObj;
                        JSONArray detailsArray;
                        try {
                            dataObj = json.getJSONObject("data");
                            termName = dataObj.getString("termname").trim();
                            detailsArray = dataObj.getJSONArray("details");
                            Log.d("termName", termName);
                        } catch (Exception e) {
                            return true;
                        }

                        ////////////////   ////////////////////////////////////////
                        subjects.clear();
                        for (int i = 0; i < detailsArray.length(); i++) {
                            Log.d("detailsArray", detailsArray.length() + "");
                            JSONObject subjectObj = detailsArray.getJSONObject(i);
                            PremiumServiceSubject subject = new PremiumServiceSubject(subjectObj);
                            subjects.add(subject);
                        }
                        return true;
                    } else {
                        try {
                            error = json.getString("data");
                        } catch (Exception e) {
                        }
                        return false;
                    }
                } else {
                    error = getResources().getString(R.string.unknown_response);
                }
            } catch (
                    JSONException e) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
//            if (dialog != null) {
//                dialog.dismiss();
//                dialog = null;
//            }
            if (result) {
                requetsForTotal();
                if (null != subjects && subjects.size() > 0) {
                    updateSubjectUI(subjects);
                }
                graphBody.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                timer = new Timer();
                MyTimerTask myTimerTask = new MyTimerTask();
                timer.schedule(myTimerTask, 30000, 50000);
            } else {
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                            .getTitle().toString(), error);
                } else {
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                                   .getTitle().toString(),
                            "No Data Found");
                }
                graphBody.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void requetsForTotal() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("child_id",
                SchoolAppUtils.GetSharedParameter(getActivity(), Constants.CHILD_ID)));
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
        nameValuePairs.add(new BasicNameValuePair("section_id",
                SchoolAppUtils.GetSharedParameter(getActivity(), Constants.SECTION_ID)));

        new RequestTotalAsyntask().execute(nameValuePairs);
    }

    private class RequestTotalAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";
        PremiumServiceTotal total;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            List<NameValuePair> nameValuePairs = params[0];
            // Log parameters:
            Log.d("url extension: ", Urls.api_premium_graph_total);
            String parameters = "";
            for (NameValuePair nvp : nameValuePairs) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_premium_graph_total);
            try {
                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        Log.d("result", json.getString("result"));
                        JSONObject dataObj;
                        try {
                            dataObj = json.getJSONObject("data");
                            termNameTotal = dataObj.getString("termname").trim();
                        } catch (Exception e) {
                            return true;
                        }

                        JSONObject totalObj = dataObj.getJSONObject("details");
                        total = new PremiumServiceTotal(totalObj);

                        return true;
                    } else {
                        try {
                            error = json.getString("data");
                        } catch (Exception e) {
                        }
                        return false;
                    }
                } else {
                    error = getResources().getString(R.string.unknown_response);
                }
            } catch (
                    JSONException e) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
            if (result) {
                updateTotalUI(total);
                graphBody.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            } else {
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                            .getTitle().toString(), error);
                } else {
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                                    .getTitle().toString(),"No Data Found");
                }
                graphBody.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            updateUI.sendEmptyMessage(0);
        }

        private Handler updateUI = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                animation.start();
                animation1.start();
                updateSubjectUI(subjects);
            }
        };
    }

    private void updateSubjectUI(ArrayList<PremiumServiceSubject> subjects) {
        if (flag < subjects.size()) {
            flag = flag + 1;
            if(flag == subjects.size()){
                flag = 0;
            }
        } else{
            flag = 0;
        }
        if(subjects.size()>0) {
            SubjectView individualSubject = new SubjectView(getActivity(), Float.parseFloat(subjects.get(flag).highest), Float.parseFloat(subjects.get(flag).mrks_obtain), Float.parseFloat(subjects.get(flag).fullmarks), termName, subjects.get(flag).sub_name, studentName);
            subjectView.removeAllViews();
            subjectView.addView(individualSubject);
            ClassPositionView subjectwisePosition = new ClassPositionView(getActivity(), "Class Position", subjects.get(flag).sub_name, subjects.get(flag).student_rank);
            rankView1.removeAllViews();
            rankView1.addView(subjectwisePosition);
        }
    }

    private void updateTotalUI(PremiumServiceTotal total) {
        SubjectView consolidateScoreline = new SubjectView(getActivity(), Float.parseFloat(total.highest_marks), Float.parseFloat(total.mrks_obtain), Float.parseFloat(total.total_marks), termNameTotal, "Consolidate Score Line", studentName);
        scoreLineView.removeAllViews();
        scoreLineView.addView(consolidateScoreline);

        ClassPositionView overallClassPosition = new ClassPositionView(getActivity(), "Overall Class Position", "", total.rank);
        rankView2.removeAllViews();
        rankView2.addView(overallClassPosition);
    }
}
