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

    private ActivityResultLauncher<String> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ati_document_upload);

        Button selectFileButton = findViewById(R.id.selectFileButton);

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                String filePath = getFilePathFromUri(uri);

                // Proceed with file upload
                uploadFile(filePath);
            }
        });

        selectFileButton.setOnClickListener(v -> {
            filePickerLauncher.launch("*/*");
        });


    }

    private void uploadFile(String filePath) {
        // Create a File object from the file path
        File file = new File(filePath);

        // Create a request body using the file
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("file/*"), file);

        // Create a MultipartBody.Part using the request body
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);

        // Make the API call
        ApiClient.getClient().uploadFile(filePart).enqueue(new Callback<FileUploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<FileUploadResponse> call, @NonNull Response<FileUploadResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(AtiDocumentUploadActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AtiDocumentUploadActivity.this, "Not Working", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FileUploadResponse> call, Throwable t) {
                Toast.makeText(AtiDocumentUploadActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getFilePathFromUri(Uri uri) {
        String filePath = "";
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }


   /* private String getFilePathFromUri(Uri uri) {
        String filePath = null;
        if (uri != null) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                // If the URI is a document URI, extract the document ID
                String documentId = DocumentsContract.getDocumentId(uri);
                if (isMediaDocument(uri)) {
                    // MediaProvider documents (images, videos, audio, etc.)
                    String[] split = documentId.split(":");
                    String type = split[0];
                    Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{split[1]};
                    filePath = getDataColumn(contentUri, selection, selectionArgs);
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider documents
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(documentId));
                    filePath = getDataColumn(contentUri, null, null);
                } else if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider documents
                    String[] split = documentId.split(":");
                    String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // If the URI is a content URI
                filePath = getDataColumn(uri, null, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                // If the URI is a file URI
                filePath = uri.getPath();
            }
        }
        return filePath;
    }

    private String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                filePath = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }*/

}