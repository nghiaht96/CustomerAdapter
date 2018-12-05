package com.dxc.customerAdapter.handle;

import com.dxc.customerAdapter.exception.CustomerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class HandleCustomerException {
    @Autowired
    private ObjectMapper objectMapper;
    @ExceptionHandler(value = CustomerException.class)
    public final ResponseEntity<String> customException(CustomerException ex, WebRequest web){
        ErrorDetails errorDetails = new ErrorDetails(new Date(),ex.getMessage(),web.getDescription(false));
        try {
            String rs = objectMapper.writeValueAsString(errorDetails);
            return new ResponseEntity<>(rs,ex.getStorageError().getStatus());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
