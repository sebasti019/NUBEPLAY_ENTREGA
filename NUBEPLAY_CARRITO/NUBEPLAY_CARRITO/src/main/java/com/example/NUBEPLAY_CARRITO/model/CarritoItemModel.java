package com.example.NUBEPLAY_CARRITO.model;

import lombok.Data;
import lombok.ToString;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Data
@ToString(exclude = "carrito")
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
    @JsonBackReference
    private CarritoModel carrito;

    public int getIdCarrito() {
        return carrito != null ? carrito.getIdCarrito() : 0;
    }
}