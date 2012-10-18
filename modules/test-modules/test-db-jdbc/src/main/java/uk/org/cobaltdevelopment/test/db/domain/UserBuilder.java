package uk.org.cobaltdevelopment.test.db.domain;

import java.util.Date;

public class UserBuilder {
	private User content;

	public static UserBuilder create() {
		UserBuilder userBuilder = new UserBuilder();
		userBuilder.content = new User();
		return userBuilder;
	}

	public UserBuilder user(String username) {
		content.setUserName(username);
		return this;
	}

	public UserBuilder dateRegistered(Date dateRegistered) {
		content.setDateRegistered(dateRegistered);
		return this;
	}

	public UserBuilder role(String name) {
		Role role = new Role();
		role.setName(name);
		content.getRoles().add(role);
		return this;
	}

	public User build() {
		return content;
	}
}