package com.ludaku.logic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ludaku.appbicis.AppbicisApplication;
import com.ludaku.appbicis.logic.CasosDeUsoCrearRutas;
import com.ludaku.appbicis.modelo.Ruta;
import com.ludaku.appbicis.modelo.Usuario;
import com.ludaku.appbicis.repositorios.RepositorioRuta;
import com.ludaku.appbicis.repositorios.RepositorioUsuario;

@SpringBootTest(classes = AppbicisApplication.class)
public class CasosDeUsoRutaTests {

    @Autowired
    CasosDeUsoCrearRutas pruebaRutaCrear;

    @Autowired
    RepositorioUsuario usuarios;

    @Autowired
    RepositorioRuta rutas;

    @BeforeEach
    public void borraBD() {

        rutas.deleteAll();
        usuarios.deleteAll();

    }

    @Test
    public void pruebaCrearRuta() {
        
        try {

            // Arrange
            Usuario u = new Usuario("Javier");
            u = usuarios.save(u);
    
            // act
            pruebaRutaCrear.crearRutas("Casa", "Ruta A casa", u.getUsuarioID());

            // assert
            List<Ruta> listaRutas = rutas.findByNombreRuta("Casa");
            assertTrue(listaRutas.size() > 0, "No se grabó la ruta");

            Ruta rutaEnBD = listaRutas.get(0);
            assertNotNull(rutaEnBD, "La ruta es NULL");
            assertEquals(rutaEnBD.getDescripcion(), "Ruta A casa", "La descripción no coincide");
            assertNotNull(rutaEnBD.getAutor(), "La ruta tiene el autor en NULL");
            assertEquals(rutaEnBD.getAutor().getNombreU(), "Javier", "El nombre del autor no coincide");


        } catch (Exception e) {
            fail("El programa fallo y no deberia");
        }

    }

    @Test
    public void pruebaCrearRutaConUsuarioInexistente() {

        try {

            pruebaRutaCrear.crearRutas("Casa", "Ruta A casa", 10L);
            
            fail("El programa creo la ruta y no deberia porque el usuario no existe");

        } catch (Exception e) {

            // valida que no se halla creado una ruta con ese nombre
            List<Ruta> listaRutas = rutas.findByNombreRuta("Casa");
            assertTrue(listaRutas.size() == 0, "Se grabó una ruta y no debía");

            // Ok
        }

    }

}
