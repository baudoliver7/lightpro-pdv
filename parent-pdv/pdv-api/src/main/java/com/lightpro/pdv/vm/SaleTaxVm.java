package com.lightpro.pdv.vm;

import java.io.IOException;
import java.util.UUID;

import com.sales.domains.api.SaleTax;

public final class SaleTaxVm {
	
	public final String type;
	public final int typeId;
	public final UUID id;
	public final String name;
	public final String shortName;
	public final String resumeName;
	public final double value;
	public final int valueTypeId;
	public final String valueSummary;
	public final double amount;
	
	public SaleTaxVm(){
		throw new UnsupportedOperationException("#SaleTaxVm()");
	}
		
	public SaleTaxVm(SaleTax origin){
		try {
			this.id = origin.id();
			this.name = origin.name();
			this.shortName = origin.shortName();
	        this.value = origin.value();
	        this.valueTypeId = origin.valueType().id();
	        this.type = origin.type().toString();
	        this.typeId = origin.type().id();
	        this.valueSummary = origin.valueToString();
	        this.resumeName = origin.toString();
	        this.amount = origin.amount();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
}
