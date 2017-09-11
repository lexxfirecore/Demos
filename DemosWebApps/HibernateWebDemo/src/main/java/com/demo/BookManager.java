package com.demo;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class BookManager {

    public static List<Book> getBooks() {
        final Configuration configuration = new Configuration().configure();
        final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        final SessionFactory factory = configuration.buildSessionFactory(builder.build());
        final Session session = factory.openSession();
        final Book book = new Book("93939398948 ", "Java 8", "Author");
        session.beginTransaction();
        session.save(book);
        session.getTransaction().commit();
        final List<Book> books = session.createCriteria(Book.class).list();
       /* System.out.println("\n----\n");
        System.out.println(MessageFormat.format("Storing {0} books in the database", books.size()));
        for (final Book b : books) {
            System.out.println(b);
        }
        System.out.println("\n----\n");
        */
        session.close();
        factory.close();
        return books;
    }

}