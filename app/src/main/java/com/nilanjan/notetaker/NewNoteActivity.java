package com.nilanjan.notetaker;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nilanjan.notetaker.support.ListColumns;
import com.nilanjan.notetaker.support.NotesProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewNoteActivity extends AppCompatActivity {

    @BindView(R.id.title) EditText title;
    @BindView(R.id.body) EditText body;
    @BindView(R.id.fab) FloatingActionButton edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notes);
        ButterKnife.bind(this);
        edit.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save));
    }

    @OnClick(R.id.fab)
    public void saveNote(View view) {
        if (!title.getText().toString().isEmpty()) {
            String id = Double.toString(System.currentTimeMillis() / 1000);
            ContentValues values = new ContentValues();
            values.put(ListColumns.ID, id);
            values.put(ListColumns.BODY, body.getText().toString());
            values.put(ListColumns.HEADER, title.getText().toString());
            Uri uri = getContentResolver().insert(NotesProvider.Notes.CONTENT_URI, values);
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Consider adding a title", Toast.LENGTH_SHORT).show();
        }
    }
}
