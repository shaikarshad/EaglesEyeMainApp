package com.base.ours.eagleseyemainapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RouteListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
        String[] routeSA=getResources().getStringArray(R.array.routes);
        ArrayAdapter<String> adapter=
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,routeSA);
        ListView lv= (ListView) findViewById(R.id.routeListView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(RouteListActivity.this,MapsRouteActivity.class);
                startActivity(intent);
            }
        });
    }
}
