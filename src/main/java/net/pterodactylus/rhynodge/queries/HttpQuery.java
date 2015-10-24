/*
 * Rhynodge - HttpQuery.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.queries;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.pterodactylus.rhynodge.Query;
import net.pterodactylus.rhynodge.State;
import net.pterodactylus.rhynodge.states.FailedState;
import net.pterodactylus.rhynodge.states.HttpState;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

/**
 * {@link Query} that performs an HTTP GET request to a fixed uri.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class HttpQuery implements Query {

	private final String uri;
	private final String proxyHost;
	private final int proxyPort;

	public HttpQuery(String uri) {
		this(uri, null, -1);
	}

	public HttpQuery(String uri, String proxyHost, int proxyPort) {
		this.uri = uri;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
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
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
				.setSSLHostnameVerifier((hostname, session) -> true)
				.addInterceptorFirst(new ResponseContentEncoding());
		if ((proxyHost != null) && (proxyPort != -1)) {
			httpClientBuilder.setProxy(new HttpHost(proxyHost, proxyPort));
		}
		HttpClient httpClient = httpClientBuilder.build();
		HttpGet get = new HttpGet(uri);

		try {
			/* make request. */
			get.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.11 (KHTML, like Gecko) Ubuntu/12.04 Chromium/20.0.1132.47 Chrome/20.0.1132.47 Safari/536.11");
			HttpConnectionParams.setConnectionTimeout(get.getParams(), (int) TimeUnit.SECONDS.toMillis(300));
			HttpConnectionParams.setSoTimeout(get.getParams(), (int) TimeUnit.SECONDS.toMillis(300));
			HttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return new FailedState(new IllegalStateException(String.format("Invalid HTTP Status: %d", response.getStatusLine().getStatusCode())));
			}
			HttpEntity entity = response.getEntity();

			/* yay, done! */
			return new HttpState(uri, response.getStatusLine().getStatusCode(), entity.getContentType().getValue(), EntityUtils.toByteArray(entity));

		} catch (IOException ioe1) {
			return new FailedState(ioe1);
		}
	}

}
