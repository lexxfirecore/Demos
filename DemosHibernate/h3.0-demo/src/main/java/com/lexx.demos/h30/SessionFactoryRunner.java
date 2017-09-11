package com.edifecs.hibertest;

import com.edifecs.domain.hibernate.tm.maintenance.Dbproperties;
import org.hibernate.EntityMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.metadata.ClassMetadata;

import java.util.*;

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
        configuration.configure("/conf/orm-mapping/active.hibernate.cfg.xml");
        configuration.addProperties(dialectProperties);
        configuration.addProperties(connProps);
        configuration.setProperty("hibernate.default_entity_mode", "dynamic-map");
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public void run() {
        System.out.println("Running SessionFactory.");
        Map<String, ClassMetadata> map =  sessionFactory.getAllClassMetadata();
        for (Map.Entry<String, ClassMetadata> entry : map.entrySet()) {
            System.out.println(String.format("[%s] - entity name = %s, class = %s", entry.getKey(), entry.getValue().getEntityName(), entry.getValue().getMappedClass(EntityMode.POJO)));
        }
        Session session = sessionFactory.openSession();
        session.getSession(EntityMode.MAP);
        session.beginTransaction();
        List result = session.createQuery("from com.edifecs.domain.hibernate.tm.maintenance.Dbproperties").list();
        System.out.println(result.size());
        for (Dbproperties dbproperty : (List<Dbproperties>) result ) {
            System.out.println("DbProperty (" + dbproperty.getPropertySid() + ") : " + dbproperty.getPropertyName() + " = " + dbproperty.getPropertyValue());
        }
        //--------------------------------------
        HashMap<String, Object> insertMap = new HashMap<String, Object>();
        insertMap.put("propertySid", 9l);
        insertMap.put("propertyName", "Codeset-Version");
        insertMap.put("propertyValue", "9.9.9");
        insertMap.put("propertyDescription", "Current version of the installed Codesets");
        insertMap.put("lastModifiedDateTime", new Date());
        session.save("DBProperties", insertMap);
        //--------------------------------------

        session.getTransaction().commit();
        session.close();
    }
}
