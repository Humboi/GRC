package com.example.gh_train_app.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class ProfileFragment extends Fragment {
    TextView email,phonenumber,username,fullname,address;
    Button deleteaccount,buttonEmail,buttonPhoneNo,buttonUsername,buttonAddress, buttonChangePhoto;
    ImageView profileImage;
    FirebaseUser currentUser;
    DatabaseReference rootRef;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    int GALLERY_REQUEST_CODE = 1234;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        email = root.findViewById(R.id.profileemail);
        phonenumber = root.findViewById(R.id.profilephonenumber);
        username = root.findViewById(R.id.profileusername);
        fullname = root.findViewById(R.id.profilefullname);
        address = root.findViewById(R.id.profileaddress);
        deleteaccount = root.findViewById(R.id.deleteaccount);
        profileImage = root.findViewById(R.id.userprofile);
        buttonEmail = root.findViewById(R.id.buttonEmail);
        buttonPhoneNo = root.findViewById(R.id.buttonPhoneNo);
        buttonUsername = root.findViewById(R.id.buttonUsername);
        buttonAddress = root.findViewById(R.id.buttonAddress);
        buttonChangePhoto = root.findViewById(R.id.changePhoto);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("users_photos");
        fullname.setText(currentUser.getDisplayName());
        email.setText(currentUser.getEmail());
        Glide.with( getActivity() ).load( currentUser.getPhotoUrl() ).into( profileImage );


        Query query = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("id")
                .equalTo(currentUser.getUid());
        query.addListenerForSingleValueEvent(valueEventListener);

        buttonEmail.setOnClickListener(v -> {
            LayoutInflater inflater1 = getLayoutInflater();
            View alertLayout = inflater1.inflate(R.layout.layout_custom_dialog_edit, null);
            final EditText newAddedItem = alertLayout.findViewById(R.id.newitem);
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("New Email");
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            alert.setNegativeButton("Cancel", (dialog, which) -> {
            });
            alert.setPositiveButton("OK", (dialog, which) -> {
                progressDialog = new ProgressDialog(getActivity(),
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Updating Email...");
                progressDialog.show();
                new Handler().postDelayed(() -> {
                    final String latest = newAddedItem.getText().toString();
                    currentUser.updateEmail(latest)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    rootRef.child(currentUser.getUid()).child("email").setValue(latest);
                                    progressDialog.dismiss();
                                    refresh();
                                    Toast.makeText(getContext(), "Email Successfully Updated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(e -> {
                                if (e instanceof FirebaseAuthRecentLoginRequiredException) {
                                    Toast.makeText(getContext(), "Email Update Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                }, 1000);

            });
            AlertDialog dialog = alert.create();
            dialog.show();
        });

        buttonPhoneNo.setOnClickListener(v -> {
            LayoutInflater inflater12 = getLayoutInflater();
            View alertLayout = inflater12.inflate(R.layout.layout_custom_dialog_edit, null);
            final EditText newAddedItem = alertLayout.findViewById(R.id.newitem);
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("New Phone-number");
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            alert.setNegativeButton("Cancel", (dialog, which) -> {
            });
            alert.setPositiveButton("OK", (dialog, which) -> {
                progressDialog = new ProgressDialog(getActivity(),
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Updating Phone#...");
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String latest = newAddedItem.getText().toString();
                        rootRef.child(currentUser.getUid()).child("phonenumber").setValue(latest);
                        progressDialog.dismiss();
                        refresh();
                        Toast.makeText(getContext(), "Phone number updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);

            });
            AlertDialog dialog = alert.create();
            dialog.show();
        });

        buttonUsername.setOnClickListener(v -> {
            LayoutInflater inflater13 = getLayoutInflater();
            View alertLayout = inflater13.inflate(R.layout.layout_custom_dialog_edit, null);
            final EditText newAddedItem = alertLayout.findViewById(R.id.newitem);
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("New Username");
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            alert.setNegativeButton("Cancel", (dialog, which) -> {
            });
            alert.setPositiveButton("OK", (dialog, which) -> {
                progressDialog = new ProgressDialog(getActivity(),
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Updating Username...");
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String latest = newAddedItem.getText().toString();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(latest)
                                .build();

                        currentUser.updateProfile(profileUpdates)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        rootRef.child(currentUser.getUid()).child("fullname").setValue(latest);
                                        progressDialog.dismiss();
                                        refresh();
                                        Toast.makeText(getContext(), "Username updated successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }, 1000);

            });
            AlertDialog dialog = alert.create();
            dialog.show();
        });

        buttonAddress.setOnClickListener(v -> {
            LayoutInflater inflater14 = getLayoutInflater();
            View alertLayout = inflater14.inflate(R.layout.layout_custom_dialog_edit, null);
            final EditText newAddedItem = alertLayout.findViewById(R.id.newitem);
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("New Address");
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            alert.setNegativeButton("Cancel", (dialog, which) -> {
            });
            alert.setPositiveButton("OK", (dialog, which) -> {
                progressDialog = new ProgressDialog(getActivity(),
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Updating Address...");
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String latest = newAddedItem.getText().toString();
                        rootRef.child(currentUser.getUid()).child("address").setValue(latest);
                        progressDialog.dismiss();
                        refresh();
                        Toast.makeText(getContext(), "Address updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);

            });
            AlertDialog dialog = alert.create();
            dialog.show();
        });

        deleteaccount.setOnClickListener(v -> {
            progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Deleting Account...");
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentUser.delete()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Account Deleted successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }, 1000);

        });

        buttonChangePhoto.setOnClickListener(v -> {
            Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGallery,GALLERY_REQUEST_CODE);
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                Uri profile = data.getData();
                profileImage.setImageURI(profile);

                progressDialog = new ProgressDialog(getActivity(),
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Updating Profile Picture...");
                progressDialog.show();
                new Handler().postDelayed(() -> uploadImagetoStorage(profile,currentUser), 1000);
            }
        }
    }

    // update user photo and name
    private void uploadImagetoStorage(Uri pickedImgUri, final FirebaseUser currentUser) {
        // first we need to upload user photo to firebase storage and get url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
            // image uploaded succesfully
            // now we can get our image url
            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                // uri contain user image url
                UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(uri)
                        .build();
                currentUser.updateProfile(profleUpdate)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // user info updated successfully
                                Toast.makeText(getActivity(),"Image Updated Successfully",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
            });
        });
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Register userprofile = snapshot.getValue(Register.class);
                phonenumber.setText(userprofile.getPhonenumber());
                username.setText(userprofile.getUsername());
                address.setText(userprofile.getAddress());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated( view,savedInstanceState );
        getActivity().setTitle( R.string.menu_profile );
    }

   public void refresh(){
       Query query = FirebaseDatabase.getInstance().getReference("Users")
               .orderByChild("id")
               .equalTo(currentUser.getUid());
       query.addListenerForSingleValueEvent(valueEventListener);
   }
}
