package com.example.labmcp.service;

import com.example.labmcp.model.Pedido;
import com.example.labmcp.model.Producto;
import com.example.labmcp.repository.PedidoRepository;
import com.example.labmcp.repository.ProductoRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CRUD de productos y pedidos a domicilio expuesto como herramientas MCP.
 */
@Component
public class SupermercadoToolsService {

    private final ProductoRepository productoRepo;
    private final PedidoRepository pedidoRepo;

    public SupermercadoToolsService(ProductoRepository productoRepo, PedidoRepository pedidoRepo) {
        this.productoRepo = productoRepo;
        this.pedidoRepo = pedidoRepo;
    }

    // ---------- READ ----------

    @Tool(name = "listCatalogo", description = "Devuelve la lista completa de productos disponibles en el supermercado con su precio y stock.")
    public List<Producto> listarCatalogo() {
        return productoRepo.findAll();
    }

    @Tool(name = "buscarProductos", description = "Busca productos cuyo nombre contiene el fragmento indicado (case-insensitive).")
    public List<Producto> buscarProductos(
            @ToolParam(description = "Fragmento de texto a buscar en el nombre del producto") String fragmento) {
        return productoRepo.findByNombreContainingIgnoreCase(fragmento);
    }

    @Tool(name = "listPedidos", description = "Devuelve la lista completa de pedidos a domicilio almacenados.")
    public List<Pedido> listarPedidos() {
        return pedidoRepo.findAll();
    }

    @Tool(name = "listPedidosPorEstado", description = "Devuelve únicamente los pedidos según su estado (PENDIENTE, EN_CAMINO, ENTREGADO, CANCELADO).")
    public List<Pedido> listarPedidosPorEstado(
            @ToolParam(description = "Estado del pedido: PENDIENTE, EN_CAMINO, ENTREGADO o CANCELADO") String estado) {
        return pedidoRepo.findByEstadoIgnoreCase(estado);
    }

    @Tool(name = "buscarPedidosCliente", description = "Busca pedidos realizados por un cliente específico (case-insensitive).")
    public List<Pedido> buscarPedidosPorCliente(
            @ToolParam(description = "Nombre o fragmento del nombre del cliente") String cliente) {
        return pedidoRepo.findByClienteContainingIgnoreCase(cliente);
    }

    // Agregar dentro de // ---------- CREATE ----------
    @Tool(name = "crearProducto", description = "Crea un nuevo producto en el catálogo del supermercado con nombre, categoría, precio y stock inicial.")
    @Transactional
    public Producto crearProducto(
            @ToolParam(description = "Nombre del producto (p.ej. 'Queso Campesino 500g')") String nombre,
            @ToolParam(description = "Categoría del producto (p.ej. 'Lácteos', 'Panadería', 'Despensa')") String categoria,
            @ToolParam(description = "Precio unitario del producto") Double precio,
            @ToolParam(description = "Cantidad disponible en inventario (stock)") Integer stock) {
        Producto prod = new Producto(nombre, categoria, precio, stock);
        return productoRepo.save(prod);
    }

    // Agregar dentro de // ---------- UPDATE ----------
    @Tool(name = "updateProducto", description = "Actualiza el nombre, categoría, precio o stock de un producto existente. Los parámetros nulos no se modifican.")
    @Transactional
    public Producto actualizarProducto(
            @ToolParam(description = "Id del producto a actualizar") Long id,
            @ToolParam(description = "Nuevo nombre (opcional, null = no cambiar)") String nombre,
            @ToolParam(description = "Nueva categoría (opcional, null = no cambiar)") String categoria,
            @ToolParam(description = "Nuevo precio unitario (opcional, null = no cambiar)") Double precio,
            @ToolParam(description = "Nuevo stock disponible (opcional, null = no cambiar)") Integer stock) {
        Producto prod = productoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el producto " + id));
        if (nombre != null && !nombre.isBlank()) prod.setNombre(nombre);
        if (categoria != null && !categoria.isBlank()) prod.setCategoria(categoria);
        if (precio != null) prod.setPrecio(precio);
        if (stock != null) prod.setStock(stock);
        return productoRepo.save(prod);
    }

