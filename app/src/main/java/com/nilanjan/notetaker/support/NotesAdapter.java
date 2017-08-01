package com.nilanjan.notetaker.support;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.nilanjan.notetaker.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nilanjan on 14-Jun-17.
 * Project NoteTaker
 */

public class NotesAdapter extends FragmentStatePagerAdapter {

    private static final String TITLE = "title";
    private static final String BODY = "body";
    public static final String ID = "id";
    private ArrayList<NotesData> notesData;
    public NotesAdapter(FragmentManager fm, ArrayList<NotesData> notesData) {
        super(fm);
        this.notesData = notesData;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new NotesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, notesData.get(position).getTitle());
        bundle.putString(BODY, notesData.get(position).getBody());
        bundle.putString(ID, notesData.get(position).getId());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return notesData.size();
    }

    public static class NotesFragment extends Fragment {

        @BindView(R.id.title) EditText title;
        @BindView(R.id.body) EditText body;
        @BindView(R.id.fab) FloatingActionButton edit;
        private String id;
        private KeyListener titleListener, bodyListener;
        @Nullable @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
            ButterKnife.bind(this, rootView);
            Bundle bundle = getArguments();
            id = bundle.getString(ID);
            title.setText(bundle.getString(TITLE));
            body.setText(bundle.getString(BODY));
            titleListener = title.getKeyListener();
            bodyListener = body.getKeyListener();
            body.setKeyListener(null);
            title.setKeyListener(null);
            
            edit.setTag("0");
            return rootView;
        }

        @OnClick(R.id.fab)
        public void editNote(View view) {
            int mode = Integer.parseInt(edit.getTag().toString());
            if (mode == 0) {
                body.setKeyListener(bodyListener);
                title.setKeyListener(titleListener);
                title.requestFocus();
                edit.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_menu_save));
                edit.setTag("1");
            } else {
                body.setKeyListener(null);
                title.setKeyListener(null);
                edit.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_menu_edit));
                edit.setTag("0");
                updateNote();
                Toast.makeText(getActivity(), "Note Updated!", Toast.LENGTH_SHORT).show();
            }
        }

        private void updateNote() {
            ContentValues values = new ContentValues();
            values.put(ListColumns.BODY, body.getText().toString());
            values.put(ListColumns.HEADER, title.getText().toString());
            getActivity().getContentResolver().update(NotesProvider.Notes.CONTENT_URI, values, ListColumns.ID + "=?", new String[]{id});
        }

    }
}
