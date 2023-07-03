package com.example.gh_train_app.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.gh_train_app.Fragments.BookTicketFragment;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.Book;
import com.example.gh_train_app.models.Train;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.TrainViewHolder> implements Filterable {
    Context mContext;
    List<Train> mData;
    List<Train> mDataFiltered;
    ProgressDialog progressDialog;

    public TrainAdapter(Context mContext, List<Train> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataFiltered = mData;
    }

    @NonNull
    @Override
    public TrainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.train_card, viewGroup, false);
        return new TrainViewHolder(layout);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull final TrainViewHolder trainViewHolder, final int position) {
        // bind data here
        // we apply animation to views here
        // first lets create an animation for user photo
        trainViewHolder.trainImage.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));
        trainViewHolder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
        trainViewHolder.train_name.setText(mDataFiltered.get(position).getTrain_name());
        trainViewHolder.train_number.setText(mDataFiltered.get(position).getTrain_number()+"---type ("+mDataFiltered.get(position).getType()+")");
        trainViewHolder.train_station.setText(mDataFiltered.get(position).getStation());
        trainViewHolder.seatcapacity.setText("Available seats: "+(mDataFiltered.get(position).getAvail_seats()-mDataFiltered.get(position).getReal_avail_seats()));
        trainViewHolder.train_workingdays.setText("Working days include: "+mDataFiltered.get(position).getWorking_day());
        trainViewHolder.train_fare.setText("GH₵"+mDataFiltered.get(position).getPrice());
        trainViewHolder.train_description.setText(mDataFiltered.get(position).getDescription());
        trainViewHolder.open_time.setText(mDataFiltered.get(position).getArrival_time());
        trainViewHolder.closetime.setText(mDataFiltered.get(position).getDepart_time());
        trainViewHolder.source.setText(mDataFiltered.get(position).getSource());
        trainViewHolder.destination.setText(mDataFiltered.get(position).getDest());
        Glide.with(mContext).load(mDataFiltered.get(position).getImageUrl()).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(trainViewHolder.trainImage);
        trainViewHolder.book.setOnClickListener(v -> {

            progressDialog = new ProgressDialog(mContext,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Checking availability...");
            progressDialog.show();
            new Handler().postDelayed(() -> {
                Query query = FirebaseDatabase.getInstance().getReference("Trains")
                        .orderByChild("train_id")
                        .equalTo(mDataFiltered.get(position).getTrain_id());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Train train = snapshot.getValue(Train.class);
                            int total_capacity = (train.getAvail_seats());
                            int occupie_seats = (train.getReal_avail_seats());
                            if (occupie_seats>=total_capacity){
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "Sorry seats are full. Try a different train", Toast.LENGTH_SHORT).show();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "Seats available", Toast.LENGTH_SHORT).show();
        BookTicketFragment fragment = new BookTicketFragment();
        final Bundle args = new Bundle();
        args.putString("train_id",mDataFiltered.get(position).getTrain_id());
        args.putString("train_name",mDataFiltered.get(position).getTrain_name());
        args.putString("train_number",mDataFiltered.get(position).getTrain_number());
        args.putString("train_type",mDataFiltered.get(position).getType());
        args.putString("train_source",mDataFiltered.get(position).getSource());
        args.putString("train_destination",mDataFiltered.get(position).getDest());
        args.putString("train_arrival",mDataFiltered.get(position).getArrival_time());
        args.putString("train_departure",mDataFiltered.get(position).getDepart_time());
        args.putString("train_price",mDataFiltered.get(position).getPrice());
        fragment.setArguments(args);

        FragmentTransaction transaction =((FragmentActivity)mContext).getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.flContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }, 4000);
        });
    }


    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String Key = constraint.toString();
                if (Key.isEmpty()) {

                    mDataFiltered = mData;

                } else {
                    List<Train> lstFiltered = new ArrayList<>();
                    for (Train row : mData) {

                        if (row.getTrain_name().toLowerCase().contains(Key.toLowerCase())) {
                            lstFiltered.add(row);
                        }

                    }

                    mDataFiltered = lstFiltered;

                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                mDataFiltered = (List<Train>) results.values;
                notifyDataSetChanged();

            }
        };
    }

    public class TrainViewHolder extends RecyclerView.ViewHolder {


        TextView train_name, train_number, train_station,seatcapacity,train_fare,destination,train_description,open_time,closetime,train_workingdays,source;
        LinearLayout container;
        ImageView trainImage;
        Button book;

        public TrainViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            trainImage = itemView.findViewById(R.id.trainImage);
            train_name = itemView.findViewById(R.id.train_name);
            train_number = itemView.findViewById(R.id.train_number);
            train_station = itemView.findViewById(R.id.train_station);
            seatcapacity = itemView.findViewById(R.id.seatcapacity);
            train_fare = itemView.findViewById(R.id.train_fare);
            train_description = itemView.findViewById(R.id.train_description);
            open_time = itemView.findViewById(R.id.open_time);
            closetime = itemView.findViewById(R.id.closetime);
            train_workingdays = itemView.findViewById(R.id.train_workingdays);
            book = itemView.findViewById(R.id.book);
            source = itemView.findViewById(R.id.source);
            destination = itemView.findViewById(R.id.destination);
        }
    }



}