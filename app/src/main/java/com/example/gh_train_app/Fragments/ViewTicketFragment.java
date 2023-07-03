package com.example.gh_train_app.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gh_train_app.Adapters.TicketAdapter;
import com.example.gh_train_app.OthersFiles.SpacingItemDecoration;
import com.example.gh_train_app.OthersFiles.Tools;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ViewTicketFragment extends Fragment {
    //recyclerview object
    private RecyclerView recyclerView;

    //adapter object
    private RecyclerView.Adapter adapter;

    //database reference
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Ticket> uploads;

    //no data present
    private RelativeLayout noitem;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    public ViewTicketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_view_ticket, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        recyclerView = root.findViewById(R.id.recyclerView);
        noitem = root.findViewById(R.id.noitem);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 4), true));
        recyclerView.setHasFixedSize(true);

        progressDialog = new ProgressDialog(getActivity());

        uploads = new ArrayList<>();
        String user = currentUser.getUid();
        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference("Tickets");

        //adding an event listener to fetch values
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Ticket upload = postSnapshot.getValue(Ticket.class);
                    String n=firebaseAuth.getCurrentUser().getUid();
                    if( upload.getUserID().equals(n)) {
                        uploads.add(upload);
                    }
                }
                if(uploads.size()==0)
                {
                    // Hiding the progress dialog.
                    progressDialog.dismiss();
                    noitem.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "No Data",Toast.LENGTH_SHORT).show();
                }
                else {
                    //creating adapter
                    adapter = new TicketAdapter(getActivity(), uploads);

                    //adding adapter to recyclerview
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated( view,savedInstanceState );
        getActivity().setTitle( R.string.menu_view_ticket );
    }
}
