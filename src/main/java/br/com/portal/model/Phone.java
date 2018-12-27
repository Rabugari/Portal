package br.com.portal.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Domínio para telefone do usuário
 * @author douglas.takara
 */
@Entity
public class Phone implements Serializable {

	private static final long serialVersionUID = -6753234351933827560L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private Integer ddd;
	
	@Column
	private Long number;
	
	public Integer getDdd() {
		return ddd;
	}
	public void setDdd(Integer ddd) {
		this.ddd = ddd;
	}
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
}
