package vn.edu.tlu.nhom7.calendar.activity.timezone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import java.util.TimeZone;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

import vn.edu.tlu.nhom7.calendar.R;

public class SelectTimezoneActivity extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> timezonesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_timezone);

        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);

        String[] timezones = TimeZone.getAvailableIDs();
        timezonesList = new ArrayList<>(Arrays.asList(timezones));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timezonesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTimezone = adapter.getItem(position);
            String city = selectedTimezone;


            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedTimezone", selectedTimezone);
            resultIntent.putExtra("city", city);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
