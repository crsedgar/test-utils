package uk.org.cobaltdevelopment.test.db.domain;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class User {

	private long id;

	private String userName;

	private Set<Role> roles = new LinkedHashSet<Role>();

	private Date dateRegistered;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}

	public Role getRoleByName(String name) {
		for (Role r : roles) {
			if (r.getName().equalsIgnoreCase(name)) {
				return r;
			}
		}
		return null;
	}
}
