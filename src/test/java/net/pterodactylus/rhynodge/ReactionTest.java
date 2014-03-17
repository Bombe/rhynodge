package net.pterodactylus.rhynodge;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import org.junit.Test;

/**
 * Unit test for {@link Reaction}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ReactionTest {

	private final Query query = mock(Query.class);
	private final Filter filter = mock(Filter.class);
	private final Trigger trigger = mock(Trigger.class);
	private final Action action = mock(Action.class);
	private final Reaction reactionWithoutFilters = new Reaction("without", query, trigger, action);
	private final Reaction reactionWithFilters = new Reaction("with", query, asList(filter), trigger, action);

	@Test
	public void reactionStoresNameCorrectly() {
		assertThat(reactionWithoutFilters.name(), is("without"));
		assertThat(reactionWithFilters.name(), is("with"));
	}

	@Test
	public void reactionStoresQueryCorrectly() {
		assertThat(reactionWithoutFilters.query(), is(query));
		assertThat(reactionWithFilters.query(), is(query));
	}

	@Test
	public void reactionStoresFiltersCorrectly() {
		assertThat(reactionWithoutFilters.filters(), emptyIterable());
		assertThat(reactionWithFilters.filters(), contains(filter));
	}

	@Test
	public void reactionStoresTriggerCorrectly() {
		assertThat(reactionWithoutFilters.trigger(), is(trigger));
		assertThat(reactionWithFilters.trigger(), is(trigger));
	}

	@Test
	public void reactionStoresActionCorrectly() {
		assertThat(reactionWithoutFilters.action(), is(action));
		assertThat(reactionWithFilters.action(), is(action));
	}

	@Test
	public void reactionStoresUpdateIntervalCorrectly() {
		reactionWithoutFilters.setUpdateInterval(1);
		assertThat(reactionWithoutFilters.updateInterval(), is(1L));
		reactionWithFilters.setUpdateInterval(2);
		assertThat(reactionWithFilters.updateInterval(), is(2L));
	}

}
