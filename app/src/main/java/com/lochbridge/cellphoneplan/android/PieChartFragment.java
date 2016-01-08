package com.lochbridge.cellphoneplan.android;

/**
 * Created by rgupta1 on 1/6/2016.
 */

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;


import android.support.v4.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lochbridge.cellphoneplan.spring.AggregatedLogStats;


public class PieChartFragment extends Fragment {


    private Bundle data;
    private RelativeLayout mainLayout;
    private PieChart mChart;
    private AggregatedLogStats aggregatedLogStats;
    // we're going to display pie chart for smartphones martket shares
   /* private float[] yData = { 5, 10, 15, 30, 40 };
    private String[] xData = { "Sony", "Huawei", "LG", "Apple", "Samsung" };
*/

    private Integer[] yData;

    private final String[] xData = {"Local Day Different Network", "Local Day Same Network",
            "Local Day Same Network", "Local Night Different Network",
            "STD Day Same Network", "STD Day Different Network", "STD Night Same Network",
            "STD Night Different Network"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        data = this.getArguments();

        aggregatedLogStats = (AggregatedLogStats) data.getSerializable("logAggregationObj");


        yData = new Integer[]{
                aggregatedLogStats.getLddInSeconds(),
                aggregatedLogStats.getLdsInSeconds(), aggregatedLogStats.getLnsInSeconds(),
                aggregatedLogStats.getLndInSeconds(), aggregatedLogStats.getSdsInSeconds(),
                aggregatedLogStats.getSddInSeconds(), aggregatedLogStats.getSnsInSeconds(),
                aggregatedLogStats.getSndInSeconds()
        };


        View v = inflater.inflate(R.layout.fragment_pie_chart,
                container, false);

        mainLayout = (RelativeLayout) v.findViewById(R.id.mainLayout);
        mChart = new PieChart(getActivity());

        mChart.setDrawSliceText(false);


        // add pie chart to main layout
        mainLayout.addView(mChart, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mainLayout.setBackgroundColor(Color.parseColor("#55656C"));

        // configure pie chart
        mChart.setUsePercentValues(true);
        mChart.setDescription("Aggregated Log stats data");

        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.BLACK);
        // mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(7);
        mChart.setTransparentCircleRadius(10);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        // set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(getActivity(),
                        xData[e.getXIndex()] + " = " + e.getVal() + "%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // add data
        addData();

        // customize legends
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
        return v;
    }

    private void addData() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < yData.length; i++) {
            if(yData[i]!=0){
            yVals1.add(new Entry(yData[i], i));
                xVals.add(xData[i]);
            }
        }


        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1,"");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(xVals, dataSet);
        //data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();

    }

    // TODO: Rename method, update argument and hook method into UI event


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
