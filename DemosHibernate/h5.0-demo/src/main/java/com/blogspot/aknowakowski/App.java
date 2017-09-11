package com.blogspot.aknowakowski;

import java.util.HashMap;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class App {
    public static void main(String[] args) {
        SessionFactory sessionFactory;
        sessionFactory = new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();

        Session session = sessionFactory.openSession();

        Transaction tx = session.beginTransaction();
//        Task task = new Task();
//        task.setId(new Long(44));
//        task.setName("Hello world task");
//        task.setDescription("Hello world task description");


        Map insertMap = new HashMap();

        insertMap.put("name","Vasea");
        insertMap.put("description","ddea");


        session.save("task", insertMap);
        tx.commit();
        session.close();
    }
}