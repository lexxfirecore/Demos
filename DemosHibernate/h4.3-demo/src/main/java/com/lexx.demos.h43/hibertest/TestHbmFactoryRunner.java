package com.edifecs.hibertest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.HashMap;
import java.util.List;

/**
 * Created by c-ionnmoro on 19-Nov-16.
 */
public class TestHbmFactoryRunner extends GenericRunner {

    private SessionFactory sessionFactory;

    public void configure() {
        try {
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.default_entity_mode", "dynamic-map");
            configuration.configure("/test.hibernate.cfg.xml");
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(builder.build());

        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Override
    public void run() {
        if (sessionFactory == null)
            return;
        System.out.println("-----------------------------------");
        System.out.println("Running TestHbmFactoryRunner.");
        Session session = sessionFactory.openSession();
        System.out.println(session.isOpen());
        session.beginTransaction();
        List result = session.createQuery("from an_entity ").list();
        System.out.println(result.size());
        for (TestEntity testEntity : (List<TestEntity>) result ) {
            System.out.println("TestEntity (" + testEntity.getId() + ") : " + testEntity.getName());
        }
        HashMap<String, Object> insertMap = new HashMap<String, Object>();
        insertMap.put("id", 11L);
        insertMap.put("name", "bla bla bla");
        session.save("tentity", insertMap);
        //session.save("an_entity", insertMap);
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
    }

    private void saveNewEntity() {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            TestEntity test = new TestEntity();
            test.setName("my test");
            session.save(test);
            tx.commit();
            System.out.println("get ID from detached bean : "+test.getId());
        } catch (Exception ex) {
            if(tx!=null)tx.rollback();
        } finally{
            session.close();
        }
    }
}
