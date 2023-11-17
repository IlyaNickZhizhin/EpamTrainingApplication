package org.epam;

import org.epam.config.Config;
import org.epam.model.User;
import org.hibernate.Session;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App
{
    public static void main(String[] args)
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Session session = context.getBean("getSessionFactory", org.hibernate.SessionFactory.class).openSession();
        User user = Supplier.user1;
        session.save(user);
        session.close();
        context.close();
    }
}
