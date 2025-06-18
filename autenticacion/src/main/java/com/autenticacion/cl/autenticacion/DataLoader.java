package com.autenticacion.cl.autenticacion;

import com.autenticacion.cl.autenticacion.model.Cliente;
import com.autenticacion.cl.autenticacion.repository.ClienteRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final Faker faker = new Faker();

    public DataLoader(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void run(String... args) {
        for (int i = 0; i < 10; i++) {
            Cliente cliente = new Cliente();
            cliente.setCorreoCliente(faker.internet().emailAddress());
            cliente.setContrasenaCliente(faker.internet().password(6, 10));

            clienteRepository.save(cliente);
        }

        System.out.println("✅ Se cargaron 10 clientes falsos en autenticación.");
    }
}
