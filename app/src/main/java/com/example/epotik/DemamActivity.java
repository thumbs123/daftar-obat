package com.example.epotik;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.RecyclerViewAdapter;
import helper.DbHelper;
import model.Data;

public class DemamActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private AlertDialog.Builder dialog;
    private List<Data> itemList = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private DbHelper dbHelper;

    public static final String TAG_ID = "id";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_DESKRIPSI = "deskripsi";
    public static final String COLUMN_IMAGE_PATH = "imagePath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demam);

        dbHelper = new DbHelper(getApplicationContext());
        adapter = new RecyclerViewAdapter(DemamActivity.this, itemList);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);

        getAllData();
    }

    @Override
    public void onItemClick(int position) {
        String id = itemList.get(position).getId();
        String nama = itemList.get(position).getNama();
        String deskripsi = itemList.get(position).getDeskripsi();
        String imagePath = itemList.get(position).getImagePath();
        Intent intent = new Intent(DemamActivity.this, EditActivity.class);

        intent.putExtra(PerutActivity.TAG_ID, id);
        intent.putExtra(PerutActivity.TAG_NAMA, nama);
        intent.putExtra(PerutActivity.TAG_DESKRIPSI, deskripsi);
        intent.putExtra(PerutActivity.COLUMN_IMAGE_PATH, imagePath);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        final String id = itemList.get(position).getId();
        final String nama = itemList.get(position).getNama();
        final String deskripsi = itemList.get(position).getDeskripsi();
        final String imagePath = itemList.get(position).getImagePath();

        final CharSequence[] dialogItems = {"Edit", "Delete"};
        dialog = new AlertDialog.Builder(DemamActivity.this);
        dialog.setCancelable(true);
        dialog.setItems(dialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(DemamActivity.this, EditActivity.class);
                        intent.putExtra(TAG_ID, id);
                        intent.putExtra(TAG_NAMA, nama);
                        intent.putExtra(TAG_DESKRIPSI, deskripsi);
                        intent.putExtra(COLUMN_IMAGE_PATH, imagePath);
                        startActivity(intent);
                        break;
                    case 1:
                        showDeleteConfirmationDialog(id);
                        break;
                }
            }
        }).show();
    }

    private void showDeleteConfirmationDialog(final String id) {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle("Konfirmasi");
        deleteDialog.setMessage("Apakah anda yakin ingin menghapus data?");
        deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHelper.delete(DbHelper.TABLE_DEMAM, Integer.parseInt(id));
                itemList.clear();
                getAllData();
            }
        });
        deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        deleteDialog.show();
    }


    private void getAllData() {
        itemList.clear();
        ArrayList<HashMap<String, String>> data = dbHelper.getAllData(DbHelper.TABLE_DEMAM);

        for (int i = 0; i < data.size(); i++) {
            String id = data.get(i).get(TAG_ID);
            String nama = data.get(i).get(TAG_NAMA);
            String deskripsi = data.get(i).get(TAG_DESKRIPSI);
            String imagePath = data.get(i).get(DbHelper.COLUMN_IMAGE_PATH);

            Data dataItem = new Data(id, nama, deskripsi, imagePath);
            itemList.add(dataItem);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemList.clear();
        getAllData();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}