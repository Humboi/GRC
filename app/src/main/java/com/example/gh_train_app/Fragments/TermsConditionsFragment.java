package com.example.gh_train_app.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gh_train_app.R;

public class TermsConditionsFragment extends Fragment {
    TextView conditions;

    public TermsConditionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_terms_conditions, container, false);
        conditions = root.findViewById(R.id.conditions);
        conditions.setText(Html.fromHtml(getString(R.string.terms_doc)));
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated( view,savedInstanceState );
        getActivity().setTitle( R.string.termsandcondition );
    }
}