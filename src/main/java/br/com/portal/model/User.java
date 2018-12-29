package br.com.portal.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.portal.util.MessageUtil.MessageConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Dominio para o usuário da aplicação
 * @author douglas.takara
 */
@Entity
@Table(name = "USER")
@ApiModel(value = "Usuario")
public class User implements Serializable, UserDetails {

	private static final long serialVersionUID = -9221635979870150799L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	@SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
	@ApiModelProperty(allowEmptyValue=true, required=false)
	private Long id;

	@NotNull(message=MessageConstants.INSERT_AN_USERNAME)
	@Column(name = "NAME", length = 50)
	private String name;

	@NotNull(message=MessageConstants.INSERT_AN_EMAIL)
	@Column(name = "EMAIL", length = 120)
	private String email;
	
	@NotNull(message=MessageConstants.INSERT_A_PASSWORD)
	@Column(name = "PASSWORD", length = 120)
	private String password;

	@OneToMany(cascade = CascadeType.ALL)
	@Nullable
	@ApiModelProperty(allowEmptyValue=true, required=false)
	private List<Phone> phones;

	@Column(name = "CREATED")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	@ApiModelProperty(allowEmptyValue=true, required=false)
	private LocalDateTime created;

	@Column(name = "MODIFIED")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	@ApiModelProperty(allowEmptyValue=true, required=false)
	private LocalDateTime modified;

	@Column(name = "LASTLOGIN")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	@JsonProperty(value="last_login")
	@ApiModelProperty(allowEmptyValue=true, required=false)
	private LocalDateTime lastLogin;

	@Column(name = "TOKEN")
	@ApiModelProperty(allowEmptyValue=true, required=false)
	private String token;

	@Transient
    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		return getEmail();
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
}
