package com.lightpro.pdv.vm;

import java.io.IOException;
import java.util.UUID;

import com.sales.domains.api.ProductCategory;

public final class ProductCategoryVm {
	
	public final UUID id;
	public final String name;
	public final String description;
	public final long numberOfProducts;
	public final int typeId;
	public final String type;
	
	public ProductCategoryVm(){
		throw new UnsupportedOperationException("#ProductCategoryVm()");
	}
	
	public ProductCategoryVm(final ProductCategory origin) {
		try {
			this.id = origin.id();
	        this.name = origin.name();
	        this.description = origin.description();
	        this.numberOfProducts = origin.products().count();
	        this.typeId = origin.type().id();
	        this.type = origin.type().toString();	        
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
    }
}
