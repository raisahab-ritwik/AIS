package com.knwedu.ourschool.adapter;

import java.util.ArrayList;
import java.util.TreeSet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.ExamResultsListActivity;
import com.knwedu.ourschool.ViewDummyResult;
import com.knwedu.ourschool.async.GetResult;
import com.knwedu.ourschool.model.ExamList;
import com.knwedu.ourschool.utils.Constants;

import com.knwedu.ourschool.utils.DataStructureFramwork.ExamSchedule;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class ExamsAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<ExamList> examlist = new ArrayList<ExamList>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    private ArrayList<ExamSchedule> examschedule = new ArrayList<ExamSchedule>();
    ViewHolder holder;
    private LayoutInflater mInflater;
    private Context context;

    public ExamsAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

    }

    /*public ExamsAdapter(Context context,ArrayList<ExamSchedule> examSchedule) {
        this.context = context;
        this.examschedule = examSchedule;
        mInflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }*/

    public void addItem(ArrayList<ExamSchedule> examSchedule) {
        this.examschedule = examSchedule;
        examlist.clear();
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(ArrayList<ExamList> exams, int size) {
        examlist = exams;
        sectionHeader.add(size);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        int size;
        if (examlist.size() > 0) {
            size = examlist.size();
        } else {
            size = examschedule.size();
        }
        Log.d( " Size : ",size+"");
        return size;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        if (examlist.size() > 0) {
            return examlist.get(position);
        } else {
            return examschedule.get(position);
        }
//         return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d( " position : ", position+"" );
        //ViewHolder holder = null;
        int rowType = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            Log.d("convertview","convertview");
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.item_exams, parent,false);
                    Log.d(examschedule.size() + "Subject name : ", examschedule.get(position).sub_name);
                    holder.text_date = (TextView) convertView
                            .findViewById(R.id.txt_items_exam_dated);
                    holder.text_subject_name = (TextView) convertView
                            .findViewById(R.id.txt_items_exam_subject_name);
                    holder.text_room_no = (TextView) convertView
                            .findViewById(R.id.txt_items_exam_room_no);
                    holder.text_start_time = (TextView) convertView
                            .findViewById(R.id.txt_items_exam_start_time);
                    holder.text_end_time = (TextView) convertView
                            .findViewById(R.id.txt_items_exam_end_time);
                    holder.text_date
                            .setText(examschedule.get(position).schedule_date);
                    holder.text_subject_name
                            .setText(examschedule.get(position).sub_name);
                    holder.text_room_no
                            .setText(examschedule.get(position).facility_no);
                    holder.text_start_time
                            .setText(examschedule.get(position).start_time);
                    holder.text_end_time
                            .setText(examschedule.get(position).end_time);
                    Log.d( " Subject position : ", position + "Subject name :"+ examschedule.get(position).sub_name);
                    break;
                case TYPE_SEPARATOR:
                    Log.d("TYPE_SEPARATOR","TYPE_SEPARATOR");
                    convertView = mInflater.inflate(R.layout.items_exams_header,null);
                    holder.text_term = (TextView) convertView
                            .findViewById(R.id.txt_header_exam_term);
                    holder.text_class_id = (TextView) convertView
                            .findViewById(R.id.txt_header_exam_class);
                    holder.text_date = (TextView) convertView
                            .findViewById(R.id.txt_header_exam_date);
                    holder.btn_attch = (ImageView) convertView
                            .findViewById(R.id.attchment_icon);
                    holder.btn_result = (ImageView) convertView
                            .findViewById(R.id.btn_result);
                   /* Log.d("data",examlist.get(position).term_init_date);
                    Log.d("data",examlist.get(position).name_of_exam);
                    Log.d("data",examlist.get(position).class_name);
                    Log.d("data",examlist.get(position).section_name);*/
                    holder.text_date.setText(examlist.get(position).getTerm_init_date());
                    holder.text_term.setText(examlist.get(position).getName_of_exam());
                    holder.text_class_id.setText(examlist.get(position).getClass_name()
                            + " " + examlist.get(position).getSection_name());

                    Log.d("TYPE_SEPARATOR : ", position + "");
                    holder.btn_attch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub

                            new AlertDialog.Builder(context)
                                    .setTitle("Select option")
                                    .setPositiveButton("View Document",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    // continue with view
                                                    SchoolAppUtils
                                                            .viewDocument(
                                                                    context,
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
                                                            context,
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
                            context, Constants.USERTYPEID)
                            .equals(Constants.USERTYPE_PARENT) || SchoolAppUtils
                            .GetSharedParameter(context,
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

                                new AlertDialog.Builder(context)
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
                                                        Intent in = new Intent(context, ViewDummyResult.class);
                                                        in.putExtra("term_id", examlist.get(position).getExam_term_id());
                                                        context.startActivity(in);
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

                                                        final GetResult getTask = new GetResult(context, examlist.get(position).getExam_term_id());
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
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.d("hooo",convertView.toString());
        return convertView;
    }

    class ViewHolder {
        public TextView text_date, text_subject_name, text_room_no,
                text_start_time, text_end_time, text_term, text_class_id;
        public ImageView btn_result, btn_attch;
    }

}