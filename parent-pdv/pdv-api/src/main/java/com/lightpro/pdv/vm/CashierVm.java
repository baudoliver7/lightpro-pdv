package com.lightpro.pdv.vm;

import java.io.IOException;
import java.util.UUID;

import com.sales.domains.api.Seller;

public final class CashierVm {
	
	public final UUID id;
	public final String name;
	public final String locationAddress;
	public final String phone;
	public final String mobile;
	public final String fax;
	public final String mail;
	public final String poBox;
	public final String webSite;
	public final String photo;
	public final int natureId;
	public final String nature;	
	public final String team;
	public final UUID teamId;
	
	public CashierVm(){
		throw new UnsupportedOperationException("#CashierVm()");
	}
	
	public CashierVm(final Seller contact) {
		try {
			this.id = contact.id();
	        this.name = contact.name();
	        this.locationAddress = contact.locationAddress();
	        this.phone = contact.phone();
	        this.mobile = contact.mobile();
	        this.fax = contact.fax();
	        this.mail = contact.mail();
	        this.poBox = contact.poBox();
	        this.webSite = contact.webSite();
	        this.photo = contact.photo();
	        this.natureId = contact.nature().id();
	        this.nature = contact.nature().toString();
	        this.team = contact.team().name();
	        this.teamId = contact.team().id();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
    }
}
