package net.pterodactylus.rhynodge.queries

import net.pterodactylus.rhynodge.Query
import net.pterodactylus.rhynodge.State
import net.pterodactylus.rhynodge.states.AbstractState
import net.pterodactylus.rhynodge.states.FailedState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.sameInstance
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

/**
 * Unit test for [FallbackQuery].
 *
 * @author [David ‘Bombe’ Roden](mailto:bombe@pterodactylus.net)
 */
class FallbackQueryTest {

	@JvmField @Rule val expectedException = ExpectedException.none()!!

	private val firstQuery = mock(Query::class.java)
	private val secondQuery = mock(Query::class.java)
	private val thirdQuery = mock(Query::class.java)
	private val query = FallbackQuery(firstQuery, secondQuery, thirdQuery)

	private fun setupQueries(firstState: State = FailedState(), secondState: State = FailedState(), thirdState: State = FailedState()) {
		`when`(firstQuery.state()).thenReturn(firstState)
		`when`(secondQuery.state()).thenReturn(secondState)
		`when`(thirdQuery.state()).thenReturn(thirdState)
	}

	@Test
	fun `fallback query returns state of third query`() {
		val successState: AbstractState = object : AbstractState() {}
		setupQueries(thirdState = successState)
		assertThat(query.state(), sameInstance<State>(successState))
	}

	@Test
	fun `fallback query calls all three queries`() {
		val successState: AbstractState = object : AbstractState() {}
		setupQueries(thirdState = successState)
		query.state()
		verify(firstQuery).state()
		verify(secondQuery).state()
		verify(thirdQuery).state()
	}

	@Test
	fun `fallback query returns second state`() {
		val successState: AbstractState = object : AbstractState() {}
		setupQueries(secondState = successState)
		assertThat(query.state(), sameInstance<State>(successState))
	}

	@Test
	fun `fallback query does not query third query`() {
		val successState: AbstractState = object : AbstractState() {}
		setupQueries(secondState = successState)
		query.state()
		verify(firstQuery).state()
		verify(secondQuery).state()
		verify(thirdQuery, never()).state()
	}

	@Test
	fun `fallback query returns first state`() {
		val successState: AbstractState = object : AbstractState() {}
		setupQueries(firstState = successState)
		assertThat(query.state(), sameInstance<State>(successState))
	}

	@Test
	fun `fallback query does not query second and third query`() {
		val successState: AbstractState = object : AbstractState() {}
		setupQueries(firstState = successState)
		query.state()
		verify(firstQuery).state()
		verify(secondQuery, never()).state()
		verify(thirdQuery, never()).state()
	}

	@Test
	fun `fallback query returns failed state if no query succeeds`() {
		setupQueries()
		assertThat(query.state().success(), `is`(false))
	}

	@Test
	fun `fallback query can not be created without queries`() {
		expectedException.expect(IllegalArgumentException::class.java)
		FallbackQuery()
	}

}
