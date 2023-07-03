package com.example.gh_train_app.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gh_train_app.R;


public class OpenDetailsFragment extends Fragment {
     ImageView newspreviewpic;
     TextView previewcategory,previewdesc,previewdate,previewtitle;
    String cat,tit,des,dat,img;
    public OpenDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_open_details, container, false);
        Bundle args = getArguments();
        cat=args.getString("news_category");
        tit=args.getString("news_title");
        des=args.getString("new_description");
        dat=args.getString("news_date");
        img=args.getString("news_image");
        newspreviewpic = root.findViewById(R.id.newspreviewpic);
        Glide.with(getActivity())
                .load(img)
                .into(newspreviewpic);
        previewtitle = root.findViewById(R.id.previewtitle);
        previewtitle.setText(tit);
        previewcategory = root.findViewById(R.id.previewcategory);
        previewcategory.setText("Category :"+cat);
        previewdesc = root.findViewById(R.id.previewdesc);
        previewdesc.setText(des);
        previewdate = root.findViewById(R.id.previewdate);
        previewdate.setText("Date :"+dat);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated( view,savedInstanceState );
        getActivity().setTitle( cat+" NEWS" );
    }
}
