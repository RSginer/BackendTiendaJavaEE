package com.apiTienda.datos;

import com.apiTienda.modelo.Producto;
import com.apiTienda.modelo.Review;
import com.apiTienda.modelo.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para el manejo de datos con la base de datos
 *
 * @author Rubén
 *
 */
public class JDBC {

    private Connection conn;

    public JDBC() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiendaAngularJS", "root", "root");
    }

    /**
     * Busca en la base de datos todos los productos y los retorna en una lista
     *
     * @return devuelve una lista de objetos producto
     * @throws SQLException
     */
    public List<Producto> obtenerProductos() throws SQLException {
        List<Producto> listaProductos = new ArrayList<>();
        String sqlProductos = "SELECT * FROM producto";
        PreparedStatement psProductos = this.conn.prepareStatement(sqlProductos);
        ResultSet resProducto = psProductos.executeQuery();
        while (resProducto.next()) {
            Producto p = new Producto(Integer.parseInt(resProducto.getString("id")),
                    resProducto.getString("nombre"),
                    resProducto.getString("descripcion"),
                    resProducto.getString("imagen"),
                    Integer.parseInt(resProducto.getString("stock")),
                    Double.parseDouble(resProducto.getString("precio")),
                    Double.parseDouble(resProducto.getString("precioAntes")));
            p.setReviews(this.obtenerReviewsPorProducto(Integer.parseInt(resProducto.getString("id"))));
            listaProductos.add(p);
        }
        return listaProductos;
    }

    /**
     * Dado un id de producto devuelve una lista con todas sus reviews que
     * obtiene de la base de datos
     *
     * @param idProducto
     * @return devuelve un List de reviews
     * @throws SQLException
     */
    public List<Review> obtenerReviewsPorProducto(int idProducto) throws SQLException {
        List<Review> listaReviews = new ArrayList<>();
        String sqlReviews = "SELECT * FROM review where idproducto=?";
        PreparedStatement psReviews = this.conn.prepareStatement(sqlReviews);
        psReviews.setInt(1, idProducto);
        ResultSet resReviews = psReviews.executeQuery();
        while (resReviews.next()) {
            Usuario user = this.obtenerUsuarioPorId(Integer.parseInt(resReviews.getString("idusuario")));
            Review r = new Review(Integer.parseInt(resReviews.getString("id")),
                    user.getNombre(),
                    user.getImagen(),
                    resReviews.getString("comentario"),
                    Integer.parseInt(resReviews.getString("estrellas")),
                    resReviews.getDate("fecha")
            );
            listaReviews.add(r);
        }
        return listaReviews;
    }

    /**
     * Obtiene un usuario dado un id
     *
     * @param idUsuario
     * @return devuelve un objeto Usuario
     * @throws SQLException
     */
    public Usuario obtenerUsuarioPorId(int idUsuario) throws SQLException {
        Usuario user = new Usuario();
        String sqlUsuarios = "SELECT * FROM usuario where id=?";
        PreparedStatement psUsuario = this.conn.prepareStatement(sqlUsuarios);
        psUsuario.setInt(1, idUsuario);
        ResultSet resUsuarios = psUsuario.executeQuery();
        resUsuarios.next();
        user.setImagen(resUsuarios.getString("imagen"));
        user.setNombre(resUsuarios.getString("nombre"));
        return user;
    }

}
