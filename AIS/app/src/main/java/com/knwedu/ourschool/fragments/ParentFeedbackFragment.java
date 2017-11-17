package com.knwedu.ourschool.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.CreateParentFeedbackListActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.ParentFeedbackList;
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

public class ParentFeedbackFragment extends Fragment {
    private View view;
    private ProgressDialog dialog;
    private ArrayList<ParentFeedbackList> feedbacks = new ArrayList<ParentFeedbackList>();
    private ListView listviewFeedbackList;
    private ParentFeedbackListAdapter adapter;
    private ImageView btnViewCreateFeedback;
    private TextView textViewEmpty;
    private int type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.parent_feedback_fragment_view, container, false);
        initialize();

        return view;
    }

    private void initialize() {
        // TODO Auto-generated method stub
        listviewFeedbackList = (ListView) view.findViewById(R.id.listviewFeedbackList);
        btnViewCreateFeedback = (ImageView) view.findViewById(R.id.btnViewCreateFeedback);
        textViewEmpty = (TextView) view.findViewById(R.id.textViewEmpty);
        btnViewCreateFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateParentFeedbackListActivity.class);
                intent.putExtra(Constants.PAGE_TITLE, "Feedback");
                startActivity(intent);

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * Fetching Parent Feedbacks
         */
        retrieveParentFeedbacks();
    }

    private void retrieveParentFeedbacks() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
        nameValuePairs.add(new BasicNameValuePair("user_id",
                SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERID)));
        nameValuePairs.add(new BasicNameValuePair("user_type_id",
                SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERTYPEID)));
        nameValuePairs.add(new BasicNameValuePair("child_id",
                SchoolAppUtils.GetSharedParameter(getActivity(), Constants.CHILD_ID)));


        new RequestFeedbackListAsyntask().execute(nameValuePairs);
    }

    ;

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

    private class RequestFeedbackListAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
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
            List<NameValuePair> nameValuePairs = params[0];
            // Log parameters:
            Log.d("url extension: ", Urls.api_get_parent_feedback);
            String parameters = "";
            for (NameValuePair nvp : nameValuePairs) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);

            feedbacks.clear();
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_get_parent_feedback);
            try {
                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array = json.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            ParentFeedbackList feedback = new ParentFeedbackList(array.getJSONObject(i));
                            feedbacks.add(feedback);
                        }

                        return true;
                    } else {
                        try {
                            feedbacks.clear();
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
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            if (result) {
                if (feedbacks != null) {
                    adapter = new ParentFeedbackListAdapter(getActivity(), feedbacks);
                    Log.d("Feedback Size : ",feedbacks.size()+"ADAPTER"+adapter.getCount());
                    if (feedbacks.size() > 0) {
                        textViewEmpty.setVisibility(View.GONE);
                        listviewFeedbackList.setVisibility(View.VISIBLE);
                        listviewFeedbackList.setAdapter(adapter);
                    } else {
                        textViewEmpty.setVisibility(View.VISIBLE);
                        listviewFeedbackList.setVisibility(View.GONE);
                        listviewFeedbackList.setAdapter(adapter);
                    }

                }
            } else {
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                            .getTitle().toString(), error);
                    listviewFeedbackList.setVisibility(View.GONE);
                    listviewFeedbackList.setAdapter(adapter);
                } else {
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                                    .getTitle().toString(),
                            getResources().getString(R.string.error));
                }

            }
        }

    }

    public class ParentFeedbackListAdapter extends BaseAdapter {
        ViewHolder holder;
        private LayoutInflater inflater;
        Context context;
        private ArrayList<ParentFeedbackList> feedbackList;
        private ProgressDialog dialog;

        public ParentFeedbackListAdapter(Context context, ArrayList<ParentFeedbackList> feedbacks) {
            this.context = context;
            this.feedbackList = feedbacks;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (this.feedbackList != null) {
                return this.feedbackList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.parent_feedback_list_item_view, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.textViewName);
                holder.rating = (TextView) convertView.findViewById(R.id.textViewRating);
                holder.comment = (TextView) convertView.findViewById(R.id.textViewComment);
                holder.imageViewDelete = (ImageView) convertView.findViewById(R.id.imageViewDelete);
                holder.rb = (RatingBar) convertView.findViewById(R.id.ratingBar);
                holder.rb.setClickable(false);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SchoolAppUtils.setFont(context, holder.name);
            SchoolAppUtils.setFont(context, holder.rating);
            SchoolAppUtils.setFont(context, holder.comment);

            RelativeLayout layout_comment = (RelativeLayout) convertView.findViewById(R.id.layout_created);
            holder.name.setText(feedbackList.get(position).fname + " " + feedbackList.get(position).mname + " " + feedbackList.get(position).lname);
            holder.rating.setText(feedbackList.get(position).star);

            holder.rb.setNumStars(Integer.parseInt(feedbackList.get(position).star));
            if (null != feedbackList.get(position).comments) {
                if (feedbackList.get(position).comments.isEmpty() || feedbackList.get(position).comments.equalsIgnoreCase("")) {
                    layout_comment.setVisibility(View.GONE);
                } else {
                    holder.comment.setText(feedbackList.get(position).comments);
                    layout_comment.setVisibility(View.VISIBLE);
                }

                holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("position",position+"");
                        showDialog(position);

                    }
                });
            }

            return convertView;
        }

        private class ViewHolder {
            TextView rating, name, comment;
            ImageView imageViewDelete;
            RatingBar rb;
        }

        private void showDialog(final int position) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set title
            //alertDialogBuilder.setTitle("Give Reject Reason");

            // set dialog message
            alertDialogBuilder.setCancelable(false);

            View view = inflater.inflate(R.layout.parent_feedback_dialog, null);
            Button btnSubmit = (Button) view.findViewById(R.id.btn_submit);
            Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

            alertDialogBuilder.setView(view);

            // create alert dialog
            final AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

            btnSubmit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    loadData(position);
                    notifyDataSetChanged();
                    alertDialog.cancel();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    alertDialog.cancel();
                }
            });
        }

        private void loadData(int position) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("organization_id",
                    SchoolAppUtils.GetSharedParameter(
                            (context.getApplicationContext()),
                            Constants.UORGANIZATIONID)));
            nameValuePairs.add(new BasicNameValuePair("feedback_id", feedbackList.get(position).feedback_id));

            new SendDeleteRequestAsync().execute(nameValuePairs);
        }

        private class SendDeleteRequestAsync extends
                AsyncTask<List<NameValuePair>, Void, Boolean> {
            String error = "";
            String success = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(context);
                dialog.setTitle("Loading ");
                dialog.setMessage(context.getString(R.string.please_wait));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected Boolean doInBackground(List<NameValuePair>... params) {
                List<NameValuePair> nameValuePairs = params[0];

                // Log parameters:
                Log.d("url extension", Urls.api_delete_parent_feedback);
                String parameters = "";
                for (NameValuePair nvp : nameValuePairs) {
                    parameters += nvp.getName() + "=" + nvp.getValue() + ",";
                }
                Log.d("Parameters: ", parameters);

                JsonParser jParser = new JsonParser();
                JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
                        Urls.api_delete_parent_feedback);

                try {

                    if (json != null) {
                        if (json.getString("result").equalsIgnoreCase("1")) {
                            success = json.getString("data");
                            return true;
                        } else {
                            try {
                                error = json.getString("data");
                            } catch (Exception e) {
                            }
                            return false;
                        }
                    } else {
                        error = context.getString(R.string.unknown_response);
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
                    Toast.makeText(context, success, Toast.LENGTH_LONG).show();
                    retrieveParentFeedbacks();
                } else {
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
