package com.ctrl.dscatalog.repositories;

import com.ctrl.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private long existingId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
    }

    @Test
    public void deleteShouldDeleteWhenIdExists() {
        Product product = repository.findById(existingId).get();
        repository.delete(product);
        Optional<Product> result = repository.findById(existingId);
        Assertions.assertTrue(result.isEmpty());
    }
}
