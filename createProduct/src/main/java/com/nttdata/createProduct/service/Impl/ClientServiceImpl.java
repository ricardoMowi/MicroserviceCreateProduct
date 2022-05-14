package com.nttdata.createProduct.service.Impl;

import java.util.List;

import com.nttdata.createProduct.entity.Client;
import com.nttdata.createProduct.repository.ClientRepository;
import com.nttdata.createProduct.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClientServiceImpl implements ClientService{
    @Autowired
    private ClientRepository clientRepository;
    
    @Override
    public List<Client> getAll() {
      return clientRepository.findAll();
    }

    @Override
    public Client createClient(Client client){
      return clientRepository.save(client);
    }


}
