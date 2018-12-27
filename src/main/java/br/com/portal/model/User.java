package br.com.portal.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Dominio para o usuário da aplicação
 * @author douglas.takara
 */
@Entity
public class User implements Serializable {

	private static final long serialVersionUID = -9221635979870150799L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private String email;
	
	@Column
	private String password;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Phone> phones;
	
	@Column
	@JsonFormat(pattern="dd/MM/yyyy HH:mm")
	private LocalDateTime created;
	
	@Column
	@JsonFormat(pattern="dd/MM/yyyy HH:mm")
	private LocalDateTime modified;
	
	@Column
	@JsonFormat(pattern="dd/MM/yyyy HH:mm")
	private LocalDateTime lastLogin;
	
	@Column
	private String token;
	
	public User(String name, String email, String password, List<Phone> phones) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.phones = phones;
	}
}
