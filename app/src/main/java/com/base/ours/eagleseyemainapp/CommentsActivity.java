package com.base.ours.eagleseyemainapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import static com.base.ours.eagleseyemainapp.ClientConnection.sendMessage;


public class CommentsActivity extends AppCompatActivity {

    Button mButton;
    EditText mEdit;

    ArrayList<String> comments = new ArrayList<String>();

    String routeId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        //comment reply: "comment" \t Route BusID \t CommentID \t comment(UserID Comment)
        Intent intent = getIntent();
        String data = intent.getStringExtra("comment");
        String[] split = data.split("\t");

        if (split.length > 1)
            routeId = split[1];

        int i = 2;
        while (i < split.length) {
            String id = split[i++];
            id = String.format("%-10s", id);
            String comment = split[i++];
            comments.add(id + ": " + comment);
        }

        //comments.add("hi");

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, comments);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        mButton = (Button) findViewById(R.id.button5);

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEdit = (EditText) findViewById(R.id.editText2);
                String comment = mEdit.getText().toString();


                String id = "User";
                String forId = String.format("%-10s", id);

                comments.add(forId + ": " + comment);

                //comment by user: "comment" \t Route BusID \t UserID \t Comment
                sendMessage("comment\t" + routeId + "\t" + id + "\t" + comment);


                ArrayAdapter adapter = new ArrayAdapter<String>(CommentsActivity.this, R.layout.activity_listview, comments);
                //ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_listview, split);
                ListView listView = (ListView) findViewById(R.id.mobile_list);
                listView.setAdapter(adapter);
            }
        });

    }

}
