package com.hdlight.wallpaperapps.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hdlight.wallpaperapps.PositionDAO;
import com.hdlight.wallpaperapps.model.Position;


@Database(entities = {Position.class}, exportSchema = false, version = 1)
public abstract class DataBaseHelper extends RoomDatabase {

    private static final String DB_NAME = "position_db";


    private static DataBaseHelper instance;

    public static synchronized DataBaseHelper getDb(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, DataBaseHelper.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract PositionDAO positionDAO();

}
