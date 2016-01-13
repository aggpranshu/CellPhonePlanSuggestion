
package com.lochbridge.cellphoneplan.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.lochbridge.cellphoneplan.Utils.URLClass;
import com.lochbridge.cellphoneplan.model.AggregatedLogStats;
import com.lochbridge.cellphoneplan.model.BillPlansList;
import com.lochbridge.cellphoneplan.model.ProviderList;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.sql.Array;
import java.util.ArrayList;

public class FragmentPlansPicker extends DialogFragment {

    Bundle plansBundle;
    private ListView callLV, mesgLV, internetLV;
    private String[] arrayCall;
    private String[] arrayMesg;
    private String[] arrayInternet;
    SparseBooleanArray checked;
    ArrayList<String> selectedItemsCalls;
    ArrayList<String> selectedItemsMesg;
    ArrayList<String> selectedItemsInternet;


    public FragmentPlansPicker() {
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        plansBundle = this.getArguments();

        arrayCall = plansBundle.getStringArrayList("listPlanCalls").toArray(
                new String[plansBundle.getStringArrayList("listPlanCalls").size()]);

        arrayMesg = plansBundle.getStringArrayList("listPlanMesg").toArray(
                new String[plansBundle.getStringArrayList("listPlanMesg").size()]);

        arrayInternet = plansBundle.getStringArrayList("listPlanInternet").toArray(
                new String[plansBundle.getStringArrayList("listPlanInternet").size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.tab_layout, null);
        callLV = (ListView) view.findViewById(R.id.listViewCall);
        mesgLV = (ListView) view.findViewById(R.id.listViewMesg);
        internetLV = (ListView) view.findViewById(R.id.listViewInternet);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.simple_multiselect_listview, arrayCall);
        callLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        callLV.setAdapter(adapter);

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(),
                R.layout.simple_multiselect_listview, arrayMesg
                );
        mesgLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mesgLV.setAdapter(adapter2);

        final ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getActivity(),
                R.layout.simple_multiselect_listview, arrayInternet);
        internetLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        internetLV.setAdapter(adapter3);

        TabHost tabs = (TabHost) view.findViewById(R.id.tabHost);

        tabs.setup();

        TabHost.TabSpec tabpage1 = tabs.newTabSpec("one");
        tabpage1.setContent(R.id.listViewCall);
        tabpage1.setIndicator("Call");

        TabHost.TabSpec tabpage2 = tabs.newTabSpec("two");
        tabpage2.setContent(R.id.listViewMesg);
        tabpage2.setIndicator("Message");

        TabHost.TabSpec tabpage3 = tabs.newTabSpec("two");
        tabpage3.setContent(R.id.listViewInternet);
        tabpage3.setIndicator("Internet");

        tabs.addTab(tabpage1);
        tabs.addTab(tabpage2);
        tabs.addTab(tabpage3);

        builder.setView(view);

        builder.setTitle("Select your plan");

        callLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                checked = callLV.getCheckedItemPositions();
                 selectedItemsCalls = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position1 = checked.keyAt(i);
                    // Add sport if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i))
                        selectedItemsCalls.add(adapter.getItem(position1).split("abcde")[0]);
                    Log.i("SparseBoolean", String.valueOf(selectedItemsCalls.size()));
                }
                Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
            }
        });


        mesgLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checked = mesgLV.getCheckedItemPositions();
                 selectedItemsMesg = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position1 = checked.keyAt(i);
                    // Add sport if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i))
                        selectedItemsMesg.add(adapter2.getItem(position1).split("abcde")[0]);
                    Log.i("SparseBoolean", String.valueOf(selectedItemsMesg.size()));
                }
            }
        });

        internetLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checked = internetLV.getCheckedItemPositions();
                 selectedItemsInternet = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position1 = checked.keyAt(i);
                    // Add sport if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i))
                        selectedItemsInternet.add(adapter3.getItem(position1).split("abcde")[0]);
                    Log.i("SparseBoolean", String.valueOf(selectedItemsInternet.size()));
                }
            }
        });


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Plans selected", Toast.LENGTH_SHORT).show();

                ArrayList<String> listUserPlan = new ArrayList<String>();
                listUserPlan.addAll(selectedItemsCalls);
                listUserPlan.addAll(selectedItemsInternet);
                listUserPlan.addAll(selectedItemsMesg);

                /*for (int i = 0; i < selectedItemsCalls.size(); i++) {
                    Log.i("Selected Items id", selectedItemsCalls.get(i));
                }

                for (int i = 0; i < selectedItemsMesg.size(); i++) {
                    Log.i("Selected Items id", selectedItemsMesg.get(i));
                }

                for (int i = 0; i < selectedItemsInternet.size(); i++) {
                    Log.i("Selected Items id", selectedItemsInternet.get(i));
                }*/

                new BGTaskUserPlanInfo().execute(listUserPlan);
            }
        });

        return builder.create();

    }

    private class BGTaskUserPlanInfo extends AsyncTask<ArrayList<String>,Void,String>{


        @Override
        protected String doInBackground(ArrayList... params) {

            String url = URLClass.baseURL + URLClass.UserPlanURL;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String result = restTemplate.postForObject(url + "/userplans" , params[0],
                    String.class);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        //    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        }
    }
}
