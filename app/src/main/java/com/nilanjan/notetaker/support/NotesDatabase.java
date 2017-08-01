package com.nilanjan.notetaker.support;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.ExecOnCreate;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by nilanjan on 13-Jun-17.
 * Project NoteTaker
 */

@Database(version = NotesDatabase.VERSION,
        packageName = "com.nilanjan.notetaker.provider")
public class NotesDatabase {
    public static final int VERSION = 1;
    @Table(ListColumns.class)
    public static final String NOTE = "note";
    @ExecOnCreate
    public static final String EXEC_ON_CREATE = "SELECT * FROM " + NOTE;

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {
    }

}