package com.example.gh_train_app.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gh_train_app.Adapters.FareAdapter;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.Train;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FareEnquiryFragment extends Fragment implements View.OnClickListener {
    AutoCompleteTextView from, to;
    Button search;
    RecyclerView FareRecyclerView;
    DatabaseReference mDatabase;
    FareAdapter fareadapter;
    List<Train> trainfareList;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter2;
    String where, whereto;
    int flag = 0;
    ProgressDialog progressDialog;

    public FareEnquiryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_fare_enquiry, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference("Trains");
        sourceList();
        destinationList();
        from = root.findViewById(R.id.from_search_input);
        to = root.findViewById(R.id.to_search_input);
        search = root.findViewById(R.id.search);
        FareRecyclerView = root.findViewById(R.id.all_fare);
        FareRecyclerView.setHasFixedSize(true);
        FareRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        trainfareList = new ArrayList<>();
        fareadapter = new FareAdapter(getActivity(), trainfareList);
        FareRecyclerView.setAdapter(fareadapter);
        search.setOnClickListener(this);

        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                arrayAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                arrayAdapter.getFilter().filter(s.toString());
            }
        });

        to.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                arrayAdapter2.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                arrayAdapter2.getFilter().filter(s.toString());
            }
        });
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                getSpecifics();
                break;
        }
    }

    public void getSpecifics() {
        where = from.getText().toString().trim();
        whereto = to.getText().toString().trim();
        flag = 0;
        if (from.getText().toString().trim().length() == 0) {
            from.setError("Source cannot be Empty");
            from.requestFocus();
            flag = 1;
        }
        if (to.getText().toString().trim().length() == 0) {
            to.setError("Destination cannot be Empty");
            to.requestFocus();
            flag = 1;
        }
        if (flag == 0) {
            from.setError(null);
            to.setError(null);
            progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Searching...");
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

//                        Query query = mDatabase.orderByChild("dest")
//                                .startAt(where)
//                                .endAt(whereto+ "uf8ff");
//                        query.
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            trainfareList.clear();
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Train train = snapshot.getValue(Train.class);
                                    if (train.getSource().equals(where) && train.getDest().equals(whereto)) {
                                        trainfareList.add(train);
                                    }

                                }
                                if (trainfareList.size() == 0) {
                                    // Hiding the progress dialog.
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "No Search found", Toast.LENGTH_SHORT).show();
                                }
                                fareadapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            } else {
                                trainfareList.clear();
                                Toast.makeText(getActivity(), "No search found", Toast.LENGTH_SHORT).show();
                                trainfareList.clear();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
                }
            }, 4000);
        }
    }


    //    code for showing all sources
    public ArrayList<String> sourceList() {
        final ArrayList<String> source = new ArrayList<>();
        Query query = mDatabase;
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(Train.class).getSource();
                source.add(String.valueOf(value));
                arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, source);
                from.setAdapter(arrayAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }


        });
        return source;
    }

    //    code for showing all destinations
    public ArrayList<String> destinationList() {
        final ArrayList<String> destination = new ArrayList<>();
        Query query = mDatabase;
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(Train.class).getDest();
                destination.add(String.valueOf(value));
                arrayAdapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, destination);
                from.setAdapter(arrayAdapter2);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }


        });
        return destination;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.menu_fare_enquiry);
    }
}


