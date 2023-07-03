package com.example.gh_train_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gh_train_app.Activities.PlaceDetailsActivity;
import com.example.gh_train_app.Fragments.LocationFragment;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.MyPlaces;
import com.example.gh_train_app.models.Results;


public class PlaceRecyclerViewAdapter extends RecyclerView.Adapter<PlaceRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private MyPlaces myPlaces;
    private double lat, lng,distanceinKM;

    public PlaceRecyclerViewAdapter(Context context, MyPlaces myPlaces, double lat, double lng) {
        this.context = context;
        this.myPlaces = myPlaces;
        this.lat = lat;
        this.lng = lng;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_place_single, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Results results = myPlaces.getResults().get(position);
        holder.bind(results);
        holder.linearLayoutDetails.setOnClickListener(view -> {
//                distanceinKM = distance(LocationFragment.lat,LocationFragment.lng,lat,lng,"KM");

            Intent intent = new Intent(context, PlaceDetailsActivity.class);
            intent.putExtra("result", results);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
//                intent.putExtra("disKM",distanceinKM);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return myPlaces.getResults().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, address;
        public LinearLayout linearLayoutDetails;
        public ImageView placeIV;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textViewPlaceName);
            address = view.findViewById(R.id.textViewAddress);
            linearLayoutDetails = view.findViewById(R.id.linearLayoutDetails);
            placeIV = view.findViewById(R.id.placeImageView);
        }

        public void bind(Results results) {
            name.setText(results.getName());
            address.setText(results.getVicinity());
        }
    }

//    public double distance(double lat1, double lon1, double lat2, double lon2,String unit) {
//        if ((lat1 == lat2) && (lon1 == lon2)) {
//            return 0;
//        }
//        else {
//            double theta = lon1 - lon2;
//            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
//            dist = Math.acos(dist);
//            dist = Math.toDegrees(dist);
//            dist = dist * 60 * 1.1515;
//            if (unit.equals("K")) {
//                dist = dist * 1.609344;
//            } else if (unit.equals("N")) {
//                dist = dist * 0.8684;
//            }
//            return (dist);
//        }
//    }

}
