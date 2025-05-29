package com.example.NUBEPLAY_INVENTARIO.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "inventario")
@Data
public class InventarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_juego")
    private int idJuego;

    @Column(name = "titulo_juego", nullable = false)
    private String tituloJuego;

    @Column(name = "categoria", nullable = false)
    private String categoria;

    @Column(name = "stock")
    private int stock;

    @Column(name = "precio")
    private double precio;

    @Column(name = "rating_contenido")
    private int ratingContenido;
}