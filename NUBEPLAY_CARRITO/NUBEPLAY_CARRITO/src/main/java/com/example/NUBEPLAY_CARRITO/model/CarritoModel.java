package com.example.NUBEPLAY_CARRITO.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Data
@ToString(exclude = "items")
@Entity
@Table(name = "carrito")


public class CarritoModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCarrito;


    private int idUsuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CarritoItemModel> items; 
}