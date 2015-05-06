/*
 * Rhynodge - EmailAction.java - Copyright © 2013 David Roden
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

package net.pterodactylus.rhynodge.actions;

import static java.lang.System.getProperties;
import static javax.mail.Session.getInstance;

import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.pterodactylus.rhynodge.Action;
import net.pterodactylus.rhynodge.output.Output;

import com.google.common.annotations.VisibleForTesting;
import com.sun.mail.smtp.SMTPTransport;
import org.apache.log4j.Logger;

/**
 * {@link Action} implementation that sends an email containing the triggering
 * object to an email address.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class EmailAction implements Action {

	private static final Logger logger = Logger.getLogger(EmailAction.class);

	/** The email address of the sender. */
	private final String sender;

	/** The email address of the recipient. */
	private final String recipient;

	private final Transport transport;
	private final Session session;

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
		this.sender = sender;
		this.recipient = recipient;
		Properties properties = getProperties();
		properties.put("mail.smtp.host", hostname);
		session = getInstance(properties);
		transport = new SMTPTransport(session, new URLName("smtp", hostname, 25, null, "", ""));
	}

	@VisibleForTesting
	EmailAction(Transport transport, String sender, String recipient) {
		this.transport = transport;
		this.sender = sender;
		this.recipient = recipient;
		this.session = getInstance(getProperties());
	}

	//
	// ACTION METHODS
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Output output) {
		MimeMessage message = new MimeMessage(session);
		try {
			/* create message. */
			message.setFrom(new InternetAddress(sender));
			message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
			message.setSubject(output.summary(), "UTF-8");

			/* create text and html parts. */
			MimeMultipart multipart = new MimeMultipart();
			multipart.setSubType("alternative");
			addPlainTextPart(output, multipart);
			addHtmlPart(output, multipart);
			message.setContent(multipart);

			if (!transport.isConnected()) {
				transport.connect();
			}
			transport.sendMessage(message, message.getAllRecipients());
		} catch (MessagingException me1) {
			logger.error("Could not send email!", me1);
		}
	}

	private void addPlainTextPart(Output output, MimeMultipart multipart) throws MessagingException {
		if (output.text("text/plain", -1) == null) {
			return;
		}
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent(output.text("text/plain", -1), "text/plain;charset=utf-8");
		multipart.addBodyPart(textPart);
	}

	private void addHtmlPart(Output output, MimeMultipart multipart) throws MessagingException {
		if (output.text("text/html", -1) == null) {
			return;
		}
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(output.text("text/html", -1), "text/html;charset=utf-8");
		multipart.addBodyPart(htmlPart);
	}

}
