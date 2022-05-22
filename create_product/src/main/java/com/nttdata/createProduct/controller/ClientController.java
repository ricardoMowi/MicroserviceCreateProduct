package com.nttdata.createProduct.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.nttdata.createProduct.entity.Client;
import com.nttdata.createProduct.repository.ClientRepository;
import com.nttdata.createProduct.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepo;

    //Variables
    private static final String GET_ALL_SERVICE = "MC1";

    
    //CRUD
    @GetMapping(value = "/all")
    public List<Client> getAll() {
        log.info("lista todos");
        return clientService.getAll();
    }  

    @GetMapping("getClient/{id}")
    @CircuitBreaker(name = GET_ALL_SERVICE, fallbackMethod = "customerContactInfoFallback")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getClientData(@PathVariable("id") String id){
      Map<String, Object> salida = new HashMap<>();
      Optional<Client> client_doc = clientRepo.findById(id);
      if (client_doc.isPresent()) {
        salida.put("client", client_doc);
      }else{
        salida.put("status", "Id de Cliente no encontrado");
      }
      return ResponseEntity.ok(salida);
    }



    @PostMapping(value = "/create")
    public Client createClient(@RequestBody Client new_client){
        new_client.setStatus("ACTIVE");
        return clientService.createClient(new_client);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable("id") String id, @RequestBody Client temp) {
      Optional<Client> client = clientRepo.findById(id);
      if (client.isPresent()) {
        temp.setId(id);
        return new ResponseEntity<>(clientService.createClient(temp), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    }

    @PutMapping("setInactive/{id}")
    public ResponseEntity<Client> setInactive(@PathVariable("id") String id, @RequestBody Client temp_client) {
      Optional<Client> client_doc = clientRepo.findById(id);
      if (client_doc.isPresent()) {
        Client _client = client_doc.get();
        _client.setStatus("INACTIVE");
        return new ResponseEntity<>(clientRepo.save(_client), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } 

    public ResponseEntity<String> customerContactInfoFallback(Exception e) {
      return new ResponseEntity<String>("GET: Client contact info endpoint is not available right now.", HttpStatus.OK);
    }

}
