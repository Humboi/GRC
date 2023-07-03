package com.example.gh_train_app.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

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
import com.example.gh_train_app.models.Train;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.jfenn.timedatepickers.dialogs.PickerDialog;
import me.jfenn.timedatepickers.dialogs.TimeSheetPickerDialog;
import me.jfenn.timedatepickers.views.LinearTimePickerView;

import static android.app.Activity.RESULT_OK;

public class AddTrainFragment extends Fragment implements View.OnClickListener {
    // Folder path for Firebase Storage.
    String Storage_Path = "Train_images/";
    int Image_Request_Code = 7;
    // Root Database Name for Firebase Database.
    String Database_Path = "Trains";
    Uri FilePathUri;
    EditText trainname, trainnumber, trainsource, traindestination, trainarrival, traindeparture, traindescription, traincapacity, seatalreadybooked, workingdays, price;
    String train_name, train_number, train_type, train_station, train_source, train_dest, train_depart_time,totalcapacity,seatsbooed,
            train_arrival_time, working_days, unitprice, train_descript;
//    int available_Steats,alreadybookedseats;
    Button addtrain,btnupload;
    ImageView selected_image;
    Spinner select_train_type, select_train_station;
    ArrayAdapter<CharSequence> select_train_type_adapter;
    DatabaseReference mdb,mdbtrainstation;
    StorageReference trainstorageReference;
    ProgressDialog progressDialog;
    int flag = 0;
    private String format = "";

