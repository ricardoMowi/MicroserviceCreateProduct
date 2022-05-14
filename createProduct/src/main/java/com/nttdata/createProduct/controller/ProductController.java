package com.nttdata.createProduct.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.nttdata.createProduct.entity.Client;
import com.nttdata.createProduct.entity.Product;
import com.nttdata.createProduct.repository.ClientRepository;
import com.nttdata.createProduct.repository.ProductRepository;
import com.nttdata.createProduct.service.ProductService;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ClientRepository clientRepo;
    //CRUD
    @GetMapping(value = "/all")
    public List<Product> getAll() {
        return productService.getAll();
    } 

    // @PostMapping(value = "/create")
    // public Product createProduct(@RequestBody Product new_produc){
    //     new_produc.setStatus("ACTIVE");
    //     return productService.createProduct(new_produc);
    // }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") String id, @RequestBody Product temp) {
      Optional<Product> product = productRepo.findById(id);
      if (product.isPresent()) {
        temp.setId(id);
        return new ResponseEntity<>(productService.createProduct(temp), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    }

    @PutMapping("setInactive/{id}")
    public ResponseEntity<Product> setInactive(@PathVariable("id") String id) {
      Optional<Product> product_dov = productRepo.findById(id);
      if (product_dov.isPresent()) {
        Product _product = product_dov.get();
        _product.setStatus("INACTIVE");
        return new ResponseEntity<>(productRepo.save(_product), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } 


    // Crear Servicio para Registrar cuentas
    // Campos Obligatorios: Id cliente (valido) , tipo de cuenta, monto

    //Clase interna para validar cliente y cuentas
    public HashMap<String, Object> validateClient(String id) {        
        HashMap<String, Object> map = new HashMap<>();
        Optional<Client> client_doc = clientRepo.findById(id);
        if (client_doc.isPresent()) {
            //Existe, obtener cantidad de cuentas
            List <Product> Products_1 = productRepo.findByProductTypeAndClientId("SAVING_ACCOUNT",id);  
            List <Product> Products_2 = productRepo.findByProductTypeAndClientId("CURRENT_ACCOUNT",id);  
            List <Product> Products_3 = productRepo.findByProductTypeAndClientId("FIXED_TERM_ACCOUNT",id);  
            int Q_1 =  Products_1.size();
            int Q_2 =  Products_2.size();
            int Q_3 =  Products_3.size();
            
            //Armar hashmap
            map.put("message", "Id de cliente encontrado");
            map.put("cant_cuenta_ahorro", Q_1);
            map.put("cant_cuenta_corriente", Q_2);
            map.put("cant_cuenta_plazo_fijo", Q_3);
        }else{
            map.put("message", "Id de cliente no encontrado");
        }
        return map;
    }


    //Clase interna para crear cuenta del tipo cuenta ahorro
    public HashMap<String, Object> createCurrentAccount(@RequestBody Product new_product ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            new_product.setMaintenanceCommission(0.0); 
            map.put("account", new_product);
            productRepo.save(new_product);
        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }                    
        return map;
    }
    //Clase interna para crear cuenta del tipo cuenta corriente
    public HashMap<String, Object> createSavingAccount(@RequestBody Product new_product ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            new_product.setMaximumTransactionLimit(0); 
            map.put("account", new_product);
            productRepo.save(new_product);
        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }             
        return map;
    }    
    //Clase interna para crear cuenta del tipo cuenta plazo fijo
    public HashMap<String, Object> createFixedTermAccount(@RequestBody Product new_product ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            new_product.setMaintenanceCommission(0.0); 
            map.put("account", new_product);
            productRepo.save(new_product);
        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }                    
        return map;
    }


    //Microservicio para crear cuentas
    @PostMapping("createProduct")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Product new_product){

        log.info("entrando a método createProduct");
        Map<String, Object> salida = new HashMap<>();      
        HashMap<String, Object> data_client = validateClient(new_product.getClientId());  
        String message = (data_client.get("message")).toString();

        if(message == "Id de cliente no encontrado"){
            log.info("id incorrecto");
            salida.put("message", "Id de cliente no encontrado");  
        }else{
            
            String ClientType= (data_client.get("clientType")).toString();

            int cant_cuenta_ahorro= (int) data_client.get("cant_cuenta_ahorro");
            int cant_cuenta_corriente= (int) data_client.get("clientType");
            int cant_cuenta_plazo_fijo=(int) data_client.get("clientType");

            log.info("entro al else");
            String productType = new_product.getProductType();
            log.info(productType);

            if(productType.equals("CURRENT_ACCOUNT")){
                log.info("1");
                HashMap<String, Object> create_product_a = createCurrentAccount(  new_product );
                salida.put("ouput", create_product_a);
            }else if(productType == "SAVING_ACCOUNT"){
                log.info("2");
                HashMap<String, Object> create_product_b = createSavingAccount(  new_product );
                salida.put("ouput", create_product_b);
            }else if(productType == "FIXED_TERM_ACCOUNT"){
                log.info("3");
                HashMap<String, Object> create_product_c = createFixedTermAccount(  new_product );
                salida.put("ouput", create_product_c);
            }

        }  
        
        log.info("imprime nomas");
        return ResponseEntity.ok(salida);
    }












}
