package com.example.cities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondClass extends AppCompatActivity {

    ListView listView;
    Bundle extras;
    Map<String, double[]> cities = new HashMap<>();
    String selectedCity;
    double[] selectedCityCoords;
    List<String> citiesWithinTheL = new ArrayList<>();
    float L;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        listView = findViewById(R.id.list_view);

        Intent intent = getIntent();
        extras = intent.getExtras();
        cities = (HashMap<String, double[]>) extras.getSerializable("cities");
        selectedCity = extras.getString("selectedCity");

        selectedCityCoords = cities.get(selectedCity);
        L = extras.getFloat("L", 5);

        searchForCitiesWithinTheL();
        citiesWithinTheL.remove(selectedCity);

        Collections.sort(citiesWithinTheL);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item, citiesWithinTheL);
        listView.setAdapter(adapter);

        Toast.makeText(this, "selectedCity: " + selectedCity + ", selectedCityCoords: " + selectedCityCoords[0] + ", " + selectedCityCoords[1] +
                ", L= " + L + ", citiesWithinTheL = " + citiesWithinTheL.size(), Toast.LENGTH_LONG).show();
    }

    private void searchForCitiesWithinTheL() {

        double[] value;
        float[] results = new float[1];

        for (Map.Entry<String, double[]> entry : cities.entrySet()) {
            value = entry.getValue();
            Location.distanceBetween(selectedCityCoords[1], selectedCityCoords[0], value[1], value[0], results);
            if (results[0] <= L * 1000)
                citiesWithinTheL.add(entry.getKey());
        }
    }
}