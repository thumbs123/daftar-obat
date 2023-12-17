package com.example.epotik;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.bumptech.glide.Glide;

import java.util.Objects;

import helper.DbHelper;

public class Add2 extends AppCompatActivity {
    private static final int STORAGE_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "Add2";

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
        imageView = findViewById(R.id.imagePath);


        SQLite = new DbHelper(this);


        Intent intent = getIntent();
        String receivedId = intent.getStringExtra(GigiActivity.TAG_ID);
        String receivedNama = intent.getStringExtra(GigiActivity.TAG_NAMA);
        String receivedDeskripsi = intent.getStringExtra(GigiActivity.TAG_DESKRIPSI);
        String receivedImagePath = intent.getStringExtra(GigiActivity.COLUMN_IMAGE_PATH);


        if (TextUtils.isEmpty(receivedId)) {
            setTitle("Tambah data");
        } else {
            setTitle("Edit Data");
            id.setText(receivedId);
            nama.setText(receivedNama);
            deskripsi.setText(receivedDeskripsi);

            if (!TextUtils.isEmpty(receivedImagePath)) {
                Glide.with(this).load(receivedImagePath).into(imageView);
            }
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
                // Membersihkan input dan menutup activity
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

    // Menangani hasil pemilihan gambar dari galeri
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

                            // Menampilkan gambar yang dipilih di ImageView menggunakan Glide
                            Glide.with(Add2.this).load(imageUri).into(imageView);
                        }
                    } else {
                        Toast.makeText(Add2.this, "Dibatalkan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    // Memeriksa izin penyimpanan
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    // Meminta izin penyimpanan jika belum diberikan
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
    }

    // Menyimpan data baru ke database SQLite
    private void save() {
        if (TextUtils.isEmpty(nama.getText().toString().trim()) || TextUtils.isEmpty(deskripsi.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Harap masukkan nama atau deskripsi...", Toast.LENGTH_SHORT).show();
        } else {
            String imagePath = Objects.requireNonNull(imageViewToUri(imageView)).toString();
            SQLite.insertWithImage(DbHelper.TABLE_GIGI, nama.getText().toString().trim(), deskripsi.getText().toString().trim(), imagePath);

            Intent intent = new Intent(Add2.this, GigiActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(), "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
        }
    }

    private void edit() {
        if (TextUtils.isEmpty(nama.getText().toString()) || TextUtils.isEmpty(deskripsi.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Harap masukkan nama atau deskripsi..", Toast.LENGTH_SHORT).show();
        } else {
            String imagePath = Objects.requireNonNull(imageViewToUri(imageView)).toString();
            SQLite.updateWithImage(DbHelper.TABLE_GIGI, Integer.parseInt(id.getText().toString().trim()), nama.getText().toString().trim(), deskripsi.getText().toString().trim(), imagePath);

            blank();
            finish();
            Toast.makeText(getApplicationContext(), "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show();
        }
    }

    // Membersihkan input
    private void blank() {
        nama.requestFocus();
        id.setText(null);
        nama.setText(null);
        deskripsi.setText(null);
        imageView.setImageResource(android.R.color.transparent);
    }

    // Mengubah ImageView menjadi Uri
    private Uri imageViewToUri(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        // Menyimpan gambar ke penyimpanan dan mendapatkan URI
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Deskripsi Gambar", null);
        return Uri.parse(path);
    }

    // Menangani ketika tombol kembali ditekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Menutup activity
        finish();
    }
}
