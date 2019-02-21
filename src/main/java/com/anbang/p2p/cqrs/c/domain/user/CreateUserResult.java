package com.anbang.p2p.cqrs.c.domain.user;

public class CreateUserResult {
	private String userId;
	private String publisher;
	private String uuid;

	public CreateUserResult(String userId, String publisher, String uuid) {
		this.userId = userId;
		this.publisher = publisher;
		this.uuid = uuid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
