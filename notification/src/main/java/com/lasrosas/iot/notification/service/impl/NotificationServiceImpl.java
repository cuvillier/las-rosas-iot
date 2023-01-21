package com.lasrosas.iot.notification.service.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lasrosas.iot.notification.service.api.Notification;
import com.lasrosas.iot.notification.service.api.NotificationService;

public class NotificationServiceImpl implements NotificationService {
	public static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

	@Override
	public void send(Notification notification) {
	
		try {
			var requestBuilder = HttpRequest.newBuilder()
					  .uri(new URI("https://ntfy.sh/d9a36cf0-9906-11ed-a8fc-0242ac120002"))
					  .header("priority", notification.getPriority().toString().toLowerCase())
					  .POST(HttpRequest.BodyPublishers.ofString(notification.getContent()));

			if( notification.getTitle() != null) {
				requestBuilder.header("title", notification.getTitle());

				if( notification.getTags() != null) {
					requestBuilder.header("tags", String.join(",", notification.getTitle()));
				}
			}

			var request = requestBuilder.build();
			var client = HttpClient.newHttpClient();
			
			var response = client.send(request, BodyHandlers.ofString());
			if( response.statusCode() != 200 ) {
				log.error("Http POST return statusCode " + response.statusCode());
			}

		} catch (Exception e) {
			throw new RuntimeException("Cannot notify", e);
		}
	}

}
