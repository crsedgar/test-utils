package uk.org.cobaltdevelopment.test.db.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_ACCOUNT")
public class Account {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = "DATE_CREATED")
	private Date dateCreated;

}
