package com.example.ecolim.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ecolim.models.Residuo;
import java.util.List;

@Dao
public interface ResiduoDao {
    @Insert
    void insert(Residuo residuo);

    @Query("SELECT * FROM residuos")
    List<Residuo> getAll();
}
