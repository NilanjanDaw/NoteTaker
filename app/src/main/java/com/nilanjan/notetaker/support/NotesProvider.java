package com.nilanjan.notetaker.support;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by nilanjan on 13-Jun-17.
 * Project NoteTaker
 */

@ContentProvider(authority = NotesProvider.AUTHORITY,
        database = NotesDatabase.class,
        packageName = "com.nilanjan.notetaker.provider")
public class NotesProvider {
    public static final String AUTHORITY = "com.nilanjan.notetaker.NotesProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = NotesDatabase.NOTE)
    public static class Notes {
        @ContentUri(path = NotesDatabase.NOTE,
                type = "vnd.android.cursor.dir/note",
                defaultSort = ListColumns.ID + " DESC")
        public static final Uri CONTENT_URI = buildUri(NotesDatabase.NOTE);

        @InexactContentUri(
                name = "NOTE_BY_ID",
                path = NotesDatabase.NOTE + "/#",
                type = "vnd.android.cursor.dir/note",
                whereColumn = ListColumns.ID,
                pathSegment = 1
        )
        public static Uri withId(String id) {
            return buildUri(NotesDatabase.NOTE, id);
        }

        @InexactContentUri(
                name = "NOTE_BY_HEADER",
                path = NotesDatabase.NOTE + "/$",
                type = "vnd.android.cursor.dir/note",
                whereColumn = ListColumns.HEADER,
                pathSegment = 1
        )
        public static Uri withTitle(String header) {
            return buildUri(NotesDatabase.NOTE, header);
        }

    }
}