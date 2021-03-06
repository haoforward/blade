package com.blade.ioc.bean;

import com.blade.ioc.Injector;
import com.blade.ioc.Ioc;
import com.blade.kit.IocKit;

import java.lang.reflect.Field;

/**
 * Bean Field Injector
 *
 * @author <a href="mailto:biezhi.me@gmail.com" target="_blank">biezhi</a>
 * @since 1.5
 */
public class FieldInjector implements Injector {

    private Ioc   ioc;
    private Field field;

    public FieldInjector(Ioc ioc, Field field) {
        this.ioc = ioc;
        this.field = field;
    }

    public Class<?> getType() {
        return field.getType();
    }

    public boolean isSingleton() {
        return IocKit.isSingleton(field.getType());
    }

    @Override
    public void injection(Object bean) {
        try {
            Class<?> fieldType = field.getType();
            Object   value     = ioc.getBean(fieldType);
            if (value == null) {
                throw new IllegalStateException("Can't inject bean: " + fieldType.getName() + " for field: " + field);
            }
            injection(bean, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void injection(Object bean, Object value) {
        try {
            field.setAccessible(true);
            field.set(bean, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasInjectFields() {
        return field.getType().getDeclaredFields().length > 0;
    }

}