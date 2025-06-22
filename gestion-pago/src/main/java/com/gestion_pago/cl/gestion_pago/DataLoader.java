package com.gestion_pago.cl.gestion_pago;

import com.gestion_pago.cl.gestion_pago.model.Usuario;
import com.gestion_pago.cl.gestion_pago.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final Faker faker = new Faker();

    public DataLoader(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        for (int i = 0; i < 10; i++) {
            Usuario usuario = new Usuario();
            usuario.setNro_cuenta(faker.finance().iban().substring(0, 16));
            usuario.setProductoId(faker.number().numberBetween(1L, 20L)); // ID válido y seguro
            usuario.setNombre(faker.name().firstName());
            usuario.setApellido(faker.name().lastName());
            usuario.setCorreo(faker.internet().emailAddress());
            usuario.setFechaNacimiento(LocalDate.now().minusYears(faker.number().numberBetween(18, 60)));
            usuario.setMonto(faker.number().randomDouble(2, 1000, 50000)); // valor con 2 decimales

            usuarioRepository.save(usuario);
        }

        System.out.println("Se cargaron 10 usuarios de pago falsos con IDs válidos de productos.");
    }
}
