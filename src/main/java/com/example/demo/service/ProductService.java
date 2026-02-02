package com.example.demo.service;

import com.example.demo.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    private final Map<Long, Product> db = new LinkedHashMap<>();
    private final AtomicLong autoId = new AtomicLong(0);

    public ProductService() {
    save(new Product(null, "Chuột Logitech", new BigDecimal("199000"), 10, 1L, "Chuột không dây", null));
    save(new Product(null, "Nồi cơm điện", new BigDecimal("890000"), 5, 2L, "Nồi cơm 1.8L", null));
}


    public List<Product> findAll() {
        return new ArrayList<>(db.values());
    }

    public Product findById(Long id) {
        return db.get(id);
    }

    public Product save(Product p) {
        if (p.getId() == null) {
            p.setId(autoId.incrementAndGet());
        }
        db.put(p.getId(), p);
        return p;
    }

    public boolean existsById(Long id) {
        return db.containsKey(id);
    }

    public void deleteById(Long id) {
        db.remove(id);
    }
}
