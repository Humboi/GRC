package com.example.gh_train_app.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.Ticket;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private Context context;
    private List<Ticket> uploads;

    public TicketAdapter(Context context, List<Ticket> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticketcard, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ticket upload = uploads.get(position);

        holder.textViewName.setText(upload.getDate());
        holder.textViewStatus.setText(upload.getBooking_status());

        Glide.with(context).load(upload.getQrcode()).into(holder.imageView);
        if (upload.getImageUrl()!= null) {
            Glide.with(context).load(upload.getImageUrl()).into(holder.profile);
        }else {
            holder.profile.setVisibility(View.GONE);
        }
        holder.container.setOnClickListener(onClickListener(position));
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    private View.OnClickListener onClickListener(final int position) {
        return (View.OnClickListener) v -> {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.ticketcardzoom);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setTitle("QR Code Details ");
            dialog.setCancelable(true); // dismiss when touching outside Dialog

            // set the custom dialog components - texts and image
            TextView status = (TextView) dialog.findViewById(R.id.status);
            TextView date = (TextView) dialog.findViewById(R.id.date);
            CircularImageView profile_image = (CircularImageView) dialog.findViewById(R.id.profile_image);
            ImageView icon = (ImageView) dialog.findViewById(R.id.image);
            status.setText(uploads.get(position).getBooking_status());
            date.setText(uploads.get(position).getDate());
            if (uploads.get(position).getImageUrl() != null) {
                Glide.with(context).load(uploads.get(position).getImageUrl()).crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profile_image);
            }else {
                profile_image.setVisibility(View.GONE);
            }

            Glide.with(context).load(uploads.get(position).getQrcode()).crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(icon);
            dialog.show();
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName,textViewStatus;
        public ImageView imageView;
        public CircularImageView profile;
        public View container;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.name);
            profile = itemView.findViewById(R.id.profile);
            textViewStatus = itemView.findViewById(R.id.status);
            imageView =  itemView.findViewById(R.id.image);
            container = itemView.findViewById(R.id.card_view);
        }
    }
}
