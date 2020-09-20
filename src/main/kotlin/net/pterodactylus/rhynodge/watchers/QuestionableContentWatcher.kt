/*
 * rhynodge - QuestionableContentWatcher.kt - Copyright © 2013–2020 David Roden
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.pterodactylus.rhynodge.watchers

import net.pterodactylus.rhynodge.filters.HtmlFilter
import net.pterodactylus.rhynodge.filters.comics.QuestionableContentComicFilter
import net.pterodactylus.rhynodge.queries.HttpQuery
import net.pterodactylus.rhynodge.triggers.NewComicTrigger

class QuestionableContentWatcher : DefaultWatcher(query, filters, trigger)

private val query = HttpQuery("https://www.questionablecontent.net/")
private val filters = listOf(HtmlFilter(), QuestionableContentComicFilter())
private val trigger = NewComicTrigger()
