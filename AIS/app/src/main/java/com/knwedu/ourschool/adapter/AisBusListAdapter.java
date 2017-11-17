package com.knwedu.ourschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AisSubscriptionActivity;
import com.knwedu.ourschool.model.ListBusData;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ddasgupta on 2/27/2017.
 */

public class AisBusListAdapter extends BaseAdapter {
    BusListViewHolder holder;
    private LayoutInflater inflater;
    Context context;
    private ArrayList<ListBusData> mListbus;

    public AisBusListAdapter(Context context, ArrayList<ListBusData> lstbus){
        this.context = context;
        mListbus = lstbus;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        if(this.mListbus != null)
        {
            return this.mListbus.size();
            //return 2;
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
            convertView = inflater.inflate(R.layout.item_list_bus, parent,false);
            holder = new BusListViewHolder();

            holder.route_no = (TextView) convertView.findViewById(R.id.rout_no);
            holder.op_name  = (TextView) convertView.findViewById(R.id.op_name);
            holder.main_layout = (RelativeLayout) convertView.findViewById(R.id.main_layout);
            convertView.setTag(holder);
        }else{
            holder = (BusListViewHolder) convertView.getTag();
        }
    if(mListbus.get(position).route_number != null && mListbus.get(position).operator_name!= null) {
    holder.route_no.setText(mListbus.get(position).route_number);
    holder.op_name.setText(mListbus.get(position).operator_name);
    }
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sub = new Intent(context, AisSubscriptionActivity.class);

                //sub.putExtra("value", (Serializable) mListbus.get(position));
                sub.putExtra("id",mListbus.get(position).id );
                sub.putExtra("begin_time",mListbus.get(position).begin_time );
                sub.putExtra("end_time",mListbus.get(position).end_time );
                sub.putExtra("route_number",mListbus.get(position).route_number );
                sub.putExtra("description",mListbus.get(position).description );
                sub.putExtra("operator_name",mListbus.get(position).operator_name );
                sub.putExtra("emergency_contact_number",mListbus.get(position).emergency_contact_number );
                sub.putExtra("attachment",mListbus.get(position).attachment );
                sub.putExtra("transport_coupon",mListbus.get(position).transport_coupon );
                context.startActivity(sub);
            }
        });

        return  convertView;
    }

    private class BusListViewHolder
    {
        TextView route_no, op_name;
        RelativeLayout main_layout;


    }
}
