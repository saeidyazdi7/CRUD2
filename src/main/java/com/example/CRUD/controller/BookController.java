package com.example.CRUD.controller;

import com.example.CRUD.model.Book;
import com.example.CRUD.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class BookController {
    Logger logger = LoggerFactory.getLogger(BookController.class);
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Book>> getAllBooks() {
        logger.info("getAllBooks called");
        try {
            List<Book> bookList = new ArrayList<>();
            bookRepository.findAll().forEach(bookList::add);
            if (bookList.isEmpty()) {
                return new ResponseEntity<>(bookList, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bookList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBookById/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        logger.info("getBookById called");
        Optional<Book> bookData = bookRepository.findById(id);
        if (bookData.isPresent()) {
            return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        logger.info("addBook called");
        Book bookObj = bookRepository.save(book);
        return new ResponseEntity<>(bookObj, HttpStatus.OK);
    }

    @PostMapping("/updateBookById/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable Long id, @RequestBody Book newBookData) {
        logger.info("updateBookById called");
        Optional<Book> oldBookData = bookRepository.findById(id);
        if (oldBookData.isPresent()) {
            Book updatedBookData = oldBookData.get();
            updatedBookData.setTitle(newBookData.getTitle());
            updatedBookData.setAuthor(newBookData.getAuthor());
            Book bookObj = bookRepository.save(updatedBookData);
            return new ResponseEntity<>(bookObj, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<HttpStatus> deleteBookById(@PathVariable Long id) {
        logger.info("deleteBookById called");
        bookRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
