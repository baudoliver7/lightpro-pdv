package com.lightpro.pdv.cmd;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import com.common.utilities.convert.TimeConvert;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.securities.api.Sex;

public class CashierEdited {
	
	private final UUID id;
	private final String firstName;
	private final String lastName;
	private final Sex sex;
	private final String address;
	private final Date birthDate;
	private final String tel1;
	private final String tel2;
	private final String email;
	private final String photo;
	private final UUID teamId;
	
	public CashierEdited(){
		throw new UnsupportedOperationException("#CashierEdited()");
	}
	
	@JsonCreator
	public CashierEdited(  @JsonProperty("id") final UUID id,
						  @JsonProperty("firstName") final String firstName,
						  @JsonProperty("lastName") final String lastName, 
				    	  @JsonProperty("sex") final Sex sex,
				    	  @JsonProperty("address") final String address,
						  @JsonProperty("birthDate") final Date birthDate, 
				    	  @JsonProperty("tel1") final String tel1,
				    	  @JsonProperty("tel2") final String tel2,
						  @JsonProperty("email") final String email, 
				    	  @JsonProperty("photo") final String photo,
				    	  @JsonProperty("teamId") final UUID teamId){
		
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.sex = sex;
		this.address = address;
		this.birthDate = birthDate;
		this.tel1 = tel1;
		this.tel2 = tel2;
		this.email = email;
		this.photo = photo;
		this.teamId = teamId;
	}
	
	public UUID id(){
		return id;
	}
	
	public String firstName(){
		return firstName;
	}
	
	public String lastName(){
		return lastName;
	}
	
	public Sex sex(){
		return sex;
	}	
	
	public String address(){
		return address;
	}
	
	public LocalDate birthDate(){		
		return TimeConvert.toLocalDate(birthDate, ZoneId.systemDefault());
	}
	
	public String tel1(){
		return tel1;
	}
	
	public String tel2(){
		return tel2;
	}
	
	public String email(){
		return email;
	}
	
	public String photo(){
		return photo;
	}
	
	public UUID teamId(){
		return teamId;
	}
}
