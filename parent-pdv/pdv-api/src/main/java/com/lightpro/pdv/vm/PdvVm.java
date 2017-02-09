package com.lightpro.pdv.vm;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.pdv.domains.api.Pdv;

public class PdvVm {
	private final transient Pdv origin;
	
	public PdvVm(){
		throw new UnsupportedOperationException("#PdvVm()");
	}
	
	public PdvVm(final Pdv origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public UUID id(){
		return origin.id();
	}
	
	@JsonGetter
	public String name() throws IOException {
		return origin.name();
	}
	
	@JsonGetter
	public boolean active() throws IOException {
		return origin.active();
	}
	
	@JsonGetter
	public int numberOfProductToSales() throws IOException {
		return origin.productsToSale().totalCount("");
	}
}
