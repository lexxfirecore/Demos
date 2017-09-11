package com.demo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BookManager {

    private static final String PERSISTENCE_UNIT_NAME = "BookManagerUnit";
    private static EntityManagerFactory factory;

    public static List<Book> getBooks() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();

        em.getTransaction().begin();

        Book book = new Book();
        book.setTitle("JPA 2.1 Book");
        book.setAuthor("Manning");
        book.setIsbn("045681325435");
        em.persist(book);
        em.getTransaction().commit();


        em.getTransaction().begin();
        Book book1 = em.find(book.getClass(), book.getId());
        List<Book> books = new ArrayList<>();
        books.add(book1);
        em.close();
        return books;
    }

    public static void main(String[] args) {
        getBooks();
        for (Book book : getBooks()) {
            System.out.println(book.getId());
            System.out.println(book.getAuthor());
            System.out.println(book.getTitle());
            System.out.println(book.getIsbn());
        }
    }

}