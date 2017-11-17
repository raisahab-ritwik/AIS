package com.knwedu.ourschool.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AisTeacherDailyDiaryDetailActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.SchoolAppUtils;

import java.util.ArrayList;

/**
 * Created by ddasgupta on 29/03/17.
 */

public class AisTeacherDailyDiaryAdapter extends BaseAdapter {

    ViewHolder holder;
    private LayoutInflater inflater;
    Context context;
    private ArrayList<DataStructureFramwork.Assignment> assignments;
    public int[] check;
    private String date;
    private DataStructureFramwork.Subject subject;
    AlertDialog.Builder dialoggg;
    private ProgressDialog dialog;
    private int type_variable_assignment;
    private String type_permission_mark, page;
    private boolean isOnline;


    public AisTeacherDailyDiaryAdapter(Context context,
                                       ArrayList<DataStructureFramwork.Assignment> assignments,
                                       DataStructureFramwork.Subject subject){
        this.context = context;
        this.assignments = assignments;
        this.subject = subject;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        page = "Daily Diary";

    }



    @Override
    public int getCount() {
        if (this.assignments != null) {
            return this.assignments.size();
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
            convertView = inflater.inflate(R.layout.teacher_assignment_items,
                    null);
            holder = new ViewHolder();
            holder.mnLayout = (RelativeLayout) convertView.findViewById(R.id.main_layout);
            holder.dateDisplay = (LinearLayout) convertView
                    .findViewById(R.id.title_layout);
            holder.day = (TextView) convertView.findViewById(R.id.day_txt);
            holder.dayNum = (TextView) convertView
                    .findViewById(R.id.day_num_txt);
            holder.title = (TextView) convertView.findViewById(R.id.title_txt);
            holder.subject = (TextView) convertView
                    .findViewById(R.id.subject_txt);
            holder.publish = (ImageView) convertView
                    .findViewById(R.id.img_publish);
            holder.mark = (ImageView) convertView.findViewById(R.id.img_mark);
            holder.attachment = (ImageView) convertView
                    .findViewById(R.id.img_attachment);
            holder.tick = (ImageView) convertView.findViewById(R.id.img_tick);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (assignments.get(position).check) {
            holder.dateDisplay.setVisibility(View.VISIBLE);
            holder.dayNum.setText(SchoolAppUtils.getDay(assignments
                    .get(position).created_date));
            String year = assignments.get(position).created_date.substring(0, 4);
            String month = assignments.get(position).created_date
                    .substring(5, 7);
            String day = assignments.get(position).created_date.substring(8, 10);
            holder.day.setText(Integer.parseInt(day) + "/"
                    + Integer.parseInt(month) + "/" + year);

        } else {
            holder.dateDisplay.setVisibility(View.GONE);
        }
        SchoolAppUtils.setFont(context, holder.title);
        SchoolAppUtils.setFont(context, holder.subject);
        holder.title.setText(assignments.get(position).title);
        holder.subject.setText(assignments.get(position).sub_name);
        holder.publish.setVisibility(View.GONE);
        holder.mark.setVisibility(View.GONE);
        holder.tick.setVisibility(View.GONE);

        if (!assignments.get(position).file_name.equals("null")
                && !assignments.get(position).attachment.equals("null")) {
            holder.attachment.setVisibility(View.VISIBLE);
        } else {
            holder.attachment.setVisibility(View.GONE);
        }

        holder.mnLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        AisTeacherDailyDiaryDetailActivity.class);
                intent.putExtra(Constants.IS_ONLINE, isOnline);
                intent.putExtra(Constants.PAGE_TITLE, page);
                intent.putExtra(Constants.ASSIGNMENT,
                        assignments.get(position).object.toString());
                intent.putExtra(Constants.SUBJECT, subject.object.toString());
                context.startActivity(intent);

            }});

        return convertView;
    }


    private class ViewHolder {
        LinearLayout dateDisplay;
        TextView title, subject, dayNum, day;
        ImageView publish, mark, attachment, tick;
        RelativeLayout mnLayout;
    }
}
