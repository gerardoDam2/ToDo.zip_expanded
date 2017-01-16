package dad.todo.services.jpa.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class EmailUtil {
	
	private static final String HOST 		= "smtp.gmail.com";
	private static final int PORT 			= 587;
	private static final String USERNAME 	= "dad.iesdpm@gmail.com";
	private static final String PASSWORD 	= "Minikit0$";

	public static void sendEmail(String toAddress, String subject, String message) throws MessagingException {

		// establece las propiedades del servidor SMTP
		Properties properties = new Properties();
		properties.put("mail.smtp.host", HOST);
		properties.put("mail.smtp.port", PORT);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		// crea una nueva sesión usando un autenticador
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		};

		Session session = Session.getInstance(properties, auth);

		// crea el nuevo email
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(USERNAME));
		msg.setRecipients(Message.RecipientType.TO, new InternetAddress[] { new InternetAddress(toAddress) });
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		msg.setContent(message, "text/html");

		// envía el email
		Transport.send(msg);

	}

}
