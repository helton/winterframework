package me.helton.winterframework.container;

import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class BeanResolver<T> {
    private final Supplier<T> resolver;
    private final Lifecycle lifecycle;
    private static Object instance;

    public T getBean() {
        if (lifecycle == Lifecycle.PROTOTYPE)
            return resolver.get();
        else {
            if (instance == null)
                instance = resolver.get();
            return (T) instance;
        }
    }
}
