package com.dxc.customerAdapter.service;

import com.dxc.customerAdapter.api.model.Customer;
import com.dxc.customerAdapter.api.model.CustomerJson;
import com.dxc.customerAdapter.entity.CustomerEntity;
import com.dxc.customerAdapter.exception.CustomerException;
import com.dxc.customerAdapter.repository.CustomerRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.dxc.customerAdapter.common.StorageError.JSON_PARSE_FAIL;
import static com.dxc.customerAdapter.common.StorageError.NOT_FOUND;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Transactional
    public List<String> upsertCustomer(String customers, String countryCode, String agentCode) {
        List<String> listId = new ArrayList<>();
        Customer cus = new Customer();
        try {
            List<Customer> newCustomers = objectMapper.readValue(customers, new TypeReference<List<Customer>>(){});
            logger.info(newCustomers.toString());
            if (newCustomers.size() == 0) throw new CustomerException("dont have any customer in json you provided",JSON_PARSE_FAIL);
            for (Customer customerTempt : newCustomers) {
                customerTempt.setCountryCode(countryCode);
                customerTempt.setAgentCode(agentCode);
                CustomerEntity oldCustomer = customerRepository.findOne(customerTempt.getId());
                if (oldCustomer == null) {
                    String id = insertCustomer(customerTempt) ;
                    listId.add(id + " inserted");
                } else {
                    customerTempt.setCreateDate(LocalDate.fromDateFields(oldCustomer.getCreateDate()));
                    customerTempt.setModifiDate(LocalDate.fromDateFields(new Date()));
                    customerTempt.setDeleted(false);
                    oldCustomer = convertToCustomerEntity(customerTempt);
                    customerRepository.save(oldCustomer);
                    listId.add(oldCustomer.getId() + " updated");
                }
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listId;
    }

    public String readAllCustomer(String countryCode,String agentCode){
        List<CustomerEntity> customers = customerRepository.readAllCustomers(countryCode,agentCode);
        if(customers.size() == 0) throw new CustomerException("not found",NOT_FOUND);
       return customers.stream().map(c ->toJSon(converToCustomer(c))).collect(Collectors.joining(",","[","]"));
    }

    @Transactional
    public String deleteCustomer(String cusId, String countryCode, String agentCode){
        int count = customerRepository.deleteByCustomerId(cusId,countryCode,agentCode);
        if(count <= 0 ){
            throw new CustomerException("not found",NOT_FOUND);
        }
        return cusId;
       // List<String> rs = new ArrayList<>();
//        List<CustomerEntity> listCustomer = customerRepository.readCustomerById(cusId,countryCode,agentCode);
//        if(listCustomer == null) throw new CustomerException("Not found",NOT_FOUND);
//        for(CustomerEntity temptCus : listCustomer){
//            temptCus.setDeleted(true);
//            customerRepository.save(temptCus);
//            rs.add(temptCus.getId() + " deleted");
//        }
//        return rs.stream().collect(Collectors.joining(",","[","]"));
    }

    public String readCustomerById(String customerId, String countryCode, String agentCode){
        List<CustomerEntity> listCustomerEntity = customerRepository.readCustomerById(customerId,countryCode,agentCode);
        if(listCustomerEntity.size() == 0){ throw new CustomerException("id: "+ customerId + " Not found",NOT_FOUND);}
        return listCustomerEntity.stream().map(c->toJSon(converToCustomer(c))).collect(Collectors.joining(",","[","]"));
    }

    @Transactional
    public String deleteMultipleCustomer(List<String> listId,String countryCode){
        int count = customerRepository.deleteMulti(listId,countryCode);
        if(count <= 0 ) throw new CustomerException("not found",NOT_FOUND);
        return listId.stream().collect(Collectors.joining(",","[","]"));
    }

    private CustomerEntity convertToCustomerEntity(Customer customer){
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customer.getId());
        customerEntity.setCustomerId(customer.getCustomerId());
        customerEntity.setAgentCode(customer.getAgentCode());
        customerEntity.setCountryCode(customer.getCountryCode());
        customerEntity.setFirstName(customer.getJson().getFirstName());
        customerEntity.setLastName(customer.getJson().getLastName());
        customerEntity.setModifiedDate(customer.getModifiDate().toDate());
        customerEntity.setCreateDate(customer.getCreateDate().toDate());
        return customerEntity;
    }

    private String toJSon(Customer customer){
        try {
            return objectMapper.writeValueAsString(customer);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new CustomerException("cant parse to json",JSON_PARSE_FAIL);
        }

    }

    private Customer converToCustomer(CustomerEntity customerEntity){
        Customer  customer = new Customer();
        customer.setId(customerEntity.getId());
        customer.setAgentCode(customerEntity.getAgentCode());
        customer.setCustomerId(customerEntity.getCustomerId());
        customer.setCountryCode(customerEntity.getCountryCode());
        customer.setCreateDate(LocalDate.fromDateFields(customerEntity.getCreateDate()));
        customer.setModifiDate(LocalDate.fromDateFields(customerEntity.getModifiedDate()));
        customer.setDeleted(customerEntity.isDeleted());
        CustomerJson cusJson = new CustomerJson();
        cusJson.setFirstName(customerEntity.getFirstName());
        cusJson.setLastName(customerEntity.getLastName());
        customer.setJson(cusJson);
        return customer;
    }

    private String insertCustomer(Customer customer){
        try{
            String id = UUID.randomUUID().toString();
            customer.setModifiDate(LocalDate.fromDateFields(new Date()));
            customer.setCreateDate(LocalDate.fromDateFields(new Date()));
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity = convertToCustomerEntity(customer);
            customerEntity.setId(id);
            customerEntity.setDeleted(false);
            customerRepository.saveAndFlush(customerEntity);
            return id;
        }catch(Exception ex){
            throw new  CustomerException("bad request",JSON_PARSE_FAIL);
        }

    }

}
