package com.example.demo.service;

import com.example.demo.model.Category;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CategoryService {

    private final Map<Long, Category> db = new LinkedHashMap<>();
    private final AtomicLong autoId = new AtomicLong(0);

    public CategoryService() {
        // seed data mẫu
        save(new Category(null, "Đồ điện tử"));
        save(new Category(null, "Đồ gia dụng"));
        save(new Category(null, "Văn phòng phẩm"));
    }

    public List<Category> findAll() {
        return new ArrayList<>(db.values());
    }

    public Category findById(Long id) {
        return db.get(id);
    }

    public Category save(Category c) {
        if (c.getId() == null) {
            c.setId(autoId.incrementAndGet());
        }
        db.put(c.getId(), c);
        return c;
    }

    public boolean existsById(Long id) {
        return db.containsKey(id);
    }

    public void deleteById(Long id) {
        db.remove(id);
    }
}
