package com.knwedu.ourschool.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;

public class ClassPositionView extends LinearLayout {
    private View chart;
    private String classpositionTitle, subjectName, rankNo;
    private Context context;

    public ClassPositionView(Context context, AttributeSet attrs, String classpositionTitle, String subjectName, String rankNo) {
        super(context, attrs);
        this.context = context;
        this.classpositionTitle =  classpositionTitle;
        this.subjectName = subjectName;
        this.rankNo = rankNo;
        initializeElements();
    }

    public ClassPositionView(Context context, String classpositionTitle, String subjectName, String rankNo) {
        super(context);
        this.context = context;
        this.classpositionTitle =  classpositionTitle;
        this.subjectName = subjectName;
        this.rankNo = rankNo;
        initializeElements();
    }

    public ClassPositionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeElements();
    }

    private void initializeElements(){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.rank_view, this, true);

        TextView textViewClassPosition = (TextView)view.findViewById(R.id.textViewClassPosition);
        textViewClassPosition.setText(classpositionTitle);

        TextView textViewSubject = (TextView)view.findViewById(R.id.textViewSubject);
        textViewSubject.setText(subjectName);

        TextView textViewRank = (TextView)view.findViewById(R.id.textViewRank);
        textViewRank.setText(rankNo);
    }
}
