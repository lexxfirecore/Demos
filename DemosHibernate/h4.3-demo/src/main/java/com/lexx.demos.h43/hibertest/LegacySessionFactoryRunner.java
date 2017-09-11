package com.edifecs.hibertest;

//import com.edifecs.domain.hibernate.tm.maintenance.Dbproperties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.metamodel.Metadata;
import org.hibernate.metamodel.MetadataSources;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by c-ionnmoro on 16-Nov-16.
 */
public class LegacySessionFactoryRunner extends GenericRunner {
    SessionFactory sessionFactory;

    @Override
    public void configure() throws Exception {
        System.out.println("Configuring LegacySessionFactoryRunner.");
        Properties dialectProperties = new Properties();
        dialectProperties.load(getClass().getResourceAsStream("/hibernate.dialect.properties"));
        Properties connProps = new Properties();
        connProps.load(getClass().getResourceAsStream("/hibernate.connection.properties"));
        Configuration configuration = new Configuration();
        configuration.configure("/conf/orm-mapping/active.hibernate.cfg.xml");
        configuration.addProperties(dialectProperties);
        configuration.addProperties(connProps);
        configuration.setProperty("hibernate.default_entity_mode", "dynamic-map");
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(builder.build());
    }

    @Override
    public void run() {
        if (sessionFactory == null)
            return;
        System.out.println("-----------------------------------");
        System.out.println("Running LegacySessionFactoryRunner.");
        Session session = sessionFactory.openSession();
        System.out.println(session.isOpen());
        session.beginTransaction();
        List result = session.createQuery("from com.edifecs.domain.hibernate.tm.maintenance.Dbproperties").list();
        System.out.println(result.size());
      /*  for (Dbproperties dbproperty : (List<Dbproperties>) result ) {
            System.out.println("DbProperty (" + dbproperty.getPropertySid() + ") : " + dbproperty.getPropertyName() + " = " + dbproperty.getPropertyValue());
        }*/

        HashMap<String, Object> insertMap = new HashMap<String, Object>();
        insertMap.put("propertySid", 11L);
        insertMap.put("PropertyName", "Codeset-Version");
        insertMap.put("PropertyValue", "9.9.9");
        insertMap.put("PropertyDescription", "Current version of the installed Codesets");
        insertMap.put("LastModifiedDateTime", new Date());
        session.save("DBProperties", insertMap);
        session.getTransaction().commit();
        session.close();
    }
}
