package com.nilanjan.notetaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nilanjan.notetaker.support.ListColumns;
import com.nilanjan.notetaker.support.NotesAdapter;
import com.nilanjan.notetaker.support.NotesData;
import com.nilanjan.notetaker.support.NotesProvider;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * Created by nilanjan on 13-Jun-17.
 * Project NoteTaker
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    public static final int ID = 0;
    public static final String TAG = MainActivity.class.getCanonicalName();

    @BindView(R.id.fragment_no_data) View noData;
    private ViewPager mViewPager;
    private ArrayList<NotesData> notesData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mViewPager = (ViewPager) findViewById(R.id.container);
        Toast.makeText(this, "Swipe Left/Right to change notes", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        mViewPager.setVisibility(View.VISIBLE);
        noData.setVisibility(GONE);
        getNotes();
    }

    @OnClick({ R.id.note_image, R.id.create_note})
    public void startNewNote(View view) {
        Intent intent = new Intent(this, NewNoteActivity.class);
        startActivity(intent);
    }

    private void getNotes() {
        getSupportLoaderManager().initLoader(ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, NotesProvider.Notes.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        notesData = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Log.d(TAG, "getMemories: " + Arrays.toString(cursor.getColumnNames()));
            NotesData data = new NotesData(
                    cursor.getString(cursor.getColumnIndex(ListColumns.ID)),
                    cursor.getString(cursor.getColumnIndex(ListColumns.HEADER)),
                    cursor.getString(cursor.getColumnIndex(ListColumns.BODY))
            );

            notesData.add(data);
            cursor.moveToNext();
        }
        NotesAdapter notesAdapter = new NotesAdapter(getSupportFragmentManager(), notesData);
        mViewPager.setAdapter(notesAdapter);
        if (notesData.size() == 0) {
            Log.d(TAG, "onLoadFinished: " + notesData.size());
            mViewPager.setVisibility(GONE);
            noData.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "onLoadFinished: " + notesData.size());
            mViewPager.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_note:
                Intent intent = new Intent(this, NewNoteActivity.class);
                startActivity(intent);
                break;
            case R.id.action_delete_note:
                int position = mViewPager.getCurrentItem();
                if (notesData.size() > 0)
                    deleteNote(position);
                else
                    Toast.makeText(this, "Sorry no note found", Toast.LENGTH_LONG).show();
                mViewPager.setAdapter(null);
                getNotes();
                break;
            case R.id.action_logout:
                SharedPreferences sharedPreferences =
                        this.getSharedPreferences(getString(R.string.shared_preference_file), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("logged", "0");
                editor.apply();
                finish();
                break;
        }
        return true;
    }

    private void deleteNote(int position) {
        String id = notesData.get(position).getId();
        getContentResolver().delete(NotesProvider.Notes.CONTENT_URI, ListColumns.ID + "=?", new String[]{id});
        Toast.makeText(this, "Note Deleted", Toast.LENGTH_LONG).show();
    }
}
