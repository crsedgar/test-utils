package uk.org.cobaltdevelopment.test.db.dao;

import java.util.Set;

import uk.org.cobaltdevelopment.test.db.domain.Role;
import uk.org.cobaltdevelopment.test.db.domain.User;

public interface UserDAO {

	/**
	 * Add new <code>User</code> returning unique identifier.
	 * 
	 */
	public abstract long add(User user);

	/**
	 * Find a user using unique identifier.
	 * 
	 */
	public abstract User find(long id);

	/**
	 * Return all roles assigned to user.
	 * 
	 * @return all roles, if user has none then empty collection returned.
	 */
	public Set<Role> roles(long id);

}