    public AddTrainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_train, container, false);
        trainname = root.findViewById(R.id.trainnname);
        btnupload = root.findViewById(R.id.upploadtrainImage);
        selected_image = root.findViewById(R.id.trainpic);
        trainnumber = root.findViewById(R.id.trainnumber);
        select_train_type = root.findViewById(R.id.traintype);
        select_train_station = root.findViewById(R.id.trainstation);
        trainsource = root.findViewById(R.id.trainsource);
        traindestination = root.findViewById(R.id.trainndest);
        trainarrival = root.findViewById(R.id.arrivaltime);
        traindeparture = root.findViewById(R.id.departuretime);
        traindescription = root.findViewById(R.id.traindescription);
        traincapacity = root.findViewById(R.id.traincapacity);
        workingdays = root.findViewById(R.id.trainworkingdays);
        price = root.findViewById(R.id.price);
        seatalreadybooked = root.findViewById(R.id.seatalreadybooked);
        seatalreadybooked.setText("0");
        addtrain = root.findViewById(R.id.addtrain);
        select_train_type_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.train_type_arrays, android.R.layout.simple_spinner_item);
        select_train_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_train_type.setAdapter(select_train_type_adapter);
        select_train_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                train_type = select_train_type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        mdb = FirebaseDatabase.getInstance().getReference(Database_Path);
        mdbtrainstation = FirebaseDatabase.getInstance().getReference().child("TrainStations");
        trainstorageReference = FirebaseStorage.getInstance().getReference();
        trainarrival.setOnClickListener(this);
        traindeparture.setOnClickListener(this);
        addtrain.setOnClickListener(this);
        btnupload.setOnClickListener(this);

        Query query = mdbtrainstation.orderByChild("station_name");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> titleList = new ArrayList<String>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    String train_station_names = dataSnapshot1.child("station_name").getValue(String.class);
                    titleList.add(train_station_names);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, titleList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_train_station.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        select_train_station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                train_station = select_train_station.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrivaltime:
                new TimeSheetPickerDialog(getActivity())
                        .setListener(new PickerDialog.OnSelectedListener<LinearTimePickerView>() {
                            @Override
                            public void onSelect(PickerDialog<LinearTimePickerView> dialog, LinearTimePickerView view) {
                                showTime(view.getHourOfDay(), view.getMinute());
                                trainarrival.setText(new StringBuilder().append(view.getHourOfDay()).append(" : ").append(view.getMinute())
                                        .append(" ").append(format));
                                Toast.makeText(getActivity(), trainarrival.getText().toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel(PickerDialog<LinearTimePickerView> dialog) {
                            }
                        })
                        .show();
                break;
            case R.id.departuretime:
                new TimeSheetPickerDialog(getActivity())
                        .setListener(new PickerDialog.OnSelectedListener<LinearTimePickerView>() {
                            @Override
                            public void onSelect(PickerDialog<LinearTimePickerView> dialog, LinearTimePickerView view) {
                                showTime(view.getHourOfDay(), view.getMinute());
                                traindeparture.setText(new StringBuilder().append(view.getHourOfDay()).append(" : ").append(view.getMinute())
                                        .append(" ").append(format));
                                Toast.makeText(getActivity(), traindeparture.getText().toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel(PickerDialog<LinearTimePickerView> dialog) {
                            }
                        })
                        .show();
                break;
            case R.id.addtrain:
                addtrain();
            break;
            case R.id.upploadtrainImage:
                openG();
                break;
        }
    }

    public void openG(){
        // Creating intent.
        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

    }

    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

    }

    public void addtrain() {
        train_name = trainname.getText().toString();
        train_number = trainnumber.getText().toString();
        train_source = trainsource.getText().toString();
        train_dest = traindestination.getText().toString();
        train_depart_time = traindeparture.getText().toString();
        train_arrival_time = trainarrival.getText().toString();
        totalcapacity = traincapacity.getText().toString();
        working_days = workingdays.getText().toString();
        unitprice = price.getText().toString();
        seatsbooed=seatalreadybooked.getText().toString();
        train_descript = traindescription.getText().toString();
        flag = 0;
        if (trainname.getText().toString().trim().length() == 0) {
            trainname.setError("Train name cannot be Empty");
            trainname.requestFocus();
            flag = 1;
        }
        if (trainnumber.getText().toString().trim().length() == 0) {
            trainnumber.setError("Train number cannot be Empty");
            trainnumber.requestFocus();
            flag = 1;
        }
        if (trainsource.getText().toString().trim().length() == 0) {
            trainsource.setError("Source cannot be Empty");
            trainsource.requestFocus();
            flag = 1;
        }
        if (traindestination.getText().toString().trim().length() == 0) {
            traindestination.setError("Destination cannot be Empty");
            traindestination.requestFocus();
            flag = 1;
        }
        if (traindeparture.getText().toString().trim().length() == 0) {
            traindeparture.setError("Depature time cannot be Empty");
            traindeparture.requestFocus();
            flag = 1;
        }
        if (trainarrival.getText().toString().trim().length() == 0) {
            trainarrival.setError("Arrival time cannot be Empty");
            trainarrival.requestFocus();
            flag = 1;
        }
        if (traincapacity.getText().toString().trim().length() == 0) {
            traincapacity.setError("Seat number cannot be Empty");
            traincapacity.requestFocus();
            flag = 1;
        }
        if (workingdays.getText().toString().trim().length() == 0) {
            workingdays.setError("Working days cannot be Empty");
            workingdays.requestFocus();
            flag = 1;
        }
        if (price.getText().toString().trim().length() == 0) {
            price.setError("Price cannot be Empty");
            price.requestFocus();
            flag = 1;
        }
        if (traindescription.getText().toString().trim().length() == 0) {
            traindescription.setError("Description cannot be Empty");
            traindescription.requestFocus();
            flag = 1;
        }
        if (flag == 0) {
            trainname.setError(null);
            trainnumber.setError(null);
            trainsource.setError(null);
            traindestination.setError(null);
            traindeparture.setError(null);
            trainarrival.setError(null);
            traincapacity.setError(null);
            workingdays.setError(null);
            traindescription.setError(null);
            price.setError(null);
            uploadImageFile();
        }
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

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    private void uploadImageFile() {
        //checking if file is available
        if (FilePathUri != null) {
            //displaying progress dialog while image is uploading
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading Train Details...");
            progressDialog.show();

            //getting the storage reference
            final StorageReference sRef = trainstorageReference.child("Train_photos/" + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            //adding the file to reference
            sRef.putFile(FilePathUri)
                    .addOnSuccessListener(taskSnapshot -> sRef.getDownloadUrl().addOnSuccessListener(uri -> {
                      String imglink=(uri.toString());
                        //adding an upload to firebase database
                        String train_Id = mdb.push().getKey();
                        //creating the upload object to store uploaded image details
                        Train train = new Train(train_Id,train_name,train_number,train_station,train_type,train_source,train_dest,train_arrival_time,train_depart_time,Integer.valueOf(totalcapacity),Integer.valueOf(seatsbooed),train_descript,working_days,unitprice, imglink);
                        mdb.child(train_Id).setValue(train);
                        //dismissing the progress dialog
                        progressDialog.dismiss();
                        //displaying success toast
                        Toast.makeText(getActivity(), "Train Details Uploaded Successfully", Toast.LENGTH_LONG).show();
                        //navigate bacjk to home page
                        Fragment frag = new HomeFragment();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.flContent, frag);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.addToBackStack(null);
                        ft.commit();
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
        getActivity().setTitle( R.string.menu_add_train );
    }

}


