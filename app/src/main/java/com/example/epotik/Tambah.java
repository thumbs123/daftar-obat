package com.example.epotik;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Tambah extends AppCompatActivity {

    Button demam, sakitPerut, sakitGigi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jenis);

        demam = findViewById(R.id.demam);
        sakitPerut = findViewById(R.id.sakit_perut);
        sakitGigi = findViewById(R.id.sakit_gigi);

        demam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tambah.this, AddActivity.class);
                startActivity(intent);
            }
        });
        sakitPerut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tambah.this, Add1.class);
                startActivity(intent);
            }
        });
        sakitGigi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tambah.this, Add2.class);
                startActivity(intent);
            }
        });
    }
}