package dit.hua.dsexternalproject.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUTHORITIES")
public class Authorities {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)    //for autoincrement 
	@Column(name = "ID")
	private int id;
	
    //annotation for foreign key  in users.java
	@Column(name = "USERNAME")
	private String username;
	
	@Column(name = "AUTHORITY")
	private String authority;

	
	public Authorities() {  //empty default constructor
	
	}
	
	
	public Authorities(int id, String authority, String username) {  //constructor
		super();
		this.id = id;
		this.authority = authority;
		this.username = username;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

	
	
	
	