
package com.lochbridge.cellphoneplan.android;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lochbridge.cellphoneplan.model.AggregatedLogStats;

/**
 * Created by rgupta1 on 1/6/2016.
 */
public class FragmentMesgInternet extends Fragment {

    private Button b;
    TableLayout stk;
    private Bundle data;
    private AggregatedLogStats aggregatedLogStats;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mesg_internet, container, false);
        stk = (TableLayout) v.findViewById(R.id.tableUserData);
        data = this.getArguments();
        aggregatedLogStats = (AggregatedLogStats) data.getSerializable("logAggregationObj");

        b = (Button) v.findViewById(R.id.layoutIncluded).findViewById(R.id.buttonBill);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Hello from the other side", Toast.LENGTH_SHORT).show();
            }
        });

        initializeTable(aggregatedLogStats);

        return v;
    }

    private void initializeTable(AggregatedLogStats aggregatedLogStats) {

        TableRow tbrow0 = new TableRow(getActivity());
        TableRow tbrow1 = new TableRow(getActivity());

        TextView tv0 = new TextView(getActivity());
        tv0.setText("SMS count");
        tv0.setGravity(Gravity.CENTER);
        tv0.setTextColor(Color.BLACK);
        tbrow0.addView(tv0);


        TextView tv1 = new TextView(getActivity());
        tv1.setText(String.valueOf(aggregatedLogStats.getSmsCount()));
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextColor(Color.BLACK);

        tbrow0.addView(tv1);


        TextView tv2 = new TextView(getActivity());
        tv2.setText("Data Usage");
        tv2.setGravity(Gravity.CENTER);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextColor(Color.BLACK);
        tbrow1.addView(tv2);


        TextView tv3 = new TextView(getActivity());
        tv3.setText(((ApplicationClass) getActivity().getApplication()).getDataUsage());
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextColor(Color.BLACK);
        tbrow1.addView(tv3);

        stk.addView(tbrow0, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));


    }
}
