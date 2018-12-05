package com.dxc.customerAdapter.delegate;

import com.dxc.customerAdapter.common.StorageError;
import com.dxc.customerAdapter.exception.CustomerException;
import com.dxc.customerAdapter.service.CustomerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import sun.management.Agent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    private final String COUNTRY_CODE = "VN";
    private final String AGENT_CODE = "go";
    private final String JSON_DOC = "[{\n" +
            "\"id\": \"dd\",\n" +
            "\"customerId\": \"cus1\",\n" +
            "\"agentCode\": \"go\",\n" +
            "\"countryCode\" : \"VN\",\n" +
            "\"json\": {\"firstName\": \"lan\",\"lastName\": \"Hu?nh\"}\n" +
            "}]";
    private final String JSON_NULL = "[]";
    private final String RESULT = "";
    private final String CUSTOMER_ID = "abc";
    private final List<String> RS = Arrays.asList("1761f3cb-f5ed-4eb8-ba4e-7cfeb7b3439f") ;
    private final List<String> LIST_ID = Arrays.asList("46bcece0-35e1-4d03-96a4-cff1a06e141b","810c58b5-16ae-47c7-8d18-6dcaf78f9931","95610aaf-d92d-4693-bccb-b7c22f20ae29");
    private final List<String> LIST_NULL = Arrays.asList();

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerDelegate customerDelegate;

    @Test
    public void upsertCustomerOldCustomerNull(){
        when(customerService.upsertCustomer(JSON_DOC,COUNTRY_CODE,AGENT_CODE)).thenReturn(RS);
        ResponseEntity<List<String>> rs = customerDelegate.upsertCustomer(JSON_DOC,COUNTRY_CODE,AGENT_CODE);
        Assert.assertEquals(rs.getBody(),RS);
    }

    @Test(expected = CustomerException.class)
    public void upsertCustomerNull(){
       customerDelegate.upsertCustomer(JSON_NULL,COUNTRY_CODE,AGENT_CODE);
    }

    @Test(expected = CustomerException.class)
    public void readCustomerById(){
        when(customerService.readCustomerById(CUSTOMER_ID,COUNTRY_CODE,AGENT_CODE)).thenThrow(new CustomerException("", StorageError.NOT_FOUND));
        customerDelegate.readCustomerById(CUSTOMER_ID,COUNTRY_CODE,AGENT_CODE);
    }

    @Test
    public void deleteMulti(){
        String Expected = LIST_ID.stream().collect(Collectors.joining(",","[","]"));
        when(customerService.deleteMultipleCustomer(LIST_ID,COUNTRY_CODE)).thenReturn(Expected);
        ResponseEntity<String>rs = customerDelegate.deleteMultipleCus(LIST_ID,COUNTRY_CODE);
        Assert.assertEquals(rs.getBody(),Expected);
    }

    @Test(expected = CustomerException.class)
    public void deleteMultiNull() throws CustomerException{
      //  when(customerService.deleteMultipleCustomer(LIST_NULL,COUNTRY_CODE)).thenReturn(null);
          customerDelegate.deleteMultipleCus(LIST_NULL,COUNTRY_CODE);
    }

    @Test
    public void readByCustomerID(){
        when(customerService.readCustomerById(CUSTOMER_ID,COUNTRY_CODE,AGENT_CODE)).thenReturn(RESULT);
        ResponseEntity<String> rs = customerDelegate.readCustomerById(CUSTOMER_ID,COUNTRY_CODE,AGENT_CODE);
        Assert.assertEquals(rs.getBody(),RESULT);
    }

}
