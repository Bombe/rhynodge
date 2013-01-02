/**
 * Reactor main definitions.
 * <p>
 * A Reactor chain consists of three different elements: a
 * {@link net.pterodactylus.reactor.Query}, a
 * {@link net.pterodactylus.reactor.Trigger}, and an
 * {@link net.pterodactylus.reactor.Action}.
 * <p>
 * A {@code Query} retrieves the current state of a system; this can simply be
 * the current state of a local file, or it can be the last tweet of a certain
 * Twitter account, or it can be anything inbetween, or something completely
 * different.
 * <p>
 * After a {@code Query} retrieved the current
 * {@link net.pterodactylus.reactor.State} of a system, this state and the
 * previously retrieved state are handed in to a {@code Trigger}. The trigger
 * then decides whether the state of the system can be considered a change.
 * <p>
 * If a system has been found to trigger, an {@code Action} is executed. It
 * performs arbitrary actions and can use both the current state and the
 * previous state to define that action.
 */

package net.pterodactylus.reactor;

