package me.helton.winterframework.container;

import lombok.SneakyThrows;
import me.helton.winterframework.container.exceptions.BeanNotRegisteredException;
import me.helton.winterframework.container.exceptions.DuplicatedBeanException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ApplicationContext {

    private static final Map<Class<?>, BeanMetadata<?>> implementations = new HashMap<>();

    public ApplicationContext() {
        throw new InstantiationError();
    }

    public static <T> boolean isBeanRegistered(final Class<T> clazz) {
        return implementations.containsKey(clazz);
    }

    @SneakyThrows
    public static <T> void registerBean(final Class<T> clazz, final T instance) {
        checkIfBeanHasBeenRegistered(clazz);
        implementations.put(clazz, new BeanMetadata<>(instance));
    }

    @SneakyThrows
    public static <T> void registerBeanResolver(final Class<T> clazz, final Supplier<T> resolver, final Lifecycle lifecycle) {
        checkIfBeanHasBeenRegistered(clazz);
        implementations.put(clazz, new BeanMetadata<>(resolver, lifecycle));
    }

    public static <T> void registerBeanResolver(final Class<T> clazz, final Supplier<T> resolver) {
        registerBeanResolver(clazz, resolver, Lifecycle.PROTOTYPE);
    }

    @SneakyThrows
    public static <T> T getBean(final Class<T> clazz) {
        BeanMetadata<?> beanMetadata = implementations.get(clazz);
        if (beanMetadata == null)
            throw new BeanNotRegisteredException();
        return (T) beanMetadata.getBean();
    }

    public static void initialize() {
        implementations.clear();
    }

    private static <T> void checkIfBeanHasBeenRegistered(Class<T> clazz) throws DuplicatedBeanException {
        if (isBeanRegistered(clazz))
            throw new DuplicatedBeanException();
    }

}
