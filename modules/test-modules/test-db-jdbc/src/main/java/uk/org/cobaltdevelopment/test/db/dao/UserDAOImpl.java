package uk.org.cobaltdevelopment.test.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.org.cobaltdevelopment.test.db.domain.Role;
import uk.org.cobaltdevelopment.test.db.domain.User;

@Repository
@Transactional
public class UserDAOImpl implements UserDAO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		LOGGER.debug("setDataSource - ENTER");
		jdbcTemplate = new JdbcTemplate(dataSource);
		simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(
				"t_user").usingGeneratedKeyColumns("id");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.org.cobaltdevelopment.test.db.dao.UserDAO#add(uk.org.cobaltdevelopment
	 * .test.db.domain.User)
	 */
	@Override
	public long add(User user) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("user_name", user.getUserName());
		params.put("date_registered", user.getDateRegistered());
		Number key = simpleJdbcInsert.executeAndReturnKey(params);
		user.setId(key.longValue());
		return key.longValue();

	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.org.cobaltdevelopment.test.db.dao.UserDAO#find(long)
	 */
	@Override
	public User find(long id) {
		User user = jdbcTemplate
				.queryForObject(
						"SELECT id, user_name, date_registered FROM t_user WHERE id = ?",
						new RowMapper<User>() {

							@Override
							public User mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								User user = new User();
								user.setId(rs.getLong(1));
								user.setUserName(rs.getString(2));
								user.setDateRegistered(rs.getDate(3));
								return user;
							}
						}, id);

		return user;
	}

	/*
	 * (non-Javadoc)
	 * @see uk.org.cobaltdevelopment.test.db.dao.UserDAO#roles(long)
	 */
	@Override
	public Set<Role> roles(long id) {
		List<Role> result = jdbcTemplate
				.query("SELECT id, name FROM t_role r INNER JOIN t_user_role ur ON (r.id = ur.role_id) WHERE user_id = ?",
						new RowMapper<Role>() {

							@Override
							public Role mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								Role role = new Role();
								role.setId(rs.getInt("id"));
								role.setName(rs.getString("name"));
								return role;
							}
						}, id);

		Set<Role> roles = new HashSet<Role>(result.size());
		roles.addAll(result);

		return roles;
	};

}
