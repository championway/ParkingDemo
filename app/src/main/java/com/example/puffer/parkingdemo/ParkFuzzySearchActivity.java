package com.example.puffer.parkingdemo;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.example.puffer.parkingdemo.model.DataManager;
import com.example.puffer.parkingdemo.model.Park;
import com.example.puffer.parkingdemo.model.ParkDao;

import java.util.ArrayList;
import java.util.Collections;

public class ParkFuzzySearchActivity extends AppCompatActivity {
    private EditText ed_search;
    private RadioGroup transportation;
    private ListView listView;

    private ArrayList<Park> parkArrayList = new ArrayList<>();
    private ParkListAdapter adapter;

    private int mode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_fuzzy_search);

        findView();
        initList();
        readDataSet();
        setListen();
    }

    private void findView(){
        ed_search = findViewById(R.id.ed_search);

        transportation = findViewById(R.id.transportation);

        listView = findViewById(R.id.listView);
    }

    private void initList(){
        adapter = new ParkListAdapter(this, parkArrayList);
        listView.setAdapter(adapter);
    }

    private void readDataSet() {
        String query = "SELECT * FROM Table_Parking WHERE ";
        Park[] parks;
        ParkDao dao = DataManager.getInstance().getParkDao();

        switch (mode) {
            case 0 :
                query += "totalbus > 0";
                break;
            case 1 :
                query += "totalcar > 0";
                break;
            case 2 :
                query += "totalmotor > 0";
                break;
            default :
                query += "totalbike > 0";
                break;
        }

        if(ed_search.getText().length() > 0) {
            query += String.format(" AND name LIKE \'%%%s%%\'", ed_search.getText().toString());
        }
        parks = dao.getAllByQuery(new SimpleSQLiteQuery(query));

        parkArrayList.clear();
        Collections.addAll(parkArrayList, parks);
        adapter.notifyDataSetChanged();
    }

    private void setListen(){
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                readDataSet();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        transportation.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.tv_bus:
                    mode = 0;
                    break;
                case R.id.tv_car:
                    mode = 1;
                    break;
                case R.id.tv_moto:
                    mode = 2;
                    break;
                case R.id.tv_bike:
                    mode = 3;
                    break;
            }

            readDataSet();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ParkInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", parkArrayList.get(position).id);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}
