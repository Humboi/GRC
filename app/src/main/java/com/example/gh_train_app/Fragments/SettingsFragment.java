package com.example.gh_train_app.Fragments;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gh_train_app.BuildConfig;
import com.example.gh_train_app.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class SettingsFragment extends Fragment implements View.OnClickListener {
    LinearLayout invite,selectLanguage, termsConditions,faq,privacy_policy;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        invite = root.findViewById(R.id.inviteFriends);
        selectLanguage = root.findViewById(R.id.selectLanguage);
        termsConditions = root.findViewById(R.id.terms);
        faq = root.findViewById(R.id.faq);
        privacy_policy = root.findViewById(R.id.privacy_policy);
        invite.setOnClickListener(this);
        selectLanguage.setOnClickListener(this);
        termsConditions.setOnClickListener(this);
        faq.setOnClickListener(this);
        privacy_policy.setOnClickListener(this);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.menu_settings);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inviteFriends:
                inviteFriends();
                break;
            case R.id.selectLanguage:
                chooseLanguage();
                break;
            case R.id.terms:
                replaceFragment(new TermsConditionsFragment());
                break;
            case R.id.faq:
                replaceFragment(new FAQFragment());
                break;
            case R.id.privacy_policy:
                replaceFragment(new PivacyPolicyFragment());
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void inviteFriends() {
        ApplicationInfo app = getActivity().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("*/*");

        // Append file and send Intent
        File originalApk = new File(filePath);

        try {
            //Make new directory in new location=
            File tempFile = new File(getActivity().getExternalCacheDir() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application's name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + getString(app.labelRes).replace(" ", "").toLowerCase() + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            //Open share dialog
//          intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            Uri photoURI = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", tempFile);
//          intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            startActivity(Intent.createChooser(intent, "Share app via"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooseLanguage(){
        Toast.makeText(getActivity(),"Still in progress",Toast.LENGTH_LONG).show();
    }

}
