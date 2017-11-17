package com.knwedu.ourschool.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inmobi.commons.InMobi;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.ExamReportActivity;
import com.knwedu.ourschool.ExamScheduleActivity;
import com.knwedu.ourschool.GraphViewExamActivity;
import com.knwedu.ourschool.adapter.ExamsAdapter;
import com.knwedu.ourschool.adapter.ExamsAdapterNew;
import com.knwedu.ourschool.model.ExamList;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.ExamSchedule;
import com.knwedu.ourschool.utils.DataStructureFramwork.Examreportcard;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExamsListFragment extends Fragment {
    AlertDialog.Builder dialoggg;
    private ListView list;
    private ExamsAdapter adapter;
    private View view;
    private ProgressDialog dialog;
    private TextView textnodata;
    private ArrayList<ExamList> exams;
    OnItemClickListener listener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            Intent intent = new Intent(getActivity(),
                    ExamScheduleActivity.class);

            intent.putExtra(Constants.TEXAMSCHEDULE,
                    exams.get(arg2).object.toString());

            intent.putExtra(Constants.PAGE_TITLE, getActivity().getTitle()
                    .toString());
            getActivity().startActivity(intent);
        }
    };
    private ArrayList<Examreportcard> examreportcard;
    private ArrayList<ExamSchedule> examschedule;
    private LinearLayout headerLayout;
    private Button report;
    private RecyclerView recycler;
    // private Button showMonthWeek;
    private ImageButton graphButton;
    private ExamsAdapterNew examsAdapterNew;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        SchoolAppUtils.loadAppLogo(getActivity(),
                (ImageButton) view.findViewById(R.id.app_logo));
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        examsAdapterNew = new ExamsAdapterNew(exams, R.layout.items_exams_header, getActivity());
        recycler.setLayoutManager((new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(examsAdapterNew);
        initialize();
        return view;
    }

    private void initialize() {
        InMobi.initialize(view.getContext(),
                getResources().getString(R.string.InMobi_Property_Id));
//		IMBanner banner = (IMBanner) view.findViewById(R.id.banner);
//		banner.loadBanner();

        //-----------------for insurance-----------------------
        ImageView adImageView = (ImageView) view.findViewById(R.id.adImageView);

        if (Integer.parseInt(SchoolAppUtils.GetSharedParameter(getActivity(),
                Constants.USERTYPEID)) == 5 && (SchoolAppUtils.GetSharedParameter(getActivity(), Constants.INSURANCE_AVIALABLE).equalsIgnoreCase("1"))) {
            adImageView.setVisibility(View.VISIBLE);

        } else {
            adImageView.setVisibility(View.GONE);
        }
        adImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.AD_URL));
