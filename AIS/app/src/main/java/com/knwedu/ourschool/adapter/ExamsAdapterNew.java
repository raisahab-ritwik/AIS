package com.knwedu.ourschool.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.ViewDummyResult;
import com.knwedu.ourschool.async.GetResult;
import com.knwedu.ourschool.model.ExamList;
import com.knwedu.ourschool.utils.Constants;

import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import java.util.ArrayList;

/**
 * Created by sayak on 19/05/2017.
 */
public class ExamsAdapterNew extends RecyclerView.Adapter<ExamsAdapterNew.ViewHolder> {
    int position;
    private int rowLayout;
    private Activity activity;
    private ArrayList<ExamList> examlist = new ArrayList<ExamList>();


    public ExamsAdapterNew(ArrayList<ExamList> examlist, int rowLayout, Activity activity) {

        this.examlist = examlist;
        this.rowLayout = rowLayout;
        this.activity = activity;
    }

    public void setData(ArrayList<ExamList> list) {
        this.examlist = list;
        notifyDataSetChanged();
    }

    public void clearData() {

        if (examlist != null)
            examlist.clear();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        position = pos;
        holder.text_date.setText(examlist.get(position).getTerm_init_date());
        holder.text_term.setText(examlist.get(position).getName_of_exam());
        holder.text_class_id.setText(examlist.get(position).getClass_name()
                + " " + examlist.get(position).getSection_name());

        Log.d("TYPE_SEPARATOR : ", position + "");
        holder.btn_attch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                new AlertDialog.Builder(activity)
                        .setTitle("Select option")
                        .setPositiveButton("View Document",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        // continue with view
                                        SchoolAppUtils
                                                .viewDocument(
                                                        activity,
                                                        Urls.api_get_doc_exam
                                                                + examlist
                                                                .get(position).getExam_term_id());
                                    }
                                })
                        .setNegativeButton("Download",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {


                                        // download
                                        final DownloadTask downloadTask = new DownloadTask(
                                                activity,
                                                examlist.get(position).getFile_name());
                                        downloadTask.execute(Urls.api_get_doc_exam
                                                + examlist
                                                .get(position).getExam_term_id());


                                    }
                                })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();

            }
        });
        /*if (examlist.get(position).getAttachment().equalsIgnoreCase("Y")) {
            holder.btn_attch.setVisibility(View.VISIBLE);
        } else {
            holder.btn_attch.setVisibility(View.VISIBLE);
        }*/
        if (examlist.get(position).getIs_result_publish().equals("1")
                && (SchoolAppUtils.GetSharedParameter(
                activity, Constants.USERTYPEID)
                .equals(Constants.USERTYPE_PARENT) || SchoolAppUtils
                .GetSharedParameter(activity,
                        Constants.USERTYPEID).equals(
                        Constants.USERTYPE_STUDENT))) {
            holder.btn_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

/*
                            Intent intent = new Intent(arg0.getContext(),
									ExamResultsListActivity.class);
							intent.putExtra("EXAM_TERM_ID",
									examlist.get(position).exam_term_id);
							arg0.getContext().startActivity(intent);*/

							/* Implementing new Result */

                    new AlertDialog.Builder(activity)
                            .setTitle("Select option")
                            .setPositiveButton("View Document",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // continue with view
                                                /* Blocked	SchoolAppUtils
                                                            .viewDocument(
																	activity,
																	Urls.base_url
																			+ Urls.api_report_card_download_mobile
																			+ "/"
																			+ examlist.get(position).exam_term_id
																			+ "/"
																			+ SchoolAppUtils
																			.GetSharedParameter(
																					activity,
																					Constants.UORGANIZATIONID)
																			+ "/"
																			+ SchoolAppUtils
																			.GetSharedParameter(
																					activity,
																					Constants.CHILD_ID));  */
                                            Intent in = new Intent(activity, ViewDummyResult.class);
                                            in.putExtra("term_id", examlist.get(position).getExam_term_id());
                                            activity.startActivity(in);
                                        }
                                    })
                            .setNegativeButton("Download",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // download

													/*final DownloadTask downloadTask = new DownloadTask(activity, "");
													downloadTask.execute(Urls.base_url
															+ Urls.api_report_card_download_mobile
															+ "/"
															+ examlist.get(position).exam_term_id
															+ "/"
															+ SchoolAppUtils
															.GetSharedParameter(
																	activity,
																	Constants.UORGANIZATIONID)
															+ "/"
															+ SchoolAppUtils
															.GetSharedParameter(
																	activity,
																	Constants.CHILD_ID));*/

                                            final GetResult getTask = new GetResult(activity, examlist.get(position).getExam_term_id());
                                            getTask.execute();





									/*
									Intent i = new Intent(Intent.ACTION_VIEW);
									i.setData(Uri.parse(Urls.base_url
											+ Urls.api_report_card_download_mobile
											+ "/"
											+ EXAM_TERM_ID
											+ "/"
											+ SchoolAppUtils
													.GetSharedParameter(
															ExamResultsListActivity.this,
															Constants.UORGANIZATIONID)
											+ "/"
											+ SchoolAppUtils
													.GetSharedParameter(
															ExamResultsListActivity.this,
															Constants.CHILD_ID)));
									startActivity(i);*/

                                        }
                                    }).setIcon(android.R.drawable.ic_dialog_info)
                            .show();






						/* End */

                }
            });

        } else {
            // holder.btn_result.setText("No Result");
            Log.d("hello", "he");
            holder.btn_result.setClickable(false);
            holder.btn_result.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {

        return examlist == null ? 0 : examlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text_date, text_subject_name, text_room_no,
                text_start_time, text_end_time, text_term, text_class_id;
        public ImageView btn_result, btn_attch;

        public ViewHolder(View itemView) {

            super(itemView);
            text_term = (TextView) itemView
                    .findViewById(R.id.txt_header_exam_term);
            text_class_id = (TextView) itemView
                    .findViewById(R.id.txt_header_exam_class);
            text_date = (TextView) itemView
                    .findViewById(R.id.txt_header_exam_date);
            //btn_attch = (ImageView) itemView.findViewById(R.id.attchment_icon);
            btn_result = (ImageView) itemView.findViewById(R.id.btn_result);

        }
    }
}
