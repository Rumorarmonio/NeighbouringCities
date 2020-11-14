package com.example.cities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Intent intent;
    Map<String, double[]> cities = new HashMap<>();
    Object[] names;
    String selectedCity;
    Spinner spinner;
    EditText L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        L = findViewById(R.id.distance);

        getJSON();

        names = cities.keySet().toArray();
        Arrays.sort(names);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item, names);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void getJSON() {
        String json;
        try {
            InputStream is = getAssets().open("city.list.min.short.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                JSONObject coords = object.getJSONObject("coord");
                cities.put(object.getString("name"), new double[]{coords.getDouble("lon"), coords.getDouble("lat")});
            }
            Log.d("mytag", "Number of cities = " + cities.size());
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCity = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void search(View view) {
        intent = new Intent(this, SecondClass.class);

        intent.putExtra("cities", (Serializable) cities);
        intent.putExtra("selectedCity", selectedCity);
        intent.putExtra("L", Float.parseFloat(L.getText().toString()));

        startActivity(intent);
    }
}