package minhho.learn.libraryApp.service;

import minhho.learn.libraryApp.model.Book;
import minhho.learn.libraryApp.model.User;
import minhho.learn.libraryApp.repository.BookRepository;
import minhho.learn.libraryApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    public Book borrowBook(Long bookId, Long userId) {
        Book book = getBookOrThrow(bookId);
        User user = getUserOrThrow(userId);

        if (book.isBorrowed()) {
            throw new RuntimeException("Book is already borrowed");
        }

        book.setBorrowedBy(user);
        book.setBorrowed(true);
        return save(book);
    }

    public Book returnBook(Long bookId) {
        Book book = getBookOrThrow(bookId);

        if (!book.isBorrowed()) {
            throw new RuntimeException("Book is not currently borrowed.");
        }

        book.setBorrowed(false);
        book.setBorrowedBy(null);

        return save(book);
    }

    private Book getBookOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + bookId));
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

}
