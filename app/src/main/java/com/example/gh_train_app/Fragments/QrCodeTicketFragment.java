package com.example.gh_train_app.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.Ticket;
import com.example.gh_train_app.models.Train;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;


public class QrCodeTicketFragment extends Fragment {
    public final static int QRcodeWidth = 500;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    ImageView qrcode;
    CircularImageView imageView;
    TextView ticketID, date, passenger1, passenger2, passenger3, source, destination, price, arrivaltime, personbooked, departuretime, booking_status;
    String imageLink,ticket_ID, ticket_date, person_booked, passenger_1, passenger_2, passenger_3, train_source, train_destination,
            train_price, arrival_time, departure_time, bookingstatus, train_name, train_number;
    Button save;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    StorageReference mStorage, sRef;
    DatabaseReference mDatabse;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    public QrCodeTicketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_qr_code_ticket, container, false);
        mDatabse = FirebaseDatabase.getInstance().getReference().child("Tickets");
        mStorage = FirebaseStorage.getInstance().getReference();
        sRef = mStorage.child("Ticket_images/" + System.currentTimeMillis());
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        Bundle qrargs = getArguments();
        imageLink = qrargs.getString("image_link");
        ticket_ID = qrargs.getString("ticket_id");
        ticket_date = qrargs.getString("ticket_date");
        passenger_1 = qrargs.getString("ticket_png1");
        passenger_2 = qrargs.getString("ticket_png2");
        passenger_3 = qrargs.getString("ticket_png3");
        train_source = qrargs.getString("ticket_source");
        train_destination = qrargs.getString("ticket_dest");
        arrival_time = qrargs.getString("ticket_arrival");
        departure_time = qrargs.getString("ticket_departure");
        bookingstatus = qrargs.getString("ticket_status");
        train_price = qrargs.getString("ticket_fare");
        person_booked = qrargs.getString("booked_person");
        train_name = qrargs.getString("ticket_nam");
        train_number = qrargs.getString("ticket_num");
        qrcode = root.findViewById(R.id.qrcode);
        imageView = root.findViewById(R.id.image);
        Glide.with(getActivity())
                .load(imageLink)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
        ticketID = root.findViewById(R.id.ticketID);
        ticketID.setText("Ticked ID: " + ticket_ID);
        date = root.findViewById(R.id.date);
        date.setText(ticket_date);
        passenger1 = root.findViewById(R.id.passenger1);
        passenger1.setText(passenger_1);
        passenger2 = root.findViewById(R.id.passenger2);
        passenger2.setText(passenger_2);
        passenger3 = root.findViewById(R.id.passenger3);
        passenger3.setText(passenger_3);
        source = root.findViewById(R.id.source);
        source.setText(train_source);
        destination = root.findViewById(R.id.destination);
        destination.setText(train_destination);
        price = root.findViewById(R.id.price);
        price.setText(train_price);
        personbooked = root.findViewById(R.id.personbooked);
        personbooked.setText(person_booked);
        departuretime = root.findViewById(R.id.departuretime);
        departuretime.setText(departure_time);
        arrivaltime = root.findViewById(R.id.arrivaltime);
        arrivaltime.setText(arrival_time);
        booking_status = root.findViewById(R.id.booking_status);
        booking_status.setText(bookingstatus);
        save = root.findViewById(R.id.saveqrcode);
        try {
            bitmap = TextToImageEncode(
                    "Ticket ID: " + ticket_ID + System.lineSeparator() +
                            "Train name " + train_name + System.lineSeparator() +
                            "Train number: " + train_number + System.lineSeparator() +
                            "Ticket date: " + ticket_date + System.lineSeparator() +
                            "Person Booked: " + person_booked + System.lineSeparator() +
                            "Co-passenger 1: " + passenger_1 + System.lineSeparator() +
                            "Co-passenger 2: " + passenger_2 + System.lineSeparator() +
                            "Co-passenger 3: " + passenger_3 + System.lineSeparator() +
                            "Train Source: " + train_source + System.lineSeparator() +
                            "Train Destination: " + train_destination + System.lineSeparator() +
                            "Train Fare: " + train_price + System.lineSeparator() +
                            "Arrival Time: " + arrival_time + System.lineSeparator() +
                            "Departure Time: " + departure_time + System.lineSeparator() +
                            "Payment Status: " + bookingstatus);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        qrcode.setImageBitmap(bitmap);

        save.setOnClickListener(v -> {
           saveQrCode();
        });

        return root;
    }

    public void saveQrCode(){
        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving Ticket...");
        progressDialog.show();

        qrcode.setDrawingCacheEnabled(true);
        qrcode.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = sRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            progressDialog.dismiss();
            //displaying success toast
            Toast.makeText(getActivity(), "Failed to Save Ticket", Toast.LENGTH_LONG).show();
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            sRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imglink = (uri.toString());
                //adding an upload to firebase database
                String train_Id = mDatabse.push().getKey();
                String user_id = currentUser.getUid();
                //creating the upload object to store uploaded image details
                Ticket train = new Ticket(imageLink,user_id, ticket_ID, ticket_date, person_booked, passenger_1, passenger_2, passenger_3, train_source, train_destination, train_price, arrival_time, departure_time, bookingstatus, imglink);
                mDatabse.child(train_Id).setValue(train);
                //dismissing the progress dialog
                progressDialog.dismiss();
                //displaying success toast
                Toast.makeText(getActivity(), "Ticket Saved Successfully", Toast.LENGTH_LONG).show();
                //navigate bacjk to home page
                Fragment frag = new HomeFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.flContent, frag);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            });
        });
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor) : getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated( view,savedInstanceState );
        getActivity().setTitle( R.string.menu_qrcode );
    }

    @Override
    public void onDetach() {
        super.onDetach();
       saveQrCode();
    }

}
