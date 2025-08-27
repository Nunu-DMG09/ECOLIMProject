package com.example.ecolim.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "residuos")
public class Residuo {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String tipo;
    public double peso;
    public String fecha;
    public String codigo;

    public Residuo(String tipo, double peso, String fecha, String codigo) {
        this.tipo = tipo;
        this.peso = peso;
        this.fecha = fecha;
        this.codigo = codigo;
    }
}
