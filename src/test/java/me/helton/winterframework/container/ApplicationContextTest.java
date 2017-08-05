package me.helton.winterframework.container;

import me.helton.winterframework.container.exceptions.BeanNotRegisteredException;
import me.helton.winterframework.container.exceptions.DuplicatedBeanException;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ApplicationContextTest {

    @Before
    public void setup() {
        ApplicationContext.initialize();
    }

    @Test
    public void shouldReturnRegisteredInstance() throws Exception {
        ApplicationContext.registerBean(SimpleInterface.class, new FirstImplementation());
        final SimpleInterface simpleInterface = ApplicationContext.getBean(SimpleInterface.class);
        assertThat(simpleInterface).isInstanceOf(FirstImplementation.class);
    }

    @Test(expected = BeanNotRegisteredException.class)
    public void shouldThrowAnExceptionIfBeanIsNotRegistered() throws Exception {
        ApplicationContext.getBean(SimpleInterface.class);
    }

    @Test(expected = DuplicatedBeanException.class)
    public void shouldNotAllowRegisterDuplicatedBeans() throws Exception {
        ApplicationContext.registerBean(SimpleInterface.class, new FirstImplementation());
        ApplicationContext.registerBean(SimpleInterface.class, new SecondImplementation());
    }

    @Test
    public void shouldReturnRegisteredInstanceViaResolver() throws Exception {
        ApplicationContext.registerBeanResolver(SimpleInterface.class, FirstImplementation::new);
        final SimpleInterface simpleInterface = ApplicationContext.getBean(SimpleInterface.class);
        assertThat(simpleInterface).isInstanceOf(FirstImplementation.class);
    }

    @Test
    public void shouldAllowToKnowIfABeanHasBeenRegistered() throws Exception {
        assertThat(ApplicationContext.isBeanRegistered(SimpleInterface.class)).isFalse();
        ApplicationContext.registerBean(SimpleInterface.class, new FirstImplementation());
        assertThat(ApplicationContext.isBeanRegistered(SimpleInterface.class)).isTrue();
    }

    @Test
    public void shouldAllowToCreatePrototypeBeans() throws Exception {
        final SimpleInterface instance = new FirstImplementation();
        ApplicationContext.registerBeanResolver(SimpleInterface.class, () -> instance);
        final SimpleInterface result = ApplicationContext.getBean(SimpleInterface.class);
        assertThat(result).isSameAs(instance);
    }

    @Test
    public void shouldAllowToCreateSingletonBeans() throws Exception {
        ApplicationContext.registerBeanResolver(SimpleInterface.class, SecondImplementation::new, Lifecycle.SINGLETON);
        final SimpleInterface first = ApplicationContext.getBean(SimpleInterface.class);
        final SimpleInterface second = ApplicationContext.getBean(SimpleInterface.class);
        assertThat(first).isNotNull();
        assertThat(second).isNotNull();
        assertThat(first).isSameAs(second);
    }

    @Test(expected = InstantiationError.class)
    public void shouldNotAllowCreateApplicationContext() throws Exception {
        new ApplicationContext();
    }

    interface SimpleInterface { }

    private class FirstImplementation implements SimpleInterface { }

    private class SecondImplementation implements SimpleInterface { }

}