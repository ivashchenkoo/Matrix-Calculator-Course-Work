package com.example.matrixcalculatorcw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private AdapterOperationList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.ThemeDark);

        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.activity_main_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MatrixActivity.class);
                intent.putExtra("itemOperation", (ItemOperation) parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });

        adapter = new AdapterOperationList(getBaseContext());
        listView.setAdapter(adapter);
    }
}