package com.example.gh_train_app.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gh_train_app.R;
import com.example.gh_train_app.models.News;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class DashboardItemFragment extends Fragment implements View.OnClickListener {
    Spinner select_news_category;
    ArrayAdapter<CharSequence> select_news_category_adapter;
    EditText title, newsdescription;
    Button addnews, btnupload;
    ImageView selected_image;
    Uri FilePathUri;
    int Image_Request_Code = 7;
    // Folder path for Firebase Storage.
    String Storage_Path = "News_images/";
    String Database_Path = "News";
    String news_category, news_title, news_description, news_date;
    DatabaseReference mdb;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    int flag = 0;

    public DashboardItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dashboard_item, container, false);
        selected_image = root.findViewById(R.id.newsimage);
        btnupload = root.findViewById(R.id.chooseImage);
        select_news_category = root.findViewById(R.id.category);
        title = root.findViewById(R.id.title);
        newsdescription = root.findViewById(R.id.newsdescription);
        addnews = root.findViewById(R.id.addnews);
        select_news_category_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.category_arrays, android.R.layout.simple_spinner_item);
        select_news_category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_news_category.setAdapter(select_news_category_adapter);
        select_news_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                news_category = select_news_category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("EEE, dd MMM yyyy, hh:mm aa");
        news_date = dateformat.format(c.getTime());
        mdb = FirebaseDatabase.getInstance().getReference(Database_Path);
        storageReference = FirebaseStorage.getInstance().getReference();
        addnews.setOnClickListener(this);
        btnupload.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addnews:
                addNews();
                break;
            case R.id.chooseImage:
                openG();
                break;
        }
    }

    public void openG() {
        // Creating intent.
        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                selected_image.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                btnupload.setText("Image Selected");

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void addNews() {
        news_title = title.getText().toString();
        news_description = newsdescription.getText().toString();
        flag = 0;
        if (title.getText().toString().trim().length() == 0) {
            title.setError("Title cannot be Empty");
            title.requestFocus();
            flag = 1;
        }
        if (newsdescription.getText().toString().trim().length() == 0) {
            newsdescription.setError("Description cannot be Empty");
            newsdescription.requestFocus();
            flag = 1;
        }
        if (flag == 0) {
            title.setError(null);
            newsdescription.setError(null);
            uploadImageFile();
        }
    }

    private void uploadImageFile() {
        //checking if file is available
        if (FilePathUri != null) {
            //displaying progress dialog while image is uploading
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading News...");
            progressDialog.show();

            //getting the storage reference
            final StorageReference sRef = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            //adding the file to reference
            sRef.putFile(FilePathUri)
                    .addOnSuccessListener(taskSnapshot -> sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imglink = (uri.toString());
                            //adding an upload to firebase database
                            String news_Id = mdb.push().getKey();
                            //creating the upload object to store uploaded image details
                            News train = new News(news_Id, news_category, news_title, news_description, news_date, imglink);
                            mdb.child(news_Id).setValue(train);
                            //dismissing the progress dialog
                            progressDialog.dismiss();
                            //displaying success toast
                            Toast.makeText(getActivity(), "News Uploaded Successfully", Toast.LENGTH_LONG).show();
                            //navigate bacjk to home page
                            Fragment frag = new HomeFragment();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.flContent, frag);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    }))
                    .addOnFailureListener(exception -> {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    });
        } else {
            //display an error if no file is selected
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated( view,savedInstanceState );
        getActivity().setTitle( R.string.menu_add_dashborad_item );
    }

}
