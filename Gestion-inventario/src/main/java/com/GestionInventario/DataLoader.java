package com.GestionInventario;

import com.GestionInventario.Model.Producto;
import com.GestionInventario.Repository.ProductoRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final Faker faker = new Faker();

    public DataLoader(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {
        for (int i = 0; i < 10; i++) {
            Producto producto = new Producto();
            producto.setNombre(faker.commerce().productName());
            producto.setPrecio(faker.number().numberBetween(1000, 100000)); // Precio entero en CLP
            producto.setCantidad(faker.number().numberBetween(1, 100));

            productoRepository.save(producto);
        }

        System.out.println("🧴 Se insertaron 10 productos con precios enteros en inventario.");
    }
}
