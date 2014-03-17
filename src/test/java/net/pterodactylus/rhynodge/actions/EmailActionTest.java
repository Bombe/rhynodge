package net.pterodactylus.rhynodge.actions;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

import net.pterodactylus.rhynodge.output.DefaultOutput;
import net.pterodactylus.rhynodge.output.Output;

import org.junit.Test;

/**
 * Unit test for {@link EmailAction}.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class EmailActionTest {

	private final Transport transport = mock(Transport.class);
	private final String sender = "sender@sender.de";
	private final String recipient = "recipient@recipient.de";
	private final EmailAction emailAction = new EmailAction(transport, sender, recipient);
	private final Output output = new DefaultOutput("Action triggered!").addText("text/plain", "Plain Text.").addText("text/html", "<p>HTML Text.</p>");

	@Test
	public void canCreateActionWithNormalConstructor() {
		new EmailAction("hostname", "sender", "recipient");
	}

	@Test
	public void emailIsGeneratedCorrectly() throws MessagingException {
		emailAction.execute(output);
		verify(transport).sendMessage(any(Message.class), any(Address[].class));
	}

	@Test
	public void exceptionWhenSendingIsSwallowed() throws MessagingException {
		doThrow(MessagingException.class).doNothing().when(transport).sendMessage(any(Message.class), any(Address[].class));
		emailAction.execute(output);
		emailAction.execute(output);
		verify(transport, times(2)).sendMessage(any(Message.class), any(Address[].class));
	}

}
