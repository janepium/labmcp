package com.example.labmcp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String cliente;

    @NotBlank
    private String direccion;

    @NotBlank
    @Column(name = "detalle_productos", columnDefinition = "TEXT")
    private String detalleProductos;

    @NotNull
    private Double total;

    private String estado;

    @Column(name = "fecha_entrega")
    private String fechaEntrega;

    @Version
    private Long version;  // bloqueo optimista

    public Pedido() {}

    public Pedido(String cliente, String direccion, String detalleProductos, Double total, String estado, String fechaEntrega) {
        this.cliente = cliente;
        this.direccion = direccion;
        this.detalleProductos = detalleProductos;
        this.total = total;
        this.estado = estado;
        this.fechaEntrega = fechaEntrega;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDetalleProductos() {
        return detalleProductos;
    }

    public void setDetalleProductos(String detalleProductos) {
        this.detalleProductos = detalleProductos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}