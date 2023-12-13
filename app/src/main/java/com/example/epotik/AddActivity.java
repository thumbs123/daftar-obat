package com.example.epotik;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Objects;

import helper.DbHelper;

public class AddActivity extends AppCompatActivity {
    private static final int STORAGE_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "AddActivity";

    EditText id, nama, deskripsi;
    Button btnCancel, upload;
    ImageView imageView;
    DbHelper SQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        id = findViewById(R.id.id);
        nama = findViewById(R.id.nama);
        deskripsi = findViewById(R.id.deskripsi);
        btnCancel = findViewById(R.id.btn_cancel);
        upload = findViewById(R.id.upload);
        imageView = findViewById(R.id.gambar);

        SQLite = new DbHelper(this);

        String receivedId = getIntent().getStringExtra(DemamActivity.TAG_ID);
        String receivedNama = getIntent().getStringExtra(DemamActivity.TAG_NAMA);
        String receivedDeskripsi = getIntent().getStringExtra(DemamActivity.TAG_DESKRIPSI);

        if (TextUtils.isEmpty(receivedId)) {
            setTitle("Add data");
        } else {
            setTitle("Edit Data");
            id.setText(receivedId);
            nama.setText(receivedNama);
            deskripsi.setText(receivedDeskripsi);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission()) {
                    pickImageGallery();
                } else {
                    requestStoragePermission();
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (TextUtils.isEmpty(id.getText().toString().trim())) {
                        save();
                    } else {
                        edit();
                    }
                } catch (Exception e) {
                    Log.e("Submit", e.toString());
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blank();
                finish();
            }
        });
    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();
                            Log.d(TAG, "onActivityResult: " + imageUri);

                            // Tampilkan gambar pada ImageView
                            imageView.setImageURI(imageUri);
                        }
                    } else {
                        Toast.makeText(AddActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
    }

    private void save() {
        if (TextUtils.isEmpty(nama.getText().toString().trim()) || TextUtils.isEmpty(deskripsi.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please input name or description...", Toast.LENGTH_SHORT).show();
        } else {
            String imagePath = Objects.requireNonNull(imageViewToUri(imageView)).toString();
            SQLite.insertWithImage(DbHelper.TABLE_DEMAM, nama.getText().toString().trim(), deskripsi.getText().toString().trim(), imagePath);

            blank();
            finish();
            Toast.makeText(getApplicationContext(), "Data added successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private void edit() {
        if (TextUtils.isEmpty(nama.getText().toString()) || TextUtils.isEmpty(deskripsi.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please input name or description..", Toast.LENGTH_SHORT).show();
        } else {
            String imagePath = Objects.requireNonNull(imageViewToUri(imageView)).toString();
            SQLite.updateWithImage(DbHelper.TABLE_DEMAM, Integer.parseInt(id.getText().toString().trim()), nama.getText().toString().trim(), deskripsi.getText().toString().trim(), imagePath);

            blank();
            finish();
            Toast.makeText(getApplicationContext(), "Data updated successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private void blank() {
        nama.requestFocus();
        id.setText(null);
        nama.setText(null);
        deskripsi.setText(null);
        imageView.setImageResource(android.R.color.transparent);
    }

    private Uri imageViewToUri(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image Description", null);
        return Uri.parse(path);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
