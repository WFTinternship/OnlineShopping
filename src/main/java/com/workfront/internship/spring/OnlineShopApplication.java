package com.workfront.internship.spring;

import com.workfront.internship.business.UserManager;
import com.workfront.internship.dao.UserDao;
import com.workfront.internship.dao.UserDaoImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContext;

/**
 * Created by Anna Asmangulyan on 8/18/2016.
 */
public class OnlineShopApplication {
    public static final String SPRING_CONTEXT_KEY = "springContextKey";

    public static void init(ServletContext servletContext) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.workfront");
        servletContext.setAttribute(SPRING_CONTEXT_KEY, applicationContext);
    }

    public static ApplicationContext getApplicationContext(ServletContext servletContext) {
        return (ApplicationContext)servletContext.getAttribute(SPRING_CONTEXT_KEY);
    }
}
