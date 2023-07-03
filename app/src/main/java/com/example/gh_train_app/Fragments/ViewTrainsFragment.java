package com.example.gh_train_app.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gh_train_app.Adapters.TrainAdapter;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.Train;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ViewTrainsFragment extends Fragment {
    RecyclerView TrainRecyclerview;
    List<Train> mData;
    EditText searchInput ;
    CharSequence search="";
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    TrainAdapter trainAdapter;
    RelativeLayout noitem;

    public ViewTrainsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_view_train, container, false);
        searchInput = root.findViewById(R.id.search_input);
        TrainRecyclerview = root.findViewById(R.id.all_train);
        noitem = root.findViewById(R.id.noitem);
        mData = new ArrayList<>();

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(getActivity());

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading Data...");

        // Showing progress dialog.
        progressDialog.show();

        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
        databaseReference = FirebaseDatabase.getInstance().getReference("Trains");
        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Train data = postSnapshot.getValue(Train.class);

                    mData.add(data);
                }
                if(mData.size()==0)
                {
                    // Hiding the progress dialog.
                    progressDialog.dismiss();
                    noitem.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "No Data",Toast.LENGTH_SHORT).show();
                }else {
                    trainAdapter = new TrainAdapter(getContext(), mData);
                    TrainRecyclerview.setAdapter(trainAdapter);
                    // Hiding the progress dialog.
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });

        // adapter ini and setup
        TrainRecyclerview.setAdapter(trainAdapter);
        TrainRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                trainAdapter.getFilter().filter(s);
                search = s;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated( view,savedInstanceState );
        getActivity().setTitle( R.string.menu_view_all_trains );
    }
}
