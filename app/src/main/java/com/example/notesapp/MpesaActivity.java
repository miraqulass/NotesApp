package com.example.notesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.delivery.Mpesa.DataModal;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MpesaActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://tinypesa.com/api/v1/";

    private static final String PHONE_NUMBER = "+254748875966";
    private EditText phoneEdit, amountEdit, accountEdit;
    String location;
    String account;
    private TextView responseTV;
    private Button postData;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneEdit = findViewById(R.id.phone_number);
        amountEdit = findViewById(R.id.amount);
//        accountEdit = findViewById(R.id.idEdtAcc);
        postData = findViewById(R.id.pay_btn);
//        responseTV = findViewById(R.id.idTVResponse);
//        loadingPB = findViewById(R.id.idLoadingPB);

        postData.setOnClickListener(view -> {
            if(phoneEdit.getText().toString().isEmpty()){
                Toast.makeText(MpesaActivity.this, "Please enter both the values", Toast.LENGTH_SHORT).show();
                return;
            }retrofit2
            location = accountEdit.getText().toString();
            account = String.valueOf(UUID.randomUUID());
            postData(phoneEdit.getText().toString(), amountEdit.getText().toString(), account);

        });
    }

    private void postData(String phone, String amount, String accountNo) {

        loadingPB.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApi service = retrofit.create(RetrofitApi.class);

        DataModal dataModal = new DataModal(phone,amount, accountNo );
        String acceptHeader = "application/json";
        String apikey = "UILUeVO6GBD";
        Call<DataModal> call = service.createPost(apikey,acceptHeader,dataModal);

        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(@NonNull Call<DataModal> call, @NonNull Response<DataModal> response) {
                // this method is called when we get response from our api.
                Toast.makeText(MpesaActivity.this, response.message(), Toast.LENGTH_LONG).show();

retrofit2

                Intent intent = new Intent(MpesaActivity.this, DownloadActivity.class);
                startActivity(intent);

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NonNull Call<DataModal> call, @NonNull Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                responseTV.setText("Error found is : " + t.getMessage());
            }
        });
    }
}