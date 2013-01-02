/*
 * Reactor - EmailAction.java - Copyright © 2013 David Roden
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

package net.pterodactylus.reactor.actions;

import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.pterodactylus.reactor.Action;

/**
 * {@link Action} implementation that sends an email containing the triggering
 * object to an email address.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class EmailAction implements Action {

	/** The name of the SMTP host. */
	private final String hostname;

	/** The email address of the sender. */
	private final String sender;

	/** The email address of the recipient. */
	private final String recipient;

	/**
	 * Creates a new email action.
	 *
	 * @param hostname
	 *            The hostname of the SMTP server
	 * @param sender
	 *            The email address of the sender
	 * @param recipient
	 *            The email address of the recipient
	 */
	public EmailAction(String hostname, String sender, String recipient) {
		this.hostname = hostname;
		this.sender = sender;
		this.recipient = recipient;
	}

	//
	// ACTION METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Object trigger) {
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", hostname);
		Session session = Session.getInstance(properties);
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(sender));
			message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
			message.setSubject("Reaction Triggered!");
			message.setText(String.valueOf(trigger));
			Transport.send(message);
		} catch (MessagingException me1) {
			/* swallow. */
		}
	}

}
