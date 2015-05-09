package net.pterodactylus.util.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link ObjectBinding}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ObjectBindingTest {

	@Test
	public void constructorCanBeCalled() {
		new ObjectBinding();
	}

	@Test
	public void objectIsBound() {
		TestClass testObject = new TestClass();
		Injector injector = Guice.createInjector(ObjectBinding.forClass(TestInterface.class).is(testObject));
		MatcherAssert.assertThat(injector.getInstance(TestInterface.class), Matchers.sameInstance(testObject));
	}

	public interface TestInterface { }

	public static class TestClass implements TestInterface { }

}
