package com.example.epotik;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import helper.DbHelper;

public class AddActivity extends AppCompatActivity {
    EditText id, nama, deskripsi;
    Button btnSubmit, btnCancel;
    DbHelper SQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        id = findViewById(R.id.id);
        nama = findViewById(R.id.nama);
        deskripsi = findViewById(R.id.deskripsi);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);

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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
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

    private void save() {
        if (TextUtils.isEmpty(nama.getText().toString().trim()) || TextUtils.isEmpty(deskripsi.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),
                    "Please input name or description...", Toast.LENGTH_SHORT).show();
        } else {
            SQLite.insert(DbHelper.TABLE_DEMAM, nama.getText().toString().trim(), deskripsi.getText().toString().trim());

            blank();
            finish();
            Toast.makeText(getApplicationContext(), "Data added successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private void edit() {
        if (TextUtils.isEmpty(nama.getText().toString()) || TextUtils.isEmpty(deskripsi.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please input name or description..", Toast.LENGTH_SHORT).show();
        } else {
            SQLite.update(
                    DbHelper.TABLE_DEMAM,
                    Integer.parseInt(id.getText().toString().trim()),
                    nama.getText().toString().trim(),
                    deskripsi.getText().toString().trim()
            );
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
