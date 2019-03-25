package com.montreal.chat.util;

import java.io.IOException;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

public class EmailSenderUtil {
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
					request.setMethod(Method.POST);
					request.setEndpoint("mail/send");
					request.setBody(mail.build());
					Response response = sg.api(request);
					System.out.println(response.getStatusCode());
					System.out.println(response.getBody());
					System.out.println(response.getHeaders());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}
}
