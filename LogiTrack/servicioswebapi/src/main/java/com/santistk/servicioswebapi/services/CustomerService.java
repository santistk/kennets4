package com.santistk.servicioswebapi.services;

import com.santistk.servicioswebapi.models.Customer;
import com.santistk.servicioswebapi.repositories.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CustomerService {
    
    @Inject
    private CustomerRepository customerRepository;

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByTaxId(String taxId) {
        return customerRepository.findByTaxId(taxId);
    }

    public List<Customer> getAll() {
        return customerRepository.getAll();
    }

    public Optional<Customer> save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }
}
