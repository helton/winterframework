package me.helton.winterframework.container;

import java.util.function.Supplier;

public class BeanMetadata<T> {
    private T instance;
    private BeanResolver<T> beanResolver;

    public BeanMetadata(final T instance) {
        this.instance = instance;
    }

    public BeanMetadata(final Supplier<T> resolver, final Lifecycle lifecycle) {
        this.beanResolver = new BeanResolver<>(resolver, lifecycle);
    }

    public T getBean() {
        if (this.instance != null)
            return instance;
        return beanResolver.getBean();
    }

}
