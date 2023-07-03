package com.example.gh_train_app.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gh_train_app.R;
import com.example.gh_train_app.models.Train;

import java.util.List;

public class FareAdapter extends RecyclerView.Adapter<FareAdapter.FareViewHolder> {

    private Context mCtx;
    private List<Train> trainfareList;
    ProgressDialog progressDialog;

    public FareAdapter(Context mCtx, List<Train> trainfareList) {
        this.mCtx = mCtx;
        this.trainfareList = trainfareList;
    }

    @NonNull
    @Override
    public FareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.fare_enquiry_card, parent, false);
        return new FareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FareViewHolder holder, int position) {
        Train train = trainfareList.get(position);
        holder.trainname.setText(train.getTrain_name()+"----"+"("+train.getTrain_number()+")");
        holder.trainstation.setText( train.getStation());
        holder.source.setText(train.getSource());
        holder.destination.setText(train.getDest());
        holder.price.setText("GH₵"+train.getPrice());
        holder.itemView.setOnClickListener(v->{

        });
    }

    @Override
    public int getItemCount() {
        return trainfareList.size();
    }

    class FareViewHolder extends RecyclerView.ViewHolder {

        TextView trainname,trainstation,source,destination,price;

        public FareViewHolder(@NonNull View itemView) {
            super(itemView);

            trainname = (TextView) itemView.findViewById( R.id.trainname );
            trainstation = (TextView) itemView.findViewById( R.id.trainstation );
            source = (TextView) itemView.findViewById( R.id.source );
            destination = (TextView) itemView.findViewById( R.id.destination );
            price = (TextView) itemView.findViewById( R.id.price );
        }
    }
}
