package com.knwedu.ourschool.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.ShowAttachmentActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.AisSyllabusData;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import java.util.ArrayList;

/**
 * Created by ddasgupta on 2/2/2017.
 */

public class AisSyllabusAdapter extends BaseAdapter {
    SylViewHolder holder;
    private LayoutInflater inflater;
    Context context;
    private ArrayList<AisSyllabusData> mListSyllabus;
    int type;
    //private ImageView syl_attachment;
    private String downloadUrl;


    public AisSyllabusAdapter(Context context, ArrayList<AisSyllabusData> lstSyl, int type){
        this.context = context;
        mListSyllabus = lstSyl;
        this.type = type;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        if(this.mListSyllabus != null)
        {
            return this.mListSyllabus.size();
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

        if(convertView == null){
            Log.d("adapter","adapter");
            convertView = inflater.inflate(R.layout.ais_syllabus_items, parent, false);
            holder = new SylViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.syl_title);
            holder.sclass = (TextView) convertView.findViewById(R.id.syl_class);
            holder.ssection = (TextView) convertView.findViewById(R.id.syl_section);
            holder.attachment = (ImageView) convertView.findViewById(R.id.syl_attachment);
            //syl_attachment= (ImageView) convertView.findViewById(R.id.syl_attachment);
            //syl_attachment.setVisibility(View.GONE);
            convertView.setTag(holder);
        }else{
            holder = (SylViewHolder) convertView.getTag();
        }

        holder.title.setText(mListSyllabus.get(position).title);
        holder.sclass.setText(mListSyllabus.get(position).class_name);
        holder.ssection.setText(mListSyllabus.get(position).section_name);

        holder.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(
                        context)
                        .setTitle("Select option")
                        .setPositiveButton("View Document",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        // continue with view
                                        if(type == 1){
                                            downloadUrl = SchoolAppUtils.GetSharedParameter(context,
                                                    Constants.COMMON_URL ) + Urls.api_ais_syllabus_downlaod + "/" + mListSyllabus.get(position).id;
                                        }else{
                                            downloadUrl = SchoolAppUtils.GetSharedParameter(context,
                                                    Constants.COMMON_URL ) + Urls.api_ais_exam_download + "/" + mListSyllabus.get(position).id;
                                        }


                                        SchoolAppUtils
                                                .imagePdfViewDocument(
                                                        context,
                                                        downloadUrl,mListSyllabus.get(position).file_name);
                                    }
                                })
                        .setNegativeButton("Download",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        // download
                                        final DownloadTask downloadTask = new DownloadTask(context, mListSyllabus.get(position).file_name);
                                        downloadTask.execute(Urls.api_get_doc + mListSyllabus.get(position).id);

                                    }
                                })
                        .setIcon(android.R.drawable.ic_dialog_info).show();

                Intent my_intent = new Intent(context, ShowAttachmentActivity.class);
                my_intent.putExtra("id",mListSyllabus.get(position).id);
                my_intent.putExtra("file_name",mListSyllabus.get(position).file_name);
                my_intent.putExtra("type",Integer.toString(type));
                context.startActivity(my_intent);



            }
        });



        return convertView;
    }





    private class SylViewHolder
    {
        TextView title, sclass, ssection;
        ImageView attachment;

    }
}