    // Agregar dentro de // ---------- DELETE ----------
    @Tool(name = "eliminarProducto", description = "Elimina el producto con el id indicado del catálogo. Devuelve un mensaje de confirmación.")
    @Transactional
    public String eliminarProducto(
            @ToolParam(description = "Id del producto a eliminar") Long id) {
        if (!productoRepo.existsById(id)) {
            return "El producto " + id + " no existe.";
        }
        productoRepo.deleteById(id);
        return "Producto " + id + " eliminado correctamente.";
    }

    // ---------- CREATE ----------

    @Tool(name = "crearPedido", description = "Crea un nuevo pedido a domicilio en el supermercado. Por defecto queda en estado PENDIENTE.")
    @Transactional
    public Pedido crearPedido(
            @ToolParam(description = "Nombre completo del cliente") String cliente,
            @ToolParam(description = "Dirección exacta de entrega del domicilio") String direccion,
            @ToolParam(description = "Detalle de productos y cantidades, p.ej. '2x Leche Deslactosada 1L, 1x Pan Integral'") String detalleProductos,
            @ToolParam(description = "Valor total del pedido calculado con los precios del catálogo") Double total,
            @ToolParam(description = "Fecha de entrega asociada, formato YYYY-MM-DD (p.ej. 2026-11-15)") String fechaEntrega) {
        Pedido p = new Pedido(cliente, direccion, detalleProductos, total, "PENDIENTE", fechaEntrega);
        return pedidoRepo.save(p);
    }

    // ---------- UPDATE ----------

    @Tool(name = "updateEstadoPedido", description = "Actualiza el estado de un pedido existente (PENDIENTE, EN_CAMINO, ENTREGADO, CANCELADO).")
    @Transactional
    public Pedido actualizarEstadoPedido(
            @ToolParam(description = "Identificador único del pedido") Long id,
            @ToolParam(description = "Nuevo estado del pedido") String estado) {
        Pedido p = pedidoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el pedido " + id));
        p.setEstado(estado.toUpperCase());
        return pedidoRepo.save(p);
    }

    @Tool(name = "updatePedido", description = "Actualiza la dirección, productos o fecha de entrega de un pedido existente. Los parámetros nulos no se modifican.")
    @Transactional
    public Pedido actualizarPedido(
            @ToolParam(description = "Id del pedido a actualizar") Long id,
            @ToolParam(description = "Nueva dirección (opcional, null = no cambiar)") String direccion,
            @ToolParam(description = "Nuevo detalle de productos (opcional, null = no cambiar)") String detalleProductos,
            @ToolParam(description = "Nueva fecha de entrega (opcional, null = no cambiar)") String fechaEntrega) {
        Pedido p = pedidoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el pedido " + id));
        if (direccion != null && !direccion.isBlank()) p.setDireccion(direccion);
        if (detalleProductos != null && !detalleProductos.isBlank()) p.setDetalleProductos(detalleProductos);
        if (fechaEntrega != null && !fechaEntrega.isBlank()) p.setFechaEntrega(fechaEntrega);
        return pedidoRepo.save(p);
    }

    // ---------- DELETE ----------

    @Tool(name = "eliminarPedido", description = "Elimina el pedido con el id indicado. Devuelve un mensaje de confirmación.")
    @Transactional
    public String eliminarPedido(
            @ToolParam(description = "Id del pedido a eliminar") Long id) {
        if (!pedidoRepo.existsById(id)) {
            return "El pedido " + id + " no existe.";
        }
        pedidoRepo.deleteById(id);
        return "Pedido " + id + " eliminado correctamente.";
    }
}