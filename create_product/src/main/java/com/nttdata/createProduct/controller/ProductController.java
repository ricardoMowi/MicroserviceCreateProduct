package com.nttdata.createProduct.controller;

import java.sql.Date;
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

            Client current_cli = Client.class.cast(client_doc.get());
            
            //Existe, obtener cantidad de cuentas
            List <Product> Products_1 = productRepo.findByProductTypeAndClientId("SAVING_ACCOUNT",id);  
            List <Product> Products_2 = productRepo.findByProductTypeAndClientId("CURRENT_ACCOUNT",id);  
            List <Product> Products_3 = productRepo.findByProductTypeAndClientId("FIXED_TERM_ACCOUNT",id);  
            int Q_1 =  Products_1.size();
            int Q_2 =  Products_2.size();
            int Q_3 =  Products_3.size();
            
            //Armar hashmap
            map.put("message", "Id de cliente encontrado");
            map.put("IdClient", id);
            map.put("ClientType", current_cli.getClientType());
            map.put("cant_cuenta_ahorro", Q_1);
            map.put("cant_cuenta_corriente", Q_2);
            map.put("cant_cuenta_plazo_fijo", Q_3);
        }else{
            map.put("message", "Id de cliente no encontrado");
        }
        return map;
    }


    //Clase interna para crear cuenta del tipo cuenta de ahorro
    public HashMap<String, Object> createSavingAccount (@RequestBody Product new_product, int cant_cuentas, String ClientType  ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            if(ClientType.equals("BUSINESS")){
                map.put("mensaje", "Cuenta de ahorro no habilitada para empresas.");
            }
            else if(ClientType.equals("PERSON") && cant_cuentas == 0){
                new_product.setMaintenanceCommission(0.0);                 
                productRepo.save(new_product);
                map.put("account", new_product);
            }else{
                map.put("mensaje", "El cliente ya tiene una cuenta de ahorro");
            }

        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }                    
        return map;
    }

    
    //Clase interna para crear cuenta del tipo cuenta de ahorro VIP
    public HashMap<String, Object> createSavingAccountVIP (@RequestBody Product new_product, String ClientType  ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            //Validar si tiene tarjeta de credito
            List <Product> cards = productRepo.findByProductTypeAndClientId("CREDIT_CARD",new_product.getClientId());  
            int cant_tarjetas = cards.size();

            if(ClientType.equals("BUSINESS")){
                map.put("mensaje", "Cuenta de ahorro VIP no habilitada para empresas.");
            }else if(ClientType.equals("PERSON")){
                if(cant_tarjetas == 0){
                    map.put("mensaje", "El cliente no tiene tarjeta de crédito");
                }
                else{
                    new_product.setMaintenanceCommission(0.0);                 
                    productRepo.save(new_product);
                    map.put("account", new_product);
                }
            }

        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }                    
        return map;
    }

    //Clase interna para crear cuenta del tipo cuenta de ahorro VIP
    public HashMap<String, Object> createCurrentAccountPYME (@RequestBody Product new_product, String ClientType  ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            //Validar si tiene tarjeta de credito
            List <Product> Products = productRepo.findByProductTypeAndClientId("CREDIT_CARD",new_product.getClientId());  
            int cant_tarjetas = Products.size();

            if(ClientType.equals("PERSON")){
                map.put("mensaje", "Cuenta corriente PYME no habilitada para personas.");
            }else if(ClientType.equals("BUSINESS")){
                if(cant_tarjetas == 0){
                    map.put("mensaje", "El cliente no tiene tarjeta de crédito");
                }
                else{
                    new_product.setMaintenanceCommission(0.0);                 
                    productRepo.save(new_product);
                    map.put("account", new_product);
                }
            }

        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }                    
        return map;
    }
    
            
    //Clase interna para crear cuenta del tipo cuenta corriente
    public HashMap<String, Object> createCurrentAccount(@RequestBody Product new_product,  int cant_cuentas, String ClientType  ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            if(ClientType.equals("BUSINESS")){
                map.put("mensaje", "Cuenta corriente no habilitada para empresas.");
            }
            else if(ClientType.equals("PERSON") && cant_cuentas == 0){
                new_product.setMaximumTransactionLimit(0); 
                productRepo.save(new_product);
                map.put("account", productRepo.save(new_product));
            }else{
                map.put("mensaje", "El cliente ya tiene una cuenta corriente.");
            }
        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }             
        return map;
    }    
    //Clase interna para crear cuenta del tipo cuenta plazo fijo
    public HashMap<String, Object> createFixedTermAccount(@RequestBody Product new_product, String ClientType ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            if(ClientType.equals("BUSINESS")){
                map.put("mensaje", "Cuenta de plazo fijo no habilitada para empresas.");
            }else{
                new_product.setMaintenanceCommission(0.0); 
                new_product.setMaximumTransactionLimit(0);  
                productRepo.save(new_product);
                map.put("account", new_product);
            }
            
        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }                    
        return map;
    }
    //Clase interna para crear creditos
    public HashMap<String, Object> createCredit(@RequestBody Product new_product, String ClientType, String IdClient ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            List <Product> Products_1 = productRepo.findByProductTypeAndClientId("BUSINESS_CREDIT",IdClient);  
            List <Product> Products_2 = productRepo.findByProductTypeAndClientId("PERSONAL_CREDIT",IdClient);  

            if(ClientType.equals("BUSINESS") && Products_1.size() == 0){
               new_product.setProductType("BUSINESS_CREDIT");
               productRepo.save(new_product);
               map.put("account", new_product);
            }
            else if(ClientType.equals("PERSON")&& Products_2.size() == 0){
               new_product.setProductType("PERSONAL_CREDIT");
               productRepo.save(new_product);
               map.put("account", new_product);
            }else{
                map.put("mensaje", "El cliente ya tiene un producto de crédito habilitado."); 
            }            
            
            
        }catch(Exception e) {
            e.printStackTrace();
            map.put("mensaje", "error");
        }                    
        return map;
    }
    //Clase interna para crear una tarjeta de crédito
    public HashMap<String, Object> createCreditCard(@RequestBody Product new_product, String ClientType, String IdClient ){
        HashMap<String, Object> map = new HashMap<>();
        try{
            List <Product> Products_1 = productRepo.findByProductTypeAndClientId("CREDIT_CARD",IdClient);  

            if(Products_1.size() == 0){
               new_product.setProductType("BUSINESS_CREDIT");
               productRepo.save(new_product);
               map.put("account", new_product);
            }else{
                map.put("mensaje", "El cliente ya tiene una tarjeta de crédito habilitada."); 
            }                 
            
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
            
            String ClientType= (data_client.get("ClientType")).toString();         
            String IdClient= (data_client.get("IdClient")).toString();      
            
            int cant_cuenta_ahorro= (int) data_client.get("cant_cuenta_ahorro");
            int cant_cuenta_corriente= (int) data_client.get("cant_cuenta_corriente");
            //int cant_cuenta_plazo_fijo=(int) data_client.get("cant_cuenta_plazo_fijo");

            log.info("entro al else");
            String productType = new_product.getProductType();
            log.info(productType);

            //Asignar fecha de creación
            java.util.Date date = new java.util.Date();
            new_product.setCreationDate(date);
            //Asignar status
            new_product.setStatus("ACTIVE");

            //Productos del tipo cuenta
            if(productType.equals("CURRENT_ACCOUNT" )){
                log.info("1");
                HashMap<String, Object> create_product_a = createCurrentAccount(  new_product, cant_cuenta_corriente, ClientType );
                salida.put("ouput", create_product_a);
            }else if(productType.equals("SAVING_ACCOUNT")){
                log.info("2");
                HashMap<String, Object> create_product_b = createSavingAccount(  new_product,  cant_cuenta_ahorro, ClientType);
                salida.put("ouput", create_product_b);
            }else if(productType.equals("SAVING_ACCOUNT_VIP")){
                log.info("2");
                HashMap<String, Object> create_product_b = createSavingAccountVIP(  new_product,   ClientType);
                salida.put("ouput", create_product_b);
            }else if(productType.equals("SAVING_ACCOUNT_VIP")){
                log.info("2");
                HashMap<String, Object> create_product_b = createCurrentAccountPYME(  new_product,   ClientType);
                salida.put("ouput", create_product_b);
            }else if(productType.equals("FIXED_TERM_ACCOUNT")){
                log.info("3");
                HashMap<String, Object> create_product_c = createFixedTermAccount(  new_product, ClientType );
                salida.put("ouput", create_product_c);
            }
            //Productos del tipo crédito
            else if(productType.equals("BUSINESS_CREDIT") || productType.equals("PERSONAL_CREDIT")){
                log.info("4");
                HashMap<String, Object> create_product_d = createCredit(  new_product, ClientType, IdClient );
                salida.put("ouput", create_product_d);
            }else if(productType.equals("CREDIT_CARD")){
                log.info("5");
                HashMap<String, Object> create_product_e = createCreditCard(  new_product, ClientType, IdClient );
                salida.put("ouput", create_product_e);
            }

        }  
        
        log.info("imprime nomas");
        return ResponseEntity.ok(salida);
    }












}
