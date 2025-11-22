package com.santistk.servicioswebapi.repositories;

import com.santistk.servicioswebapi.models.Customer;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CustomerRepository extends BaseRepository<Customer, Long> {
    
    @Override
    protected Class<Customer> entity() {
        return Customer.class;
    }

    public Optional<Customer> findByTaxId(String taxId) {
        try {
            Customer customer = entityManager.createQuery(
                "SELECT c FROM Customer c WHERE c.taxId = :taxId", Customer.class)
                .setParameter("taxId", taxId)
                .getSingleResult();
            return Optional.of(customer);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
