package com.google.code.jee.utils.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * The Class SpringBeanProvider.
 */
@Component("springBeanProvider")
public class SpringBeanProvider implements ApplicationContextAware {
    private static ApplicationContext CONTEXT = null;

    /**
     * {@inheritedDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        CONTEXT = ctx;
    }

    /**
     * Gets the bean.
     * 
     * @param beanName the bean name
     * @return the bean
     */
    public static Object getBean(String beanName) {
        return CONTEXT.getBean(beanName);
    }

    /**
     * Gets the bean.
     * 
     * @param <T> the generic type
     * @param clazz the clazz
     * @return the bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return CONTEXT.getBean(clazz);
    }

    /**
     * Gets the spring context.
     * 
     * @return the spring context
     */
    public static ApplicationContext getSpringContext() {
        return CONTEXT;
    }
}
