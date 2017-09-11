package com.edifecs.hibertest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by c-ionnmoro on 19-Nov-16.
 */
public class TestHbmFactoryRunner extends GenericRunner {

    private SessionFactory sessionFactory;

    public void configure() throws IOException {
        System.out.println("Configuring SessionFactory.");
        Configuration configuration = new Configuration();
        configuration.configure("/test.hibernate.cfg.xml");
        configuration.setProperty("hibernate.default_entity_mode", "dynamic-map");
        sessionFactory = configuration.buildSessionFactory();
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
        List<Map<String, Object>> result = session.createQuery("from tentity ").list();
        System.out.println(result.size());
        for (Map<String, Object> testEntity : result ) {
            testEntity.forEach((k,v) -> System.out.print(k + ":" + v + ",  "));
        }

        List<Map<String, Object>> result2 = session.createQuery("from " + TestEntityA.class.getName()).list();
        System.out.println(result.size());
        for (Map<String, Object> testEntity2 : result ) {
            testEntity2.forEach((k,v) -> System.out.print(k + ":" + v + ",  "));
        }

        HashMap<String, Object> insertMap = new HashMap<String, Object>();
        insertMap.put("id", 11L);
        insertMap.put("name", "bla bla bla");
        session.merge("tentity", insertMap);

        HashMap<String, Object> insertMap2 = new HashMap<String, Object>();
        insertMap2.put("id", 12L);
        insertMap2.put("name", "rgdde");
        TestEntityA ent = new TestEntityA();
        ent.setId(13L);
        ent.setName("demo");
        session.save("test_entity_a", insertMap);

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
