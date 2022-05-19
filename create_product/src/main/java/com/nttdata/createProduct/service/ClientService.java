package com.nttdata.createProduct.service;

import java.util.List;

import com.nttdata.createProduct.entity.Client;

public interface ClientService {
    List<Client> getAll();
    Client createClient(Client new_client);
}
