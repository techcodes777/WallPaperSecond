package com.hdlight.wallpaperapps.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "wallpaper_table")
public class Position {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "pos")
    private int position;

    public Position(int id, int position) {
        this.id = id;
        this.position = position;
    }

    @Ignore
    public Position(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
