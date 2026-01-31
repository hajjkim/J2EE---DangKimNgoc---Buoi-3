package com.example.demo.service;

import com.example.demo.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final List<Book> books = new ArrayList<>();
    private long nextId = 1;

    public BookService() {
        addBook(new Book(0, "Dế Mèn Phiêu Lưu Ký", "Tô Hoài"));
        addBook(new Book(0, "Harry Potter", "J.K. Rowling"));
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public void addBook(Book book) {
        book.setId((int) nextId++);
        books.add(book);
    }

    public Optional<Book> getBookById(long id) {
        return books.stream()
                .filter(b -> b.getId() == (int) id)
                .findFirst();
    }

    public void updateBook(Book updatedBook) {
        books.stream()
                .filter(b -> b.getId() == updatedBook.getId())
                .findFirst()
                .ifPresent(b -> {
                    b.setTitle(updatedBook.getTitle());
                    b.setAuthor(updatedBook.getAuthor());
                });
    }

    public void deleteBook(long id) {
        books.removeIf(b -> b.getId() == (int) id);
    }
}
