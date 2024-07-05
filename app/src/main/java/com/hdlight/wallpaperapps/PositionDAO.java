package com.hdlight.wallpaperapps;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hdlight.wallpaperapps.model.Position;

import java.util.List;

@Dao
public interface PositionDAO {


    @Query("SELECT * FROM wallpaper_table")
    List<Position> getAllPosition();

    @Insert
    void addPos(Position position);


}
