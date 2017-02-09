package com.lightpro.pdv.cmd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PdvEdited {
	private final String name;
	
	public PdvEdited(){
		throw new UnsupportedOperationException("#PdvEdited()");
	}
	
	@JsonCreator
	public PdvEdited(@JsonProperty("name") final String name){
		
		this.name = name;
	}
	
	public String name(){
		return name;
	}
}
