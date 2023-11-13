package org.epam;

import org.epam.config.Storage;
import org.epam.model.User;
import org.epam.service.TrainingService;
import org.springframework.context.ConfigurableApplicationContext;

public class App
{
    public static void main( String[] args )
    {
        ConfigurableApplicationContext context = new org.springframework.context.annotation.AnnotationConfigApplicationContext(Storage.class);
        Storage storage = context.getBean(Storage.class);
        System.out.println(storage);
        storage.getUsers().put("Vasily.Vasiliev", new User(99, "Vasily", "Vasiliev", "Vasily.Vasiliev", "password98", true));
        System.out.println(storage);
        context.close();
    }
}
