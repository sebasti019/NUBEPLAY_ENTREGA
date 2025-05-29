package com.example.NUBEPLAY_CARRITO.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "carrito_item")
public class CarritoItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idItem;

    private int idJuego;
    private int cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrito")
    private CarritoModel carrito;

    public int getIdCarrito() {
        return carrito != null ? carrito.getIdCarrito() : 0;
    }
}