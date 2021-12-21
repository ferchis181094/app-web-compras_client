package com.componente.controller;


import com.eCommerce.controller.ProductoController;
import com.eCommerce.controller.ReglasController;
import com.eCommerce.entity.Carrito;
import com.eCommerce.entity.Producto;
import com.eCommerce.entity.Regla;
import conf.Constants;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "GestionProductoController")
@SessionScoped
public class GestionProductoController implements Serializable {

    private List<Producto> listproducto;
    private ProductoController productoService;

    private int cantidad;

    private final Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

    private List<Carrito> productosCarrito = new ArrayList<>();

    double[] resultados;
    
    private ReglasController service;

    @PostConstruct
    public void init() {
        service = new ReglasController();
        resultados = new double[3];
        this.productoService = new ProductoController();
        this.listproducto = productoService.selectAll();

    }

    public String addCarrito(int id) {
        int cant = 1;
        Producto producto = (Producto) productoService.selectRegister(String.valueOf(id));
        
        List<Regla> reglasVigentes= service.selectAll();
//        List<Regla> reglasVigentes= service.selectAll();
        for (Regla reglasVigente : reglasVigentes) {
            if (producto.getCategoria().getIdCategoria()==reglasVigente.getCategoria().getIdCategoria()) {
                producto.setPrecio(producto.getPrecio()-reglasVigente.getTax_Precio());
            }
        }
        for (int i = 0; i < productosCarrito.size(); i++) {
            if (productosCarrito.get(i).getProducto().getIdProductos() == producto.getIdProductos()) {
                cant = productosCarrito.get(i).getCantidad() + 1;
                productosCarrito.get(i).setCantidad(cant);
            }
        }
        if (cant == 1) {
            Carrito carrito = new Carrito();
            carrito.setProducto(producto);
            carrito.setCantidad(1);
            carrito.setIdCarrito(productosCarrito.size() + 1);
            carrito.setPrecio(producto.getPrecio());
            productosCarrito.add(carrito);
        }
        return "/productos";
    }

    public void verCarrito() {
        for (Carrito carrito : productosCarrito) {
            this.resultados[2] = resultados[2] + carrito.getTotal();
        }
        resultados[1] = resultados[2] * Constants.IVA_VALUE;
        resultados[0] = resultados[2] - resultados[1];

        this.redirect("carrito");
    }

    public void GenerarCompra() {
        productoService.insert(productosCarrito);
        this.redirect("productos");
    }

    public double[] getResultados() {
        return resultados;
    }

    public void setResultados(double[] resultados) {
        this.resultados = resultados;
    }

    public List<Producto> getListproducto() {
        return listproducto;
    }

    public void setListproducto(List<Producto> listproducto) {
        this.listproducto = listproducto;
    }

    public ProductoController getProductoService() {
        return productoService;
    }

    public void setProductoService(ProductoController productoService) {
        this.productoService = productoService;
    }

    public List<Carrito> getProductosCarrito() {
        return productosCarrito;
    }

    public void setProductosCarrito(List<Carrito> productosCarrito) {
        this.productosCarrito = productosCarrito;
    }

    public int getCantidad() {
        cantidad = productosCarrito.size();
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void redirect(String page) {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext
                    .getCurrentInstance().getExternalContext().getRequest();
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            request.getContextPath()
                            + "/faces/" + page + ".xhtml");
        } catch (IOException e) {
            System.err.println("Error:" + e);
        }
    }
}
