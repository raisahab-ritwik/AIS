package com.knwedu.ourschool.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class SubjectView extends LinearLayout {
    private View chart;
    private int topperValue, obtainedValue, yAxisMaxVal;
    private String termName,subjectName,studentName;
    private Context context;

    public SubjectView(Context context, AttributeSet attrs,int topperValue, int obtainedValue,int yAxisMaxVal, String termName, String subjectName, String studentName) {
        super(context, attrs);
        this.context = context;
        this.topperValue =  topperValue;
        this.obtainedValue = obtainedValue;
        this.yAxisMaxVal = yAxisMaxVal;
        this.termName = termName;
        this.subjectName = subjectName;
        this.studentName = studentName;
        initializeElements();
        openChart();
    }

    public SubjectView(Context context,int topperValue, int obtainedValue,int yAxisMaxVal, String termName, String subjectName, String studentName) {
        super(context);
        this.context = context;
        this.topperValue =  topperValue;
        this.obtainedValue = obtainedValue;
        this.yAxisMaxVal = yAxisMaxVal;
        this.termName = termName;
        this.subjectName = subjectName;
        this.studentName = studentName;
        initializeElements();
        openChart();
    }

    public SubjectView(Context context,float topperValue, float obtainedValue,float yAxisMaxVal, String termName, String subjectName, String studentName) {
        super(context);
        this.context = context;
        this.topperValue =  (int)topperValue;
        this.obtainedValue = (int)obtainedValue;
        this.yAxisMaxVal = (int)yAxisMaxVal;
        this.termName = termName;
        this.subjectName = subjectName;
        this.studentName = studentName;
        initializeElements();
        openChart();
    }

    public SubjectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeElements();
        openChart();
    }

    private void initializeElements(){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.subject_view, this, true);
        TextView differMarks = (TextView)view.findViewById(R.id.textViewMarksDifference);
        int marksDiff = topperValue - obtainedValue;
        differMarks.setText(marksDiff+"");

        TextView texttermName = (TextView)view.findViewById(R.id.textViewSessionName);
        texttermName.setText(termName);

//        TextView textStudentName = (TextView)view.findViewById(R.id.textViewStudentName);
//        textStudentName.setText(studentName);

        TextView textSubName = (TextView)view.findViewById(R.id.textViewSubjectName);
        textSubName.setText(subjectName.toUpperCase());
    }

    private void openChart(){
        int[] x = { 0,1};
        //int[] expense = { 98,75};
        int[] expense = { topperValue, 0};

        //int[] mbl = {  Color.GREEN,Color.RED};
        int[] mbl = {  obtainedValue, 0};


        // Creating an XYSeries for Expense
        XYSeries expenseSeries = new XYSeries("Class Topper");

        for(int i=0;i<x.length;i++){
            expenseSeries.add(i,expense[i]);
            //expenseSeries.add(i,expense[i]);

        }

        XYSeries expenseSeries1 = new XYSeries(studentName);

        for(int i=0;i<x.length;i++){
            expenseSeries1.add(i,mbl[i]);
            //expenseSeries1.add(i,mbl[i]);

        }

        // Creating a dataset to hold  series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(expenseSeries);
        dataset.addSeries(expenseSeries1);


        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(Color.GREEN); //color of the graph set to cyan
        expenseRenderer.setFillPoints(true);
        expenseRenderer.setLineWidth(5);
        expenseRenderer.setChartValuesTextSize(18);
        expenseRenderer.setDisplayChartValues(true);

        XYSeriesRenderer expenseRenderer1 = new XYSeriesRenderer();
        expenseRenderer1.setColor(Color.RED); //color of the graph set to cyan
        expenseRenderer1.setFillPoints(true);
        expenseRenderer1.setChartValuesTextSize(18);
        expenseRenderer1.setLineWidth(5);
        expenseRenderer1.setDisplayChartValues(true);


        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        multiRenderer.setXLabels(0);
        //multiRenderer.setChartTitle("Expense Chart");
        //multiRenderer.setXTitle("Year 2016");
        //multiRenderer.setYTitle("Amount in Dollars");

        multiRenderer.setAxesColor(Color.YELLOW);

        /***
         * Customizing graphs
         */
        //setting text size of the title
        multiRenderer.setChartTitleTextSize(20);
        //setting text size of the axis title
        multiRenderer.setAxisTitleTextSize(20);
        //setting text size of the graph lable
        multiRenderer.setLabelsTextSize(20);
        //setting zoom buttons visiblity
        multiRenderer.setZoomButtonsVisible(false);
        //setting pan enablity which uses graph to move on both axis
        multiRenderer.setPanEnabled(false, false);
        //setting click false on graph
        multiRenderer.setClickEnabled(false);
        //setting zoom to false on both axis
        multiRenderer.setZoomEnabled(false, false);
        //setting lines to display on y axis
        multiRenderer.setShowGridY(false);
        //setting lines to display on x axis
        multiRenderer.setShowGridX(false);
        //setting legend to fit the screen size
        multiRenderer.setFitLegend(true);
        //setting displaying line on grid
        multiRenderer.setShowGrid(false);
        //setting zoom to false
        multiRenderer.setZoomEnabled(false);
        //setting external zoom functions to false
        multiRenderer.setExternalZoomEnabled(false);
        //setting displaying lines on graph to be formatted(like using graphics)
        multiRenderer.setAntialiasing(false);
        //setting to in scroll to false
        multiRenderer.setInScroll(false);
        //setting to set legend height of the graph
        multiRenderer.setLegendHeight(20);
        multiRenderer.setLegendTextSize(20);
        //setting x axis label align
        multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
        //setting y axis label to align
        multiRenderer.setYLabelsAlign(Paint.Align.LEFT);
        //setting text style
        multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        //setting no of values to display in y axis
        multiRenderer.setYLabels(10);
        multiRenderer.setYLabelsColor(0,Color.WHITE);
        // setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.
        // if you use dynamic values then get the max y value and set here

        multiRenderer.setYAxisMax(yAxisMaxVal);
        multiRenderer.setYAxisMin(0);
        //setting used to move the graph on xaxiz to .5 to the right
        multiRenderer.setXAxisMin(-1.0);
//setting max values to be display in x axis
        multiRenderer.setXAxisMax(0.7);
        //setting bar size or space between two bars
        // multiRenderer.setBarSpacing(0.5);
        //Setting background color of the graph to transparent
        multiRenderer.setBackgroundColor(Color.TRANSPARENT);
        //Setting margin color of the graph to transparent
        multiRenderer.setMarginsColor(getResources().getColor(R.color.transparent_background));
        multiRenderer.setApplyBackgroundColor(true);

        //setting the margin size for the graph in the order top, left, bottom, right
        multiRenderer.setMargins(new int[]{20, 20, 20, 10});

//        for(int i=0; i< x.length;i++){
//            multiRenderer.addXTextLabel(i, mMonth[i]);
//        }

        // Adding expenseRenderer to multipleRenderer
        multiRenderer.addSeriesRenderer(expenseRenderer);
        multiRenderer.addSeriesRenderer(expenseRenderer1);


        //this part is used to display graph on the xml
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
        //remove any views before u paint the chart
        chartContainer.removeAllViews();
        //drawing bar chart
        chart = ChartFactory.getBarChartView(getContext(), dataset, multiRenderer, BarChart.Type.DEFAULT);
        //adding the view to the linearlayout
        chartContainer.addView(chart);

    }


}
