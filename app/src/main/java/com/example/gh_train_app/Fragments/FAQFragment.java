package com.example.gh_train_app.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.gh_train_app.OthersFiles.Tools;
import com.example.gh_train_app.OthersFiles.ViewAnimation;
import com.example.gh_train_app.R;

public class FAQFragment extends Fragment {
    NestedScrollView nested_scroll_view;
    Button question1_hide_text,question2_hide_text,question3_hide_text;
    ImageButton question1_toggle_text,question2_toggle_text,question3_toggle_text;
    View question1_expand_text,question2_expand_text,question3_expand_text;
    
    
    public FAQFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_f_a_q, container, false);

        //    hide text
        question1_hide_text = root.findViewById(R.id.question1_hide_text);
        question2_hide_text = root.findViewById(R.id.question2_hide_text);
        question3_hide_text = root.findViewById(R.id.question3_hide_text);

        //    toggle text
        question1_toggle_text = root.findViewById(R.id.question1_toggle_text);
        question2_toggle_text = root.findViewById(R.id.question2_toggle_text);
        question3_toggle_text = root.findViewById(R.id.question3_toggle_text);


        //    expand text
        question1_expand_text = root.findViewById(R.id.question1_expand_text);
        question2_expand_text = root.findViewById(R.id.question2_expand_text);
        question3_expand_text = root.findViewById(R.id.question3_expand_text);

        //visibitities
        nested_scroll_view = root.findViewById(R.id.nested_scroll_view);
        question1_expand_text.setVisibility(View.GONE);
        question2_expand_text.setVisibility(View.GONE);
        question3_expand_text.setVisibility(View.GONE);


        question1_toggle_text.setOnClickListener(view -> question1SectionText(question1_toggle_text));
        question2_toggle_text.setOnClickListener(view -> question2SectionText(question2_toggle_text));
        question3_toggle_text.setOnClickListener(view -> question3SectionText(question3_toggle_text));

        question1_hide_text.setOnClickListener(view -> question1SectionText(question1_toggle_text));
        question2_hide_text.setOnClickListener(view -> question2SectionText(question2_toggle_text));
        question3_hide_text.setOnClickListener(view -> question3SectionText(question3_toggle_text));

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated( view,savedInstanceState );
        getActivity().setTitle( R.string.menu_faq );
    }

    private void question1SectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(question1_expand_text, () -> Tools.nestedScrollTo(nested_scroll_view, question1_expand_text));
        } else {
            ViewAnimation.collapse(question1_expand_text);
        }
    }

    private void question2SectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(question2_expand_text, () -> Tools.nestedScrollTo(nested_scroll_view, question2_expand_text));
        } else {
            ViewAnimation.collapse(question2_expand_text);
        }
    }

    private void question3SectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(question3_expand_text, () -> Tools.nestedScrollTo(nested_scroll_view, question3_expand_text));
        } else {
            ViewAnimation.collapse(question3_expand_text);
        }
    }



    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }


}