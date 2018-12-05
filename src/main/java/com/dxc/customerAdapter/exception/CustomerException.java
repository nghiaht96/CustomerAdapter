package com.dxc.customerAdapter.exception;

import com.dxc.customerAdapter.common.StorageError;
import org.springframework.http.HttpStatus;

public class CustomerException  extends  RuntimeException{
       private static final long serialVersionUID = 1491607362490161046L;
    private final StorageError storageError;

    public CustomerException(String message, StorageError storageError) {
        super(message);
        this.storageError = storageError;
    }

    public StorageError getStorageError() {
        return storageError;
    }
}
