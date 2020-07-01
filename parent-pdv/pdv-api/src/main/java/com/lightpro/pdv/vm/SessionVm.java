package com.lightpro.pdv.vm;

import java.io.IOException;
import java.util.UUID;

import com.pdv.domains.api.Session;

public final class SessionVm {
	
	public final UUID id;
	public final String reference;
	public final UUID pdvId;
	public final String pdv;
	public final UUID cashierId;
	public final String cashier;
	
	public SessionVm(){
		throw new UnsupportedOperationException("#SessionVm()");
	}
	
	public SessionVm(final Session origin) {
		try {
			this.id = origin.id();
			this.reference = origin.reference();
			this.pdvId = origin.pdv().id();
	        this.pdv = origin.pdv().name();
	        this.cashierId = origin.cashier().id();
	        this.cashier = origin.cashier().name();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
}
