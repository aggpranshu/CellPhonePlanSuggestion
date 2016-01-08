
package com.lochbridge.cellphoneplan.android;

import static java.util.Calendar.YEAR;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

/**
 * Created by PAggarwal1 on 9/16/2015.
 */
public class FragmentDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private long diffInMilis;
    private long diffInDays;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, month, day, 0, 0);
        calendar.set(Calendar.YEAR, year);

        String formatedDate = sdf.format(calendar.getTime());
        if (calendar.getTime().before(Calendar.getInstance().getTime())) {
            diffInMilis = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
            diffInDays = diffInMilis / (24 * 60 * 60 * 1000);
            if (diffInDays > 21) {
                String promptMessage = "Cannot fetch call logs for " + diffInDays
                        + " days right now. Please wait for "
                        + String.valueOf(diffInDays - 21) + "days";
                Toast.makeText(getContext(), promptMessage, Toast.LENGTH_SHORT).show();
            }

            else {
                Toast.makeText(getContext(), "Day before", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getBaseContext(), UserLogsTabs.class);
                intent.putExtra("date", calendar.getTime());
                intent.putExtra("whenItHappened", "before");
                startActivity(intent);

            }
        }

        else if (calendar.getTime().equals(Calendar.getInstance().getTime()))
        {
            Toast.makeText(getContext(), "Please select a date before the current date",
                    Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(getContext(), "Cannot fetch call logs from future", Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent(getActivity(), CallLogStats.class);
            intent.putExtra("date", calendar.getTime());
            intent.putExtra("whenItHappened", "after");
            startActivity(intent);*/
        }
    }
}
