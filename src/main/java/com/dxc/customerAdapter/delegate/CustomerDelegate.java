package com.dxc.customerAdapter.delegate;

import com.dxc.customerAdapter.api.V1ApiDelegate;
import com.dxc.customerAdapter.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerDelegate implements V1ApiDelegate {
    @Autowired
    private CustomerService customerService;

    @Override
    public ResponseEntity<String> deleteCustomer(String customerId, String countryCode, String agentCode) {
        return ResponseEntity.ok(customerService.deleteCustomer(customerId,countryCode,agentCode));
    }

    @Override
    public ResponseEntity<String> deleteMultipleCus(List<String> listId, String countryCode) {
        return ResponseEntity.ok(customerService.deleteMultipleCustomer(listId,countryCode));
    }

    @Override
    public ResponseEntity<String> readAllCustomer(String countryCode, String agentCode) {
        return ResponseEntity.ok(customerService.readAllCustomer(countryCode,agentCode));
    }

    @Override
    public ResponseEntity<String> readCustomerById(String customerId, String countryCode, String agentCode) {
        return ResponseEntity.ok(customerService.readCustomerById(customerId,countryCode,agentCode));
    }

    @Override
    public ResponseEntity<List<String>> upsertCustomer(String customer, String countryCode, String agentCode) {
        return ResponseEntity.ok(customerService.upsertCustomer(customer,countryCode,agentCode));
    }
}
