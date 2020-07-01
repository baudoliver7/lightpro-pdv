package com.lightpro.pdv.vm;

import java.io.IOException;
import java.util.UUID;

import com.sales.domains.api.Product;

public final class ProductVm {
	
	public final UUID id;
	public final String name;
	public final String barCode;
	public final String description;
	public final UUID mesureUnitId;
	public final String mesureUnitShortName;
	public final String mesureUnitFullName;
	public final String taxesDescription;
	public final String pricingSummary;
	public final int pricingModeId;
	public final String pricingMode;
	public final UUID categoryId;
	public final String category;
	public final double quantity;
	public final String emballage;
	public final String internalReference;
	
	public ProductVm(){
		throw new UnsupportedOperationException("#ProductVm()");
	}
	
	public ProductVm(final Product origin) {
		try {
			this.id = origin.id();
			this.name = origin.name();			
			this.barCode = origin.barCode();
			this.description = origin.description();
			this.mesureUnitId = origin.unit().id();
			this.mesureUnitShortName = origin.unit().shortName();
			this.mesureUnitFullName = origin.unit().fullName();
			this.taxesDescription = origin.taxes().toString();
			this.pricingSummary = origin.pricing().toString();
			this.pricingMode = origin.pricing().mode().toString();
	        this.pricingModeId = origin.pricing().mode().id();
	        this.category = origin.category().name();
	        this.categoryId = origin.category().id();
	        this.quantity = origin.quantity();
	        this.emballage = origin.emballage();
	        this.internalReference = origin.internalReference();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
}
