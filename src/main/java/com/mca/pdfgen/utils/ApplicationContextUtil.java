package com.mca.pdfgen.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware 
{
    private static ApplicationContext context;

    public static ApplicationContext getContext() 
    {
        return context;
    }

    public static <T> T getBean(final Class<T> bean) 
    {
        return getContext().getBean(bean);
    }

    public static <T> T getBean(final String beanName, final Class<T> bean) 
    {
        return getContext().getBean(beanName, bean);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) 
    		                          throws BeansException 
    {
        context = applicationContext;
    }
}
