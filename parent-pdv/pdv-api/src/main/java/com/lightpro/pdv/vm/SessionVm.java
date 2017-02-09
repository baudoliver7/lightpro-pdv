package com.lightpro.pdv.vm;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.pdv.domains.api.Session;

public class SessionVm {
	private final transient Session origin;
	
	public SessionVm(){
		throw new UnsupportedOperationException("#SessionVm()");
	}
	
	public SessionVm(final Session origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public UUID id(){
		return origin.id();
	}
	
	@JsonGetter
	public String reference() throws IOException {
		return origin.reference();
	}
	
	@JsonGetter
	public UUID pdvId() throws IOException {
		return origin.pdv().id();
	}
	
	@JsonGetter
	public String pdv() throws IOException {
		return origin.pdv().name();
	}
	
	@JsonGetter
	public UUID cashierId() throws IOException {
		return origin.cashier().id();
	}
	
	@JsonGetter
	public String cashier() throws IOException {
		return origin.cashier().fullName();
	}
}
