package br.com.portal.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Domínio para telefone do usuário
 * @author douglas.takara
 */
@Entity
@Table(name="PHONE")
@ApiModel(value = "Telefone")
public class Phone implements Serializable {

	private static final long serialVersionUID = -6753234351933827560L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="phone_seq")
	@SequenceGenerator(name="phone_seq", sequenceName="phone_seq", allocationSize=1	)
	@ApiModelProperty(allowEmptyValue=true, required=false)
	private Long id;
	
	@Column(name="DDD", length=2)
	@ApiModelProperty(dataType="String", example="\"11\"")
	private Integer ddd;
	
	@Column(name="NUMBER", length=20)
	@ApiModelProperty(dataType="String", example="\"1112345678\"")
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