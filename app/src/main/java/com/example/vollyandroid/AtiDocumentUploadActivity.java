package com.example.vollyandroid;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AtiDocumentUploadActivity extends AppCompatActivity {

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ati_document_upload);

        apiInterface = ApiClient.getClient();

        findViewById(R.id.uploadButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestClass requestClass = new RequestClass("4181290000882183","11","30","1990-10-30","01755556338","est@gmail.com",
                        "","","");

                apiInterface.getResponse(requestClass).enqueue(new Callback<ResponseClass>() {
                    @Override
                    public void onResponse(Call<ResponseClass> call, Response<ResponseClass> response) {
                        ResponseClass responseClass = response.body();

                        Log.d("Response",responseClass.toString());
                        Toast.makeText(AtiDocumentUploadActivity.this, ""+responseClass.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseClass> call, Throwable t) {

                    }
                });

            }
        });


    }

}