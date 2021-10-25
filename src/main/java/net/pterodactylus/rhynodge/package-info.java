/**
 * Rhynodge main definitions.
 * <p>
 * A {@link net.pterodactylus.rhynodge.Reaction} consists of three different
 * elements: a {@link net.pterodactylus.rhynodge.Query}, a
 * {@link net.pterodactylus.rhynodge.Merger}, and an
 * {@link net.pterodactylus.rhynodge.Action}.
 * <p>
 * A {@code Query} retrieves the current state of a system; this can simply be
 * the current state of a local file, or it can be the last tweet of a certain
 * Twitter account, or it can be anything inbetween, or something completely
 * different.
 * <p>
 * After a {@code Query} retrieved the current
 * {@link net.pterodactylus.rhynodge.State} of a system, this state and the
 * previously retrieved state are handed in to a {@code Merger}. The merger
 * takes care of merging the two states in a way that the new state can
 * {@link net.pterodactylus.rhynodge.State#triggered() decide} whether a
 * noteworthy change has occured.
 * <p>
 * If a system has been found to trigger, an {@code Action} is executed. It
 * performs arbitrary actions and can use both the current state and the
 * previous state to define that action.
 */

package net.pterodactylus.rhynodge;

