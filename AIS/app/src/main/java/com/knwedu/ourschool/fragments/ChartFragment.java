package com.knwedu.ourschool.fragments;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class ChartFragment  extends Fragment {

	private View mChart;
	private String[] mMonth = new String[] {
			"Class Topper","Amit Das"
	};

	private LinearLayout chart;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.chart_fragment, container,
				false);
		//SchoolAppUtils.loadAppLogo(getActivity(), (ImageButton) view.findViewById(R.id.app_logo));

		initialize();

		return view;
	}

	private void initialize() {
		chart = (LinearLayout) view.findViewById(R.id.chart);
		openChart();
	}

	private void openChart(){
		int[] x = { 0,1};
		int[] expense = { 98,75};

		int[] mbl = {  Color.GREEN,Color.RED};


		// Creating an XYSeries for Expense
		XYSeries expenseSeries = new XYSeries("");

		for(int i=0;i<x.length;i++){
			expenseSeries.add(i,expense[i]);
			//expenseSeries.add(i,expense[i]);

		}

		XYSeries expenseSeries1 = new XYSeries("");

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
		expenseRenderer.setChartValuesTextSize(20);
		expenseRenderer.setDisplayChartValues(true);

		XYSeriesRenderer expenseRenderer1 = new XYSeriesRenderer();
		expenseRenderer1.setColor(Color.RED); //color of the graph set to cyan
		expenseRenderer1.setFillPoints(true);
		expenseRenderer1.setChartValuesTextSize(20);
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
		multiRenderer.setChartTitleTextSize(28);
		//setting text size of the axis title
		multiRenderer.setAxisTitleTextSize(24);
		//setting text size of the graph lable
		multiRenderer.setLabelsTextSize(24);
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
		multiRenderer.setAntialiasing(true);
		//setting to in scroll to false
		multiRenderer.setInScroll(false);
		//setting to set legend height of the graph
		multiRenderer.setLegendHeight(30);
		//setting x axis label align
		multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
		//setting y axis label to align
		multiRenderer.setYLabelsAlign(Paint.Align.LEFT);
		//setting text style
		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
		//setting no of values to display in y axis
		multiRenderer.setYLabels(10);
		multiRenderer.setYLabelsColor(0,Color.YELLOW);
		// setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.
		// if you use dynamic values then get the max y value and set here

		multiRenderer.setYAxisMax(100);
		multiRenderer.setYAxisMin(0);
		//setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMin(-0.5);
//setting max values to be display in x axis
		multiRenderer.setXAxisMax(2);
		//setting bar size or space between two bars
		// multiRenderer.setBarSpacing(0.5);
		//Setting background color of the graph to transparent
		multiRenderer.setBackgroundColor(Color.TRANSPARENT);
		//Setting margin color of the graph to transparent
		multiRenderer.setMarginsColor(getResources().getColor(R.color.transparent_background));
		multiRenderer.setApplyBackgroundColor(true);

		//setting the margin size for the graph in the order top, left, bottom, right
		multiRenderer.setMargins(new int[]{30, 30, 30, 30});

//        for(int i=0; i< x.length;i++){
//            multiRenderer.addXTextLabel(i, mMonth[i]);
//        }

		// Adding expenseRenderer to multipleRenderer
		multiRenderer.addSeriesRenderer(expenseRenderer);
		multiRenderer.addSeriesRenderer(expenseRenderer1);


		//this part is used to display graph on the xml
		//LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
		//remove any views before u paint the chart
		//chartContainer.removeAllViews();

		chart.removeAllViews();
		//drawing bar chart
		mChart = ChartFactory.getBarChartView(getActivity(), dataset, multiRenderer, BarChart.Type.DEFAULT);
		//adding the view to the linearlayout
		chart.addView(chart);

	}


	private void openChart_old(){
		int[] x = { 0 ,1 };
		int[] income = { 98, 75};
		//int[] expense = {75 };

		// Creating an XYSeries for Income
		XYSeries incomeSeries = new XYSeries("Highest Marks");
		// Creating an XYSeries for Expense
		XYSeries expenseSeries = new XYSeries("Obtained Marks");
		// Adding data to Income and Expense Series


		for(int i=0;i<x.length;i++){
			incomeSeries.add(i,income[i]);
			//expenseSeries.add(i,expense[i]);
		}



		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(incomeSeries);
			
		// Adding Expense Series to dataset
		//dataset.addSeries(expenseSeries);

		// Creating XYSeriesRenderer to customize incomeSeries
		XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
		System.out.println(incomeSeries.getIndexForKey(0)+"value"+incomeSeries.getIndexForKey(1));
		if(incomeSeries.getX(0) == 0.0){
			incomeRenderer.setColor(Color.GREEN);
		}else if(incomeSeries.getX(1) == 1.0){
			incomeRenderer.setColor(Color.RED);
		}
		//incomeRenderer.setColor(Color.GREEN); //color of the graph set to cyan
		incomeRenderer.setFillPoints(true);
		incomeRenderer.setLineWidth(20);
		incomeRenderer.setChartValuesTextSize(20);
		incomeRenderer.setChartValuesSpacing(20);
		incomeRenderer.setDisplayChartValues(true);
		//incomeRenderer.setDisplayChartValuesDistance(10); //setting chart value distance

		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
		expenseRenderer.setColor(Color.RED);
		expenseRenderer.setFillPoints(true);
		expenseRenderer.setLineWidth(20);
		expenseRenderer.setChartValuesTextSize(20);
		expenseRenderer.setChartValuesSpacing(20);
		expenseRenderer.setDisplayChartValues(true);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		//multiRenderer.setXLabels(5);
		//multiRenderer.setChartTitle("Income vs Expense Chart");
		//multiRenderer.setXTitle("Year 2014");
		//multiRenderer.setYTitle("Marks Obtained");

		/***
		 * Customizing graphs
		 */
//setting text size of the title
		multiRenderer.setChartTitleTextSize(28);
		//setting text size of the axis title
		multiRenderer.setAxisTitleTextSize(24);
		//setting text size of the graph lable
		multiRenderer.setLabelsTextSize(24);
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
		multiRenderer.setAntialiasing(true);
		//setting to in scroll to false
		multiRenderer.setInScroll(false);
		//setting to set legend height of the graph
		multiRenderer.setLegendHeight(30);
		//setting x axis label align
		multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
		//setting y axis label to align
		multiRenderer.setYLabelsAlign(Paint.Align.LEFT);
		//setting text style
		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
		//setting no of values to display in y axis
		//multiRenderer.setYLabels(10);
		// setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.
		// if you use dynamic values then get the max y value and set here
		multiRenderer.setYAxisMax(100);
		multiRenderer.setYAxisMin(0);
		//setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMin(-10);
//setting max values to be display in x axis
		multiRenderer.setXAxisMax(50);
		//setting bar size or space between two bars
		multiRenderer.setBarSpacing(10);
		//Setting background color of the graph to transparent
		multiRenderer.setBackgroundColor(Color.TRANSPARENT);
		//Setting margin color of the graph to transparent
		multiRenderer.setMarginsColor(getResources().getColor(R.color.transparent_background));
		multiRenderer.setApplyBackgroundColor(true);

		//setting the margin size for the graph in the order top, left, bottom, right
		multiRenderer.setMargins(new int[]{30, 30, 30, 30});




//		for(int i=0; i< x.length;i++){
//			multiRenderer.addXTextLabel(i, mMonth[i]);
//		}

		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(incomeRenderer);
		//multiRenderer.addSeriesRenderer(expenseRenderer);

		//this part is used to display graph on the xml

		//remove any views before u paint the chart
		chart.removeAllViews();
		//drawing bar chart
		mChart = ChartFactory.getBarChartView(getActivity(), dataset, multiRenderer,Type.DEFAULT);
		//adding the view to the linearlayout
		chart.addView(mChart);

	}

	/*public Intent getIntent(Context context){

		int y[] = {25,10,15,20};

		CategorySeries series = new CategorySeries("Bar1");
		for(int i=0; i < y.length; i++){
			series.add("Bar"+(i+1),y[i]);
		}

		XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();  // collection of series under one object.,there could any
		dataSet.addSeries(series.toXYSeries());                            // number of series

		//customization of the chart

		XYSeriesRenderer renderer = new XYSeriesRenderer();     // one renderer for one series
		renderer.setColor(Color.RED);
		renderer.setDisplayChartValues(true);
		renderer.setChartValuesSpacing((float) 5.5d);
		renderer.setLineWidth((float) 10.5d);


		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();   // collection multiple values for one renderer or series
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.setChartTitle("Demo Graph");
//        mRenderer.setXTitle("xValues");
		mRenderer.setYTitle("Rupee");
		mRenderer.setZoomButtonsVisible(true);    mRenderer.setShowLegend(true);
		mRenderer.setShowGridX(true);      // this will show the grid in  graph
		mRenderer.setShowGridY(true);
//        mRenderer.setAntialiasing(true);
		mRenderer.setBarSpacing(.5);   // adding spacing between the line or stacks
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.BLACK);
		mRenderer.setXAxisMin(0);
//        mRenderer.setYAxisMin(.5);
		mRenderer.setXAxisMax(5);
		mRenderer.setYAxisMax(50);
//
		mRenderer.setXLabels(0);
		mRenderer.addXTextLabel(1,"Income");
		mRenderer.addXTextLabel(2,"Saving");
		mRenderer.addXTextLabel(3,"Expenditure");
		mRenderer.addXTextLabel(4,"NetIncome");
		mRenderer.setPanEnabled(true, true);    // will fix the chart position
		Intent intent = ChartFactory.getBarChartIntent(context, dataSet, mRenderer,Type.DEFAULT);

		return intent;
	}*/
}