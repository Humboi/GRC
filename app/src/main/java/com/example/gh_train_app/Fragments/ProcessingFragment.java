package com.example.gh_train_app.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gh_train_app.OthersFiles.ApiClient;
import com.example.gh_train_app.OthersFiles.ApiInterface;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.BaseResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.gh_train_app.OthersFiles.Tools.APPID;
import static com.example.gh_train_app.OthersFiles.Tools.BOOK_ID;
import static com.example.gh_train_app.OthersFiles.Tools.PUSH_ID;
import static com.example.gh_train_app.OthersFiles.Tools.RECEIVE_MONEY;
import static com.example.gh_train_app.OthersFiles.Tools.TRANSACTION_ID;
import static com.example.gh_train_app.OthersFiles.Tools.showMessage;


public class ProcessingFragment extends Fragment {
    private final int ONE_SECONDS = 1000;
    private final Handler handler = new Handler();
    private ProgressBar progressBar;
    private Button viewTransactions;
    private ImageView successImage;
    private TextView transaction_status, transaction_date, transaction_reason;
    private String transactionId,pushKey,booking_id,booking_code, booking_status, train_number, train_name, train_fare,
            train_source, train_destination, train_arrival_time, train_depature_time, person_booked,
            passenger_one_booked, passenger_two_booked, passenger_three_booked, date_booked,payment_date;
    private ApiInterface apiInterface;
    private DatabaseReference bookings, receive;
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;


    public ProcessingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_processing, container, false);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        bookings = FirebaseDatabase.getInstance().getReference().child("Bookings");
        receive = FirebaseDatabase.getInstance().getReference().child(RECEIVE_MONEY);
        Bundle bundle = getArguments();
        transactionId = bundle.getString(TRANSACTION_ID) == null ? "0" : bundle.getString(TRANSACTION_ID);
        booking_id = bundle.getString(BOOK_ID) == null ? "0" : bundle.getString(BOOK_ID);
        pushKey = bundle.getString(PUSH_ID) == null ? "0" : bundle.getString(PUSH_ID);
        booking_code = bundle.getString("ticket_id");
        date_booked = bundle.getString("ticket_date");
        person_booked = bundle.getString("booked_person");
        passenger_one_booked = bundle.getString("ticket_png1");
        passenger_two_booked = bundle.getString("ticket_png2");
        passenger_three_booked = bundle.getString("ticket_png3");
        train_source = bundle.getString("ticket_source");
        train_destination = bundle.getString("ticket_dest");
        train_arrival_time = bundle.getString("ticket_arrival");
        train_depature_time = bundle.getString("ticket_departure");
        booking_status = bundle.getString("ticket_status");
        train_fare = bundle.getString("ticket_fare");
        train_name = bundle.getString("ticket_nam");
        train_number = bundle.getString("ticket_num");

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("EEE, dd MMM yyyy, hh:mm aa");
        payment_date = dateformat.format(c.getTime());

        scheduleStatusChecker();
        progressBar = root.findViewById(R.id.progressBar);
        successImage = root.findViewById(R.id.successImage);
        transaction_status = root.findViewById(R.id.transaction_status);
        transaction_date = root.findViewById(R.id.transaction_date);
        transaction_reason = root.findViewById(R.id.transaction_reason);
        viewTransactions = root.findViewById(R.id.view_transactions);

        viewTransactions.setOnClickListener(v->{
            handler.removeCallbacksAndMessages(null);
            Fragment frag = new QrCodeTicketFragment();
            final Bundle qrargs = new Bundle();
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
        });

        return  root;
    }


    private void scheduleStatusChecker() {
        handler.postDelayed(new Runnable() {
            public void run() {
                checkTransactionStatus(transactionId);
                handler.postDelayed(this, ONE_SECONDS);
            }
        }, ONE_SECONDS);
    }

    private void checkTransactionStatus(String transId) {
        //proceed to make redde online status check money status api call
        Call<BaseResponse> sendMoneyCall = apiInterface.checkStatusCall(transId, APPID);
        sendMoneyCall.enqueue(new Callback<BaseResponse>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                if (response.isSuccessful()) {
                    BaseResponse baseResponse = response.body();
                    Log.d("ResponseBody", response.body().toString());
                    transaction_status.setText(baseResponse.getStatus());
                    transaction_reason.setText(baseResponse.getReason());
                    transaction_date.setText(baseResponse.getStatusdate());
                    viewTransactions.setVisibility(View.GONE);
                    if (baseResponse != null && baseResponse.getStatus().equals(getResources().getText(R.string.ok))) {
                        progressBar.setVisibility(View.GONE);
                        successImage.setVisibility(View.VISIBLE);
                        viewTransactions.setVisibility(View.VISIBLE);
                    } else if (baseResponse.getStatus().equals(getResources().getText(R.string.pending))) {

                    } else if (baseResponse.getStatus().equals(getResources().getText(R.string.failed))) {
                        progressBar.setVisibility(View.GONE);
                        successImage.setVisibility(View.VISIBLE);
                        viewTransactions.setVisibility(View.VISIBLE);
                        successImage.setImageDrawable(getResources().getDrawable(R.drawable.failed_transaction));

                    } else if (baseResponse.getStatus().equals(getResources().getText(R.string.progress))) {

                    } else if (baseResponse.getStatus().equals(getResources().getText(R.string.paid))) {
                        progressBar.setVisibility(View.GONE);
                        viewTransactions.setVisibility(View.VISIBLE);
                        successImage.setVisibility(View.VISIBLE);
                        successImage.setImageDrawable(getResources().getDrawable(R.drawable.success));
                        updateTransaction();

                    }
                } else {
                    showMessage(getActivity(), getResources().getText(R.string.response_failed));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("Transaction failed", t.getMessage());
                showMessage(getActivity(), getResources().getText(R.string.transaction_response_failed));
            }
        });
    }

   public void updateTransaction(){
       bookings.child(booking_id).child("booking_status").setValue("PAID");
       receive.child(pushKey).child("transactionStatus").setValue("PAID");
   }


}