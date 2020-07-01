package com.lightpro.pdv.vm;

import java.io.IOException;
import java.util.UUID;

import com.pdv.domains.api.Pdv;

public final class PdvVm {
	
	public final UUID id;
	public final String name;
	public final boolean active;
	public final long numberOfProducts;
	public final long numberOfProductCategories;
	public final long numberOfCashiers;
	
	public PdvVm(){
		throw new UnsupportedOperationException("#PdvVm()");
	}
	
	public PdvVm(final Pdv origin) {
		try {
			this.id = origin.id();
	        this.name = origin.name();
	        this.active = origin.active();
	        this.numberOfProducts = origin.products().count();
	        this.numberOfProductCategories = origin.productCategories().count();
	        this.numberOfCashiers = origin.cashiers().count();	        
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
}