//				browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				browserIntent.setPackage("com.android.chrome");
//				try {
//					startActivity(browserIntent);
//				} catch (ActivityNotFoundException ex) {
//					// Chrome browser presumably not installed so allow user to choose instead
//					browserIntent.setPackage(null);
//					startActivity(browserIntent);
//				}
                startActivity(new Intent(getActivity(),
                        AdvertisementWebViewActivity.class));
            }
        });


        // showMonthWeek = (Button) getActivity().findViewById(
        // R.id.show_monthly_weekly);
        report = (Button) view.findViewById(R.id.report_btn);
        if (SchoolAppUtils.GetSharedParameter(getActivity(),
                Constants.USERTYPEID).equals(Constants.USERTYPE_PARENT)
                || SchoolAppUtils.GetSharedParameter(getActivity(),
                Constants.USERTYPEID)
                .equals(Constants.USERTYPE_STUDENT)) {
            report.setVisibility(View.VISIBLE);
        } else {
            report.setVisibility(View.INVISIBLE);
        }
        report.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        ExamReportActivity.class);

                intent.putExtra(Constants.PAGE_TITLE, getActivity().getTitle()
                        .toString());
                getActivity().startActivity(intent);


            }
        });
        graphButton = (ImageButton) getActivity().findViewById(R.id.graph_btn);
        if (SchoolAppUtils.GetSharedParameter(getActivity(),
                Constants.USERTYPEID).equals(Constants.USERTYPE_PARENT)
                || SchoolAppUtils.GetSharedParameter(getActivity(),
                Constants.USERTYPEID)
                .equals(Constants.USERTYPE_STUDENT)) {
            handleGraphButton();
        }
        list = (ListView) view.findViewById(R.id.listview);
        textnodata=(TextView)view.findViewById(R.id.textnodata);
        if (exams != null) {
            adapter = new ExamsAdapter(getActivity());
            list.setAdapter(adapter);
            for (int i = 0; i < exams.size(); i++) {
                adapter.addSectionHeaderItem(exams, i);
            }
            list.setAdapter(adapter);

        } else {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
                    .GetSharedParameter(getActivity(), Constants.USERID)));
            nameValuePairs.add(new BasicNameValuePair("user_type_id",
                    SchoolAppUtils.GetSharedParameter(getActivity(),
                            Constants.USERTYPEID)));
            nameValuePairs.add(new BasicNameValuePair("organization_id",
                    SchoolAppUtils.GetSharedParameter(getActivity(),
                            Constants.UORGANIZATIONID)));
            nameValuePairs.add(new BasicNameValuePair("child_id",
                    SchoolAppUtils.GetSharedParameter(getActivity(),
                            Constants.CHILD_ID)));

            new GetExamAsyntask().execute(nameValuePairs);
        }

    }

    private void handleGraphButton() {
        // showMonthWeek.setText("");
        // showMonthWeek.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
        // getResources().getDrawable(R.drawable.graph_button_term), null,
        // null);

        // showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(null, null,
        // null, null);
        graphButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(v.getContext(),
                        GraphViewExamActivity.class);
                intent.putExtra("api_url", Urls.api_graph_exam_term);
                startActivity(intent);
            }
        });
        graphButton.setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @SuppressLint("NewApi")
    @Override
    public void onStop() {
        super.onStop();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    private class GetExamAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle(getActivity().getTitle().toString());
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            List<NameValuePair> nameValuePair = params[0];
            // Log parameters:
            Log.d("url extension: ", Urls.api_exam_list);
            String parameters = "";
            for (NameValuePair nvp : nameValuePair) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePair,
                    Urls.api_exam_list);

            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array = json.getJSONArray("exam_list");
                        Log.d("array", array.toString());
                        exams = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            ExamList exam = new ExamList(array.getJSONObject(i));
                            exams.add(exam);
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

            } catch (JSONException e) {

            }
            return false;

        }

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }

            if (result) {

                if (exams.size() > 0) {
                    Log.d("he", "he");
                    //adapter = new ExamsAdapter(getActivity());
                    adapter = new ExamsAdapter(getActivity());
                    list.setAdapter(adapter);
                    Log.d("Exams : ", exams.size() + "");
                    for (int i = 0; i < exams.size(); i++) {
                        adapter.addSectionHeaderItem(exams, i);
                    }


                    examsAdapterNew.setData(exams);
                    examsAdapterNew.notifyDataSetChanged();

                    //adapter.addSectionHeaderItem(exams, exams.size());

//					list.setAdapter(adapter);
                    list.setOnItemClickListener(listener);
                    if (SchoolAppUtils.GetSharedParameter(getActivity(),
                            Constants.USERTYPEID).equals(
                            Constants.USERTYPE_PARENT)
                            || SchoolAppUtils.GetSharedParameter(getActivity(),
                            Constants.USERTYPEID).equals(
                            Constants.USERTYPE_STUDENT)) {
                        graphButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    report.setVisibility(View.GONE);
                    //SchoolAppUtils.showDialog(getActivity(), getActivity()
                            //.getTitle().toString(), "No Exam data found");
                    textnodata.setVisibility(View.VISIBLE);
                    textnodata.setText("No Exam Schedule created yet");
                }

            } else {

                if (error.length() > 0) {
                    //SchoolAppUtils.showDialog(getActivity(), getActivity()
                            //.getTitle().toString(), error);
                    textnodata.setVisibility(View.VISIBLE);
                    textnodata.setText("No Exam Schedule created yet");
                } else {
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                                    .getTitle().toString(),
                            getResources().getString(R.string.error));
                }

            }
        }

    }

    private class GetExamScheduleAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle(getActivity().getTitle().toString());
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            List<NameValuePair> nameValuePair = params[0];
            // Log parameters:
            Log.d("url extension: ", Urls.api_exam_schedule);
            String parameters = "";
            for (NameValuePair nvp : nameValuePair) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePair,
                    Urls.api_exam_schedule);

            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array = json.getJSONArray("data");
                        examschedule = new ArrayList<ExamSchedule>();
                        for (int i = 0; i < array.length(); i++) {
                            ExamSchedule exam = new ExamSchedule(
                                    array.getJSONObject(i));
                            examschedule.add(exam);
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

            } catch (JSONException e) {

            }
            return false;

        }

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }

            if (result) {

                if (examschedule.size() > 0) {
                    adapter = new ExamsAdapter(getActivity());
                    adapter.notifyDataSetChanged();
                    for (int i = 0; i < examschedule.size(); i++) {
                        adapter.addItem(examschedule);
                    }
                    list.setAdapter(adapter);
                    if (SchoolAppUtils.GetSharedParameter(getActivity(),
                            Constants.USERTYPEID).equals(
                            Constants.USERTYPE_PARENT)
                            || SchoolAppUtils.GetSharedParameter(getActivity(),
                            Constants.USERTYPEID).equals(
                            Constants.USERTYPE_STUDENT)) {
                        graphButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                            .getTitle().toString(), "No Exam Schedule found");
                }

                list.setOnItemClickListener(null);
                // showMonthWeek.setVisibility(View.GONE);
            } else {

                if (error.length() > 0) {
                    //SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(), error);
                    textnodata.setVisibility(View.VISIBLE);
                    textnodata.setText("No Exam Schedule created yet");
                } else {
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                                    .getTitle().toString(),
                            getResources().getString(R.string.error));
                }

            }
        }

    }

}
