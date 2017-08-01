package com.nilanjan.notetaker.support;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by nilanjan on 13-Jun-17.
 * Project NoteTaker
 */

public interface ListColumns {
    @DataType(DataType.Type.TEXT) @PrimaryKey
    String ID = "id";
    @DataType(DataType.Type.TEXT)
    String HEADER = "header";
    @DataType(DataType.Type.TEXT)
    String BODY = "body";
}