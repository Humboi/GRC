package com.example.gh_train_app.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gh_train_app.OthersFiles.ApiClient;
import com.example.gh_train_app.OthersFiles.ApiInterface;
import com.example.gh_train_app.OthersFiles.RetrofitClient;
import com.example.gh_train_app.OthersFiles.Tools;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.BaseResponse;
import com.example.gh_train_app.models.Book;
import com.example.gh_train_app.models.RecieveMoney;
import com.example.gh_train_app.models.Register;
import com.example.gh_train_app.models.SaveUserTransaction;
import com.example.gh_train_app.models.Train;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.gh_train_app.OthersFiles.Tools.APPID;
import static com.example.gh_train_app.OthersFiles.Tools.BOOK_ID;
import static com.example.gh_train_app.OthersFiles.Tools.CLIENTREFERENCE;
import static com.example.gh_train_app.OthersFiles.Tools.DESCRIPTION;
import static com.example.gh_train_app.OthersFiles.Tools.NICKNAME;
import static com.example.gh_train_app.OthersFiles.Tools.PUSH_ID;
import static com.example.gh_train_app.OthersFiles.Tools.RECEIVE_MONEY;
import static com.example.gh_train_app.OthersFiles.Tools.TRANSACTION_ID;
import static com.example.gh_train_app.OthersFiles.Tools.generateClientTransactionId;
import static com.example.gh_train_app.OthersFiles.Tools.showMessage;
import static java.net.HttpURLConnection.HTTP_OK;


public class BookTicketFragment extends Fragment {
    private static final Random random = new Random();
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@#$";
    static int REQUESCODE = 1;
    TextView trainname, trainnumber, traintype, trainfare, trainsource, traindestination, trainarrival, traindeparture, tv_booking_code, username,
            newusername1, newusername2, newusername3, tv_booking_date;
    EditText passenger1, passenger2, passenger3;
    ImageButton bt_copy_code;
    String strtrain_id, strtrainname, strtrainnumber, strtraintype, strtrainfare, strtrainsource, strtraindestination, strtrainarrival, strtraindeparture;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    EditText phonenumber;
    Spinner selected_telco,payment_method;
    int onePerson = 1;
    Uri FilePathUri;
    ImageView selected_image, placeHolder;
    Button bt_add_passenger, bt_book_train, openGallery;
    private ArrayAdapter<CharSequence> select_telco_adapter,payment_method_adapter;
    DatabaseReference mdb,mdb2, rootref;
    StorageReference bookstorageReference;
    String Storage_Path = "Booking_images/";
    ProgressDialog progressDialog;
    String booking_code, booking_status, train_number, train_name, train_type, train_fare,
            train_source, train_destination, train_arrival_time, train_depature_time, person_booked,
            passenger_one_booked, passenger_two_booked, passenger_three_booked, date_booked,telco,client_transaction_id,
            pushKey,transaction_id,booking_id,selected_payment_method;
    String status = "Unpaid";
    LinearLayout paywithmomo;
    ProgressBar progressBar;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private ApiInterface apiInterface;
    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Register userprofile = snapshot.getValue(Register.class);
                username.setText(userprofile.getFullname());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public BookTicketFragment() {
        // Required empty public constructor
    }

    public static String getToken(int length) {
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return token.toString();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_book_ticket, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        client_transaction_id = generateClientTransactionId(6);
        mdb2 = FirebaseDatabase.getInstance().getReference().child(RECEIVE_MONEY);
        mdb = FirebaseDatabase.getInstance().getReference().child("Bookings");
        rootref = FirebaseDatabase.getInstance().getReference().child("Trains");
        bookstorageReference = FirebaseStorage.getInstance().getReference();
        Bundle args = getArguments();
        assert args != null;
        strtrain_id = args.getString("train_id");
        strtrainname = args.getString("train_name");
        strtrainnumber = args.getString("train_number");
        strtraintype = args.getString("train_type");
        strtrainsource = args.getString("train_source");
        strtraindestination = args.getString("train_destination");
        strtrainarrival = args.getString("train_arrival");
        strtraindeparture = args.getString("train_departure");
        strtrainfare = args.getString("train_price");
        trainname = root.findViewById(R.id.trainname);
        trainname.setText(strtrainname);
        trainnumber = root.findViewById(R.id.trainnumber);
        trainnumber.setText(strtrainnumber);
        traintype = root.findViewById(R.id.traintype);
        traintype.setText(strtraintype);
        trainfare = root.findViewById(R.id.price);
        trainfare.setText("GH₵" + strtrainfare);
        trainsource = root.findViewById(R.id.trainsource);
        trainsource.setText(strtrainsource);
        traindestination = root.findViewById(R.id.traindestination);
        traindestination.setText(strtraindestination);
        trainarrival = root.findViewById(R.id.arrivaltype);
        trainarrival.setText(strtrainarrival);
        traindeparture = root.findViewById(R.id.departuretime);
        traindeparture.setText(strtraindeparture);
        tv_booking_code = root.findViewById(R.id.tv_booking_code);
        tv_booking_code.setText(getToken(9));
        username = root.findViewById(R.id.username);
        payment_method = root.findViewById(R.id.payment_method);
        newusername1 = root.findViewById(R.id.newusername1);
        newusername2 = root.findViewById(R.id.newusername2);
        newusername3 = root.findViewById(R.id.newusername3);
        paywithmomo = root.findViewById(R.id.paywithmomo);
        passenger1 = root.findViewById(R.id.passenger1);
        passenger2 = root.findViewById(R.id.passenger2);
        passenger3 = root.findViewById(R.id.passenger3);
        phonenumber = root.findViewById(R.id.phonenumber);
        selected_telco = root.findViewById(R.id.selected_telco);
        openGallery = root.findViewById(R.id.btnuploadpic);
        selected_image = root.findViewById(R.id.passengerImage);
        placeHolder = root.findViewById(R.id.placeHolder);
        bt_add_passenger = root.findViewById(R.id.bt_add_passenger);
        bt_book_train = root.findViewById(R.id.bt_book_train);
        tv_booking_date = root.findViewById(R.id.tv_booking_date);
        progressBar = root.findViewById(R.id.progressBar);
        String currentDateandTime = sdf.format(new Date());
        tv_booking_date.setText(currentDateandTime);
        Query query = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("id")
                .equalTo(currentUser.getUid());
        query.addListenerForSingleValueEvent(valueEventListener);


        // copy to clipboard
        bt_copy_code = root.findViewById(R.id.bt_copy_code);
        bt_copy_code.setOnClickListener(view -> Tools.copyToClipboard(getActivity(), tv_booking_code.getText().toString()));

        bt_add_passenger.setOnClickListener(v -> {
            if (username.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please add name before proceeding", Toast.LENGTH_LONG).show();
            } else {
                onePerson = 2;
                newusername1.setVisibility(View.VISIBLE);
                passenger1.setVisibility(View.VISIBLE);

                passenger3.setVisibility(View.GONE);
                passenger2.setVisibility(View.GONE);
            }
            if (newusername1.getText().toString().equals("1st Passenger Name") || newusername1.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please add name before proceeding", Toast.LENGTH_LONG).show();
            } else {
                onePerson = 3;
                newusername2.setVisibility(View.VISIBLE);
                passenger2.setVisibility(View.VISIBLE);

                passenger3.setVisibility(View.GONE);
                passenger1.setVisibility(View.GONE);
            }
            if (newusername2.getText().toString().equals("2nd Passenger Name") || newusername2.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please add name before proceeding", Toast.LENGTH_LONG).show();
            } else {
                onePerson = 4;
                newusername3.setVisibility(View.VISIBLE);
                passenger3.setVisibility(View.VISIBLE);

                passenger2.setVisibility(View.GONE);
                passenger1.setVisibility(View.GONE);
            }
            if (newusername3.getText().toString().equals("3rd Passenger Name") || newusername3.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please add name before proceeding", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Cant add more than 3 passengers", Toast.LENGTH_LONG).show();
            }
        });

        select_telco_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.telco_array, android.R.layout.simple_spinner_item);
        select_telco_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selected_telco.setAdapter(select_telco_adapter);

        selected_telco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                telco = selected_telco.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        payment_method_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.payment_arrays, android.R.layout.simple_spinner_item);
        payment_method_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payment_method.setAdapter(payment_method_adapter);

        payment_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item.toString().equals("Cash")) {
                    selected_payment_method = "Cash";
                    paywithmomo.setVisibility(View.GONE);
                }
                if (item.toString().equals("Mobile Money")) {
                    selected_payment_method = "Mobile Money";
                    paywithmomo.setVisibility(View.VISIBLE);
                }
                selected_payment_method = payment_method.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        passenger1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (charSequence.toString().trim().length() == 0) {
                    newusername1.setText("1st Passenger Name");
                } else {
                    newusername1.setText(charSequence.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passenger2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (charSequence.toString().trim().length() == 0) {
                    newusername2.setText("2nd Passenger Name");
                } else {
                    newusername2.setText(charSequence.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passenger3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (charSequence.toString().trim().length() == 0) {
                    newusername3.setText("3rd Passenger Name");
                } else {
                    newusername3.setText(charSequence.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tv_booking_date.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        });
        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;

            String date = month + "/" + day + "/" + year;
            tv_booking_date.setText(date);
        };

        openGallery.setOnClickListener(v -> {
            // Creating intent.
            Intent intent = new Intent();

            // Setting intent type as image to select image from phone storage.
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), REQUESCODE);
        });

        bt_book_train.setOnClickListener(v -> {
         if (selected_payment_method.equals("Cash")){
             bookWithCash();
         }else {
             bookWithMomo();
         }
        });
        return root;
    }

    private void bookWithCash(){
        booking_code = tv_booking_code.getText().toString();
        booking_status = status;
        train_number = trainnumber.getText().toString();
        train_name = trainname.getText().toString();
        train_type = traintype.getText().toString();
        train_source = trainsource.getText().toString();
        train_destination = traindestination.getText().toString();
        train_arrival_time = trainarrival.getText().toString();
        train_depature_time = traindeparture.getText().toString();
        person_booked = username.getText().toString();
        passenger_one_booked = newusername1.getText().toString();
        passenger_two_booked = newusername2.getText().toString();
        passenger_three_booked = newusername3.getText().toString();
        date_booked = tv_booking_date.getText().toString();



        if (placeHolder.getVisibility() == View.VISIBLE) {
            Toast.makeText(getActivity(), "Please upload an image to continue", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Booking Train...");
            progressDialog.show();
            new Handler().postDelayed(() -> {
                Query query1 = FirebaseDatabase.getInstance().getReference("Trains")
                        .orderByChild("train_id")
                        .equalTo(strtrain_id);
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Train train = snapshot.getValue(Train.class);
                            int occupie_seats = (train.getReal_avail_seats());
                            int newseat = occupie_seats + onePerson;
                            rootref.child(strtrain_id).child("real_avail_seats").setValue(newseat);
                            int train_amount = Integer.parseInt(strtrainfare) * onePerson;
                            train_fare = String.valueOf(train_amount);
                            //getting the storage reference
                            final StorageReference sRef = bookstorageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

                            //adding the file to reference
                            sRef.putFile(FilePathUri)
                                    .addOnSuccessListener(taskSnapshot -> sRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        String imglink = (uri.toString());
                                        booking_id = mdb.push().getKey();
                                        Book book = new Book(booking_id, booking_code, booking_status, strtrain_id, train_number, train_name, train_type, "GH₵" + train_fare,
                                                train_source, train_destination, train_arrival_time, train_depature_time, person_booked,
                                                passenger_one_booked, passenger_two_booked, passenger_three_booked, date_booked, imglink);
                                        mdb.child(booking_id).setValue(book);
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Train Booked Successfully", Toast.LENGTH_SHORT).show();
                                        sendEmailNotification();
                                        Fragment frag = new QrCodeTicketFragment();
                                        final Bundle qrargs = new Bundle();
                                        qrargs.putString("image_link", imglink);
                                        qrargs.putString("ticket_id", booking_code);
                                        qrargs.putString("ticket_date", date_booked);
                                        qrargs.putString("booked_person", person_booked);
                                        qrargs.putString("ticket_png1", passenger_one_booked);
                                        qrargs.putString("ticket_png2", passenger_two_booked);
                                        qrargs.putString("ticket_png3", passenger_three_booked);
                                        qrargs.putString("ticket_source", train_source);
                                        qrargs.putString("ticket_dest", train_destination);
                                        qrargs.putString("ticket_arrival", train_arrival_time);
                                        qrargs.putString("ticket_departure", train_depature_time);
                                        qrargs.putString("ticket_status", booking_status);
                                        qrargs.putString("ticket_fare", train_fare);
                                        qrargs.putString("ticket_nam", train_name);
                                        qrargs.putString("ticket_num", train_number);
                                        frag.setArguments(qrargs);
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(R.id.flContent, frag);
                                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                        ft.addToBackStack(null);
                                        ft.commit();
                                    }));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }, 4000);
        }
    }

    private void bookWithMomo(){
        booking_code = tv_booking_code.getText().toString();
        booking_status = status;
        train_number = trainnumber.getText().toString();
        train_name = trainname.getText().toString();
        train_type = traintype.getText().toString();
        train_source = trainsource.getText().toString();
        train_destination = traindestination.getText().toString();
        train_arrival_time = trainarrival.getText().toString();
        train_depature_time = traindeparture.getText().toString();
        person_booked = username.getText().toString();
        passenger_one_booked = newusername1.getText().toString();
        passenger_two_booked = newusername2.getText().toString();
        passenger_three_booked = newusername3.getText().toString();
        date_booked = tv_booking_date.getText().toString();

        if (placeHolder.getVisibility() == View.VISIBLE) {
            Toast.makeText(getActivity(), "Please upload an image to continue", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Booking Train...");
            progressDialog.show();
            new Handler().postDelayed(() -> {
                Query query1 = FirebaseDatabase.getInstance().getReference("Trains")
                        .orderByChild("train_id")
                        .equalTo(strtrain_id);
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Train train = snapshot.getValue(Train.class);
                            int occupie_seats = (train.getReal_avail_seats());
                            int newseat = occupie_seats + onePerson;
                            rootref.child(strtrain_id).child("real_avail_seats").setValue(newseat);
                            int train_amount = Integer.parseInt(strtrainfare) * onePerson;
                            train_fare = String.valueOf(train_amount);
                            //getting the storage reference
                            final StorageReference sRef = bookstorageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

                            //adding the file to reference
                            sRef.putFile(FilePathUri)
                                    .addOnSuccessListener(taskSnapshot -> sRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        String imglink = (uri.toString());
                                        booking_id = mdb.push().getKey();
                                        Book book = new Book(booking_id, booking_code, booking_status, strtrain_id, train_number, train_name, train_type, "GH₵" + train_fare,
                                                train_source, train_destination, train_arrival_time, train_depature_time, person_booked,
                                                passenger_one_booked, passenger_two_booked, passenger_three_booked, date_booked, imglink);
                                        mdb.child(booking_id).setValue(book);
                                        ReceivePayment(progressDialog,imglink);
                                    }));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }, 4000);
        }

    }

    public void ReceivePayment(ProgressDialog progressDialog, String imglink) {
        RecieveMoney recieveMoney = new RecieveMoney();
        recieveMoney.setAmount(Double.parseDouble(train_fare));
        recieveMoney.setAppid(APPID);
        recieveMoney.setClientreference(CLIENTREFERENCE);
        recieveMoney.setClienttransid(client_transaction_id);
        recieveMoney.setDescription(DESCRIPTION);
        recieveMoney.setNickname(NICKNAME);
        recieveMoney.setPaymentoption(telco);
        recieveMoney.setWalletnumber(phonenumber.getText().toString());

        //proceed to make redde online receive money from client api call
        Call<BaseResponse> recieveMoneyCall = apiInterface.recieveMoneyCall(recieveMoney);
        recieveMoneyCall.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    BaseResponse baseResponse = response.body();
                    Log.d("ResponseBodyIs", response.body().toString());
                    if (baseResponse != null && baseResponse.getStatus().equals(getResources().getText(R.string.ok))) {
                        pushKey = mdb2.push().getKey();
                        transaction_id = baseResponse.getTransactionid();
                        SaveUserTransaction saveUserTransaction = new SaveUserTransaction(currentUser.getUid(), pushKey, Double.parseDouble(train_fare), APPID, CLIENTREFERENCE, client_transaction_id, DESCRIPTION, NICKNAME, telco, phonenumber.getText().toString(),transaction_id ,booking_status, date_booked);
                        mdb2.child(pushKey).setValue(saveUserTransaction);
                        progressDialog.dismiss();
//                        send email notification
                        sendEmailNotification();
//                        show toast for succes
                        Toast.makeText(getActivity(), "Train Booked Successfully", Toast.LENGTH_SHORT).show();
//                       navigate to qr code page
                        Fragment frag = new ProcessingFragment();
                        final Bundle qrargs = new Bundle();
                        qrargs.putString("image_link", imglink);
                        qrargs.putString("ticket_id", booking_code);
                        qrargs.putString("ticket_date", date_booked);
                        qrargs.putString("booked_person", person_booked);
                        qrargs.putString("ticket_png1", passenger_one_booked);
                        qrargs.putString("ticket_png2", passenger_two_booked);
                        qrargs.putString("ticket_png3", passenger_three_booked);
                        qrargs.putString("ticket_source", train_source);
                        qrargs.putString("ticket_dest", train_destination);
                        qrargs.putString("ticket_arrival", train_arrival_time);
                        qrargs.putString("ticket_departure", train_depature_time);
                        qrargs.putString("ticket_status", booking_status);
                        qrargs.putString("ticket_fare", train_fare);
                        qrargs.putString("ticket_nam", train_name);
                        qrargs.putString("ticket_num", train_number);
                        qrargs.putString(TRANSACTION_ID, transaction_id);
                        qrargs.putString(PUSH_ID, pushKey);
                        qrargs.putString(BOOK_ID, booking_id);
                        frag.setArguments(qrargs);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.flContent, frag);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.addToBackStack(null);
                        ft.commit();
                    } else if (baseResponse.getStatus().equals(getResources().getText(R.string.pending))) {
                        showMessage(getActivity(), baseResponse.getReason());
                    } else if (baseResponse.getStatus().equals(getResources().getText(R.string.failed))) {
                        showMessage(getActivity(), baseResponse.getReason());
                    } else if (baseResponse.getStatus().equals(getResources().getText(R.string.progress))) {
                        showMessage(getActivity(), baseResponse.getReason());
                    } else if (baseResponse.getStatus().equals(getResources().getText(R.string.paid))) {
                        showMessage(getActivity(), baseResponse.getReason());
                    }
                } else {
                    showMessage(getActivity(), getResources().getText(R.string.response_failed));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("Transaction failed", t.getMessage());
                showMessage(getActivity(), getResources().getText(R.string.transaction_response_failed));
            }
        });

    }

    public void sendEmailNotification() {
        RetrofitClient.getInstance()
                .getApi()
                .sendEmail("ansong123@gmail.com", currentUser.getEmail(), "Ticket Details", "Hi " + person_booked + " you have successfully book a train with the following details : " + " \n Train Id : "
                        + train_number + " \n Train type : " + train_type + " \n Train source : " + train_source + " \nTrain destination : " + train_destination + " \n Time of arrival : " + train_arrival_time + " \n Time of departure : " + train_depature_time
                        + " \n Train fare : " + train_fare + " \n Number of people on-board : " + onePerson)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == HTTP_OK) {
                            try {
                                JSONObject obj = new JSONObject(response.body().string());
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUESCODE && data != null) {
            FilePathUri = data.getData();

            selected_image.setImageURI(FilePathUri);
            placeHolder.setVisibility(View.GONE);
            selected_image.setVisibility(View.VISIBLE);

        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.menu_book_ticket);
    }

}
