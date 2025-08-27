package com.example.ecolim.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ecolim.models.Usuario;

@Dao
public interface UsuarioDao {

    @Insert
    void insert(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    Usuario login(String email, String password);

    @Query("SELECT COUNT(*) FROM usuarios")
    int getUsuariosCount();
}
