package com.dxc.customerAdapter.repository;

import com.dxc.customerAdapter.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,String> {
    @Query("select c from CustomerEntity c where c.deleted = false and c.customerId = ?1 and c.countryCode = ?2 and c.agentCode = ?3")
    public List<CustomerEntity> readCustomerById(String customerId,String countryCode,String agentCode);

    @Query("select c from CustomerEntity c where c.deleted = false and c.countryCode = ?1 and c.agentCode = ?2")
    public List<CustomerEntity> readAllCustomers(String countryCode,String agentCode);

    @Transactional
    @Modifying
    @Query("update CustomerEntity c set c.deleted = true,c.modifiedDate = now() where c.id in ?1 and c.deleted = false and c.countryCode = ?2")
    int deleteMulti(List<String> ids, String countryCode);

    @Transactional
    @Modifying
    @Query("update CustomerEntity c set c.deleted = true , c.modifiedDate = now() where c.customerId = ?1 and c.countryCode = ?2 and c.agentCode = ?3")
    int deleteByCustomerId(String customerId,String countryCode,String agentCode);
}
