package com.lightpro.pdv.vm;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sales.domains.api.Product;
import com.securities.api.Tax;

public class ProductVm {
	
	private final transient Product origin;
	
	public ProductVm(){
		throw new UnsupportedOperationException("#ProductVm()");
	}
	
	public ProductVm(final Product origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public UUID getId(){
		return origin.id();
	}
	
	@JsonGetter
	public String getName() throws IOException {
		return origin.name();
	}
	
	@JsonGetter
	public String getBarCode() throws IOException {
		return origin.barCode();
	}
	
	@JsonGetter
	public String getDescription() throws IOException {
		return origin.description();
	}
	
	@JsonGetter
	public UUID getMesureUnitId() throws IOException {
		return origin.unit().id();
	}
	
	@JsonGetter
	public String getMesureUnitShortName() throws IOException {
		return origin.unit().shortName();
	}
	
	@JsonGetter
	public String getMesureUnitFullName() throws IOException {
		return origin.unit().fullName();
	}
	
	@JsonGetter
	public String getTaxesDescription() throws IOException {
		String description = "";
		
		for (Tax tax : origin.taxes().all()) {
			description += String.format("%s (%d %s), ", tax.shortName(), tax.rate(), "%");
		}
		
		return description;
	}
	
	@JsonGetter
	public String getPriceSummary() throws IOException {
		return origin.pricing().priceSummary();
	}
	
	@JsonGetter
	public int getModeId() throws IOException {
		return origin.pricing().mode().id();
	}
	
	@JsonGetter
	public String getMode() throws IOException {
		return origin.pricing().mode().toString();
	}
}
