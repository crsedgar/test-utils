package uk.org.cobaltdevelopment.test.db.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_USER")
public class User {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = "USER_NAME")
	private String userName;

	@OneToMany(fetch = EAGER, cascade = ALL)
	@JoinColumn(name = "USER_ID")
	private Set<Role> roles = new LinkedHashSet<Role>();

	@OneToOne(fetch = EAGER, cascade = ALL)
	@JoinColumn(name = "ACCOUNT_ID")
	private Account accout;

	@Column(name = "DATE_REGISTERED")
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

	public Integer getId() {
		return id;
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
