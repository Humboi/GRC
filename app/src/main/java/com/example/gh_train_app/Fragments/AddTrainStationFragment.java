package com.example.gh_train_app.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gh_train_app.R;
import com.example.gh_train_app.models.TrainStation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import me.jfenn.timedatepickers.dialogs.PickerDialog;
import me.jfenn.timedatepickers.dialogs.TimeSheetPickerDialog;
import me.jfenn.timedatepickers.views.LinearTimePickerView;

public class AddTrainStationFragment extends Fragment implements View.OnClickListener{
    EditText stationname, stationnumber, stationdesc, open, close;
    String station_name, station_number, station_des, open_time, closing_time;
    Button addtrainstation;
    DatabaseReference mdb;
    ProgressDialog progressDialog;
    int flag = 0;
    private String format = "";

    public AddTrainStationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_train_station, container, false);
        addtrainstation = root.findViewById(R.id.addtrainstation);
        close = root.findViewById(R.id.closingtime);
        open = root.findViewById(R.id.openingtime);
        stationdesc = root.findViewById(R.id.stationdesc);
        stationnumber = root.findViewById(R.id.stationnumber);
        stationname = root.findViewById(R.id.stationname);
        mdb = FirebaseDatabase.getInstance().getReference().child("TrainStations");
        close.setOnClickListener(this);
        open.setOnClickListener(this);
        addtrainstation.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closingtime:
                new TimeSheetPickerDialog(getActivity())
                        .setListener(new PickerDialog.OnSelectedListener<LinearTimePickerView>() {
                            @Override
                            public void onSelect(PickerDialog<LinearTimePickerView> dialog, LinearTimePickerView view) {
                                  showTime(view.getHourOfDay(),view.getMinute());
                                close.setText(new StringBuilder().append(view.getHourOfDay()).append(" : ").append(view.getMinute())
                                        .append(" ").append(format));
                                Toast.makeText(getActivity(),  close.getText().toString(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCancel(PickerDialog<LinearTimePickerView> dialog) {
                            }
                        })
                        .show();
                break;
            case R.id.openingtime:
                new TimeSheetPickerDialog(getActivity())
                        .setListener(new PickerDialog.OnSelectedListener<LinearTimePickerView>() {
                            @Override
                            public void onSelect(PickerDialog<LinearTimePickerView> dialog, LinearTimePickerView view) {
                                showTime(view.getHourOfDay(),view.getMinute());
                                open.setText(new StringBuilder().append(view.getHourOfDay()).append(" : ").append(view.getMinute())
                                        .append(" ").append(format));
                                Toast.makeText(getActivity(),  open.getText().toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel(PickerDialog<LinearTimePickerView> dialog) {
                            }
                        })
                        .show();
                break;
            case R.id.addtrainstation:
                 addnewtrainstation();
                break;
        }
    }

    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

    }

    public void addnewtrainstation(){
            station_name = stationname.getText().toString().trim();
            station_number = stationnumber.getText().toString().trim();
            station_des = stationdesc.getText().toString().trim();
            open_time = open.getText().toString().trim();
            closing_time = close.getText().toString().trim();
            flag = 0;
            if (stationname.getText().toString().trim().length() == 0) {
            stationname.setError("Station Name cannot be Empty");
            stationname.requestFocus();
            flag = 1;
            }
            if (stationnumber.getText().toString().trim().length() == 0) {
            stationnumber.setError("Station number cannot be Empty");
            stationnumber.requestFocus();
            flag = 1;
            }
            if (stationdesc.getText().toString().trim().length() == 0) {
            stationdesc.setError("Description cannot be Empty");
            stationdesc.requestFocus();
            flag = 1;
            }
            if (open.getText().toString().trim().length() == 0) {
            open.setError("Open time cannot be Empty");
            open.requestFocus();
            flag = 1;
            }
            if (close.getText().toString().trim().length() == 0) {
            close.setError("Close time cannot be Empty");
            close.requestFocus();
            flag = 1;
            }
            if (flag == 0) {
            stationname.setError(null);
            stationnumber.setError(null);
            stationdesc.setError(null);
            open.setError(null);
            close.setError(null);
            progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Adding Train Station...");
            progressDialog.show();
                new Handler().postDelayed(() -> {
                    //this create a unique id and we will use it as the Primary Key for our Artist
                    String trainstationid = mdb.push().getKey();
                    //creating an Train Station Object
                    TrainStation trainStation = new TrainStation(trainstationid,station_name,station_number,station_des,closing_time,open_time);
                    //Saving the trainstation
                    mdb.child(trainstationid).setValue(trainStation);
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Train Station Added Successfully", Toast.LENGTH_SHORT).show();
                   //navigate bacjk to home page
                    Fragment frag = new HomeFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.flContent, frag);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }, 4000);
            }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated( view,savedInstanceState );
        getActivity().setTitle( R.string.menu_add_train_stations );
    }

}
