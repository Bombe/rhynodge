package net.pterodactylus.rhynodge.states;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Objects.equal;
import static org.apache.log4j.Level.OFF;
import static org.apache.log4j.Logger.getLogger;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.StateManager.StateDirectory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.io.Files;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Unit test for {@link StateManager}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class StateManagerTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	private final File statePath;
	private final StateManager stateManager;

	@BeforeClass
	public static void deactivateLogging() {
		getLogger(StateManager.class).setLevel(OFF);
	}

	public StateManagerTest() throws IOException {
		tempFolder.create();
		statePath = tempFolder.newFolder();
		stateManager = new StateManager(StateDirectory.of(statePath.getPath()));
	}

	@Test
	public void successStateCanBeRestored() {
		TestState testState = new TestState();
		stateManager.saveState("test", testState);
		Optional<State> restoredState = stateManager.loadLastState("test");
		assertThat(restoredState.get(), Matchers.<State>is(testState));
		restoredState = stateManager.loadLastSuccessfulState("test");
		assertThat(restoredState.get(), Matchers.<State>is(testState));
	}

	@Test
	public void failStateIsNotSavedAsSuccessfulState() {
		TestState testState = new TestState(false);
		stateManager.saveState("test", testState);
		Optional<State> restoredState = stateManager.loadLastState("test");
		assertThat(restoredState.get(), Matchers.<State>is(testState));
		restoredState = stateManager.loadLastSuccessfulState("test");
		assertThat(restoredState.isPresent(), is(false));
	}

	@Test
	public void invalidJsonFileCanNotBeLoaded() throws IOException {
		Files.write("not json", new File(statePath, "test.last.json"), UTF_8);
		Optional<State> restoredState = stateManager.loadLastState("test");
		assertThat(restoredState.isPresent(), is(false));
	}

	@Test
	public void jsonWithInvalidFieldsCanNotBeLoaded() throws IOException {
		Files.write("{\"not\":\"json\"}", new File(statePath, "test.last.json"), UTF_8);
		Optional<State> restoredState = stateManager.loadLastState("test");
		assertThat(restoredState.isPresent(), is(false));
	}

	@Test
	public void unmappableStateCanBeSavedButNotLoaded() throws IOException {
		InvalidState invalidState = new InvalidState();
		stateManager.saveState("test", invalidState);
		assertThat(new File(statePath, "test.last.json").exists(), is(false));
	}

	public static class TestState extends AbstractState {

		public TestState() {
			this(true);
		}

		public TestState(boolean success) {
			super(success);
		}

		@Override
		public boolean equals(Object object) {
			if (!(object instanceof TestState)) {
				return false;
			}
			TestState testState = (TestState) object;
			return equal(exception(), testState.exception())
					&& (failCount() == testState.failCount())
					&& (time() == testState.time());
		}

	}

	public static class InvalidState extends AbstractState {

		@JsonProperty
		private final Object someObject = new Object();

	}

}
