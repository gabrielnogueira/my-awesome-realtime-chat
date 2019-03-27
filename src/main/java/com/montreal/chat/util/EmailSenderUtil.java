package com.montreal.chat.util;

import static com.sendgrid.Method.POST;

import java.io.IOException;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

public class EmailSenderUtil {
	private static final String SEND_GRID_MAIL_SEND_ENDPOINT = "mail/send";

	public static void sendEmail(String emailFrom, String emailTo, String subject, String contentType,
			String contentMessage, String apiKey) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				Email from = new Email(emailFrom);
				Email to = new Email(emailTo);
				Content emailContent = new Content(contentType, contentMessage);
				Mail mail = new Mail(from, subject, to, emailContent);

				SendGrid sg = new SendGrid(apiKey);
				Request request = new Request();
				try {
					request.setMethod(POST);
					request.setEndpoint(SEND_GRID_MAIL_SEND_ENDPOINT);
					request.setBody(mail.build());
					sg.api(request);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}
}
