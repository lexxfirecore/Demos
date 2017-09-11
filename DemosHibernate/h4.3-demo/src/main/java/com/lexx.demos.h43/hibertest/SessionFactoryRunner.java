package com.edifecs.hibertest;

//import com.edifecs.domain.hibernate.tm.maintenance.Dbproperties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.DefaultNamingStrategy;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metamodel.Metadata;
import org.hibernate.metamodel.MetadataSources;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by c-ionnmoro on 16-Nov-16.
 */
public class SessionFactoryRunner extends GenericRunner {
    SessionFactory sessionFactory;

    @Override
    public void configure() throws Exception {
        System.out.println("Configuring SessionFactory.");
        Properties dialectProperties = new Properties();
        dialectProperties.load(getClass().getResourceAsStream("/hibernate.dialect.properties"));
        Properties connProps = new Properties();
        connProps.load(getClass().getResourceAsStream("/hibernate.connection.properties"));
        Configuration configuration = new Configuration();
        configuration.addProperties(dialectProperties);
        configuration.addProperties(connProps);
        configuration.setNamingStrategy(DefaultNamingStrategy.INSTANCE);
        configuration.configure("/conf/orm-mapping/active.hibernate.cfg.xml");
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();


        configuration.setProperty("hibernate.default_entity_mode", "dynamic-map");
        sessionFactory = configuration.buildSessionFactory();

        try {
            MetadataSources metadataSources = new MetadataSources(registry);
            Metadata metadata = metadataSources.buildMetadata();
            sessionFactory = metadata.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Override
    public void run() {
        System.out.println("Running SessionFactory.");
        Map<String, ClassMetadata> map =  sessionFactory.getAllClassMetadata();
        for (Map.Entry<String, ClassMetadata> entry : map.entrySet()) {
            System.out.println(String.format("[%s] - entity name = %s, class = %s", entry.getKey(), entry.getValue().getEntityName(), entry.getValue().getMappedClass().getName()));
        }
        Session session = sessionFactory.openSession();
        System.out.println(session.isOpen());
        session.beginTransaction();
        List result = session.createQuery("from com.edifecs.domain.hibernate.tm.maintenance.Dbproperties").list();
        System.out.println(result.size());
       /* for (Dbproperties dbproperty : (List<Dbproperties>) result ) {
            System.out.println("DbProperty (" + dbproperty.getPropertySid() + ") : " + dbproperty.getPropertyName() + " = " + dbproperty.getPropertyValue());
        }*/
        session.getTransaction().commit();
        session.close();
    }
}
