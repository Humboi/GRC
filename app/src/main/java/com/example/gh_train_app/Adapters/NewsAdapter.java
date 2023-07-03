package com.example.gh_train_app.Adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.News;

import java.util.List;

public  class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.RecyclerViewHolder>{
    private Context mContext;
    private List<News> news;
    private OnItemClickListener mListener;

    public NewsAdapter(Context context, List<News> uploads) {
        mContext = context;
        news = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.news_card, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        News newsdetails = news.get(position);
        holder.categoryTextView.setText(newsdetails.getNews_category());
        holder.titleTextView.setText(newsdetails.getNews_title());
        holder.descriptionTextView.setText(newsdetails.getNews_description());
        holder.dateTextView.setText(newsdetails.getNews_date());
        Glide.with(mContext)
                .load(newsdetails.getImageUrl())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.newsImageView);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView categoryTextView,titleTextView,descriptionTextView,dateTextView;
        public ImageView newsImageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            categoryTextView =itemView.findViewById ( R.id.category );
            titleTextView =itemView.findViewById ( R.id.title );
            descriptionTextView = itemView.findViewById(R.id.description);
            dateTextView = itemView.findViewById(R.id.date);
            newsImageView = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem showItem = menu.add( Menu.NONE, 1, 1, "Show");
            MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");

            showItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onShowItemClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteItemClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onShowItemClick(int position);
        void onDeleteItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}