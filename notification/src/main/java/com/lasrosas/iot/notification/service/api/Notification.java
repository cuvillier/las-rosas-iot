package com.lasrosas.iot.notification.service.api;

public class Notification {
	private final NotificationPriority priority;
	private final String content;
	private final String title;
	private final String tags[];

	public static Notification create(NotificationPriority priority, String content) {
		return new Notification(priority, content, null, null);
	}

	public static Notification create(NotificationPriority priority, String content, String title) {
		return new Notification(priority, content, title, null);
	}

	public static Notification create(NotificationPriority priority, String content, String title, String[] tags) {
		return new Notification(priority, content, title, tags);
	}

	public Notification(NotificationPriority priority, String content, String title, String[] tags) {
		super();
		this.title = title;
		this.tags = tags;
		this.content = content;
		this.priority = priority;
	}
	public NotificationPriority getPriority() {
		return priority;
	}

	public String getTitle() {
		return title;
	}
	public String[] getTags() {
		return tags;
	}
	public String getContent() {
		return content;
	}

	
}
