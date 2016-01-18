
package com.lochbridge.cellphoneplan.android;

import java.util.HashSet;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lochbridge.cellphoneplan.Utils.URLClass;
import com.lochbridge.cellphoneplan.model.BillPlans;
import com.lochbridge.cellphoneplan.model.BillPlansList;
import com.lochbridge.cellphoneplan.model.PlanDetails;

public class BillList extends AppCompatActivity {

    private TableLayout stk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);
        stk = (TableLayout) findViewById(R.id.tableForBill);

        BillPlansList billPlansList = (BillPlansList) getIntent()
                .getSerializableExtra("billObject");

        HashSet<BillPlans> refinedBillPlans = billPlansList.getBill();

        /*
         * for(int i = 3;i< initialBillPlans.size();i++){ if(basicBill.getBill()<
         * initialBillPlans.get(i).getBill()){ refinedBillPlans.add(0,basicBill);
         * refinedBillPlans.add(1, initialBillPlans.get(1)); refinedBillPlans.add(2,
         * initialBillPlans.get(2)); } else { refinedBillPlans.add(i, initialBillPlans.get(i)); } }
         */

        initializeTable(refinedBillPlans);
    }

    private void initializeTable(final HashSet<BillPlans> billPlans) {

        TextView t1v, t2v, t3v, t4v;
        TableRow tbrow0 = new TableRow(this);

        TextView tv0 = new TextView(this);
        tv0.setText(" Plan ID ");
        tv0.setGravity(Gravity.CENTER);
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText(" Total Bill ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText(" Recharge\nvalue ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(" Recharge Validity\n(Days) ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);

        stk.addView(tbrow0, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        Iterator<BillPlans> iterator = billPlans.iterator();
        while (iterator.hasNext()) {

            BillPlans billPlansObj = iterator.next();

            TableRow tbrow = new TableRow(this);

            View v=new View(this);
            v.setBackgroundColor(Color.BLACK);
            v.setLayoutParams(new TableLayout.LayoutParams(5,5));

            final Button buttonID = new Button(this);
            buttonID.setText(billPlansObj.getId().toString());
            buttonID.setTextColor(Color.WHITE);
            buttonID.setGravity(Gravity.LEFT);
            buttonID.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            buttonID.setBackgroundColor(Color.TRANSPARENT);
            tbrow.addView(buttonID);

            buttonID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BgTaskPlanByID().execute(buttonID.getText().toString());
                }
            });

            Button buttonBill = new Button(this);
            buttonBill.setText(billPlansObj.getBill().toString());
            buttonBill.setTextColor(Color.WHITE);
            buttonBill.setGravity(Gravity.LEFT);
            buttonBill.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            buttonBill.setBackgroundColor(Color.TRANSPARENT);
            tbrow.addView(buttonBill);

            Button buttonRate = new Button(this);
            buttonRate.setText(billPlansObj.getRechargeRate().toString());
            buttonRate.setTextColor(Color.WHITE);
            buttonRate.setGravity(Gravity.LEFT);
            buttonRate.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            buttonRate.setBackgroundColor(Color.TRANSPARENT);
            tbrow.addView(buttonRate);

            Button buttonValidity = new Button(this);
            buttonValidity.setText(String.valueOf(billPlansObj.getType()));
            buttonValidity.setTextColor(Color.WHITE);
            buttonValidity.setGravity(Gravity.LEFT);
            buttonValidity.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            buttonValidity.setBackgroundColor(Color.TRANSPARENT);
            tbrow.addView(buttonValidity);

            stk.addView(tbrow, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            stk.addView(v);
        }
    }

    private class BgTaskPlanByID extends AsyncTask<String, Void, PlanDetails> {

        @Override
        protected PlanDetails doInBackground(String... params) {
            try {
                String url = URLClass.baseURL + URLClass.dataproviderURL
                        + ((ApplicationClass) getApplication()).getProviderName() + "/" +
                        ((ApplicationClass) getApplication()).getCircleName()
                        + "/" + params[0];
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                return restTemplate.getForObject(url, PlanDetails.class);
            } catch (Exception e) {
                Log.e("UserTelecomDetailsActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(PlanDetails planDetails) {
            super.onPostExecute(planDetails);

            String planDetailsStr = "";

            Toast.makeText(getApplicationContext(), planDetails.toString(), Toast.LENGTH_SHORT)
                    .show();

            try {
                JSONObject jsonObject = new JSONObject(planDetails.getPlanDetails());
                planDetailsStr = "Recharge Value:" + jsonObject.getString("recharge_value") +
                        "\nRecharge Validity:" + jsonObject.getString("recharge_validity") +
                        "\nrecharge Talktime:" + jsonObject.getString("recharge_talktime") +
                        "\nDescription:" + jsonObject.getString("recharge_description_more");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    BillList.this);

            // set title
            alertDialogBuilder.setTitle("Plan Details");

            // set dialog message
            alertDialogBuilder
                    .setMessage(planDetailsStr)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }
    }
}
