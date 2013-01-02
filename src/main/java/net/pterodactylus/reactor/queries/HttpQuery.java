/*
 * Reactor - HttpQuery.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.queries;

import java.io.IOException;
import java.io.InputStreamReader;

import net.pterodactylus.reactor.Query;
import net.pterodactylus.reactor.State;
import net.pterodactylus.reactor.states.FailedState;
import net.pterodactylus.reactor.states.HttpState;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.common.io.Closeables;

/**
 * {@link Query} that performs an HTTP GET request to a fixed uri.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class HttpQuery implements Query {

	/** The uri to request. */
	private final String uri;

	/**
	 * Creates a new HTTP query.
	 *
	 * @param uri
	 *            The uri to request
	 */
	public HttpQuery(String uri) {
		this.uri = uri;
	}

	//
	// QUERY METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("deprecation")
	public State state() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.addResponseInterceptor(new ResponseContentEncoding());
		HttpGet get = new HttpGet(uri);

		InputStreamReader inputStreamReader = null;
		try {
			/* make request. */
			get.addHeader("Accept-Encoding", "identity");
			HttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return new FailedState();
			}
			HttpEntity entity = response.getEntity();

			/* yay, done! */
			return new HttpState(uri, response.getStatusLine().getStatusCode(), entity.getContentType().getValue(), EntityUtils.toByteArray(entity));

		} catch (IOException ioe1) {
			return new FailedState(ioe1);
		} finally {
			Closeables.closeQuietly(inputStreamReader);
		}
	}

}
