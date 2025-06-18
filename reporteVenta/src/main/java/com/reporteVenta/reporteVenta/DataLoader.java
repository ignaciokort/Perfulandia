package com.reporteVenta.reporteVenta;

import com.reporteVenta.reporteVenta.model.Reporte;
import com.reporteVenta.reporteVenta.repository.ReporteRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DataLoader implements CommandLineRunner {

    private final ReporteRepository reporteRepository;
    private final Faker faker = new Faker();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DataLoader(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    @Override
    public void run(String... args) {
        for (int i = 0; i < 10; i++) {
            Reporte reporte = new Reporte();
            reporte.setProducto(faker.commerce().productName());
            reporte.setCantidadVendida(faker.number().numberBetween(1, 50));
            int precio = faker.number().numberBetween(5000, 30000);
            reporte.setTotalVentas(precio * reporte.getCantidadVendida());
            reporte.setFechaVenta(LocalDate.now().minusDays(faker.number().numberBetween(1, 30)).format(formatter));

            reporteRepository.save(reporte);
        }

        System.out.println("📊 Se generaron 10 reportes de venta falsos en reporteVenta.");
    }
}
