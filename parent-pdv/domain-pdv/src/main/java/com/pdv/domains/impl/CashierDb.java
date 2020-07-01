package com.pdv.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.pdv.domains.api.Cashier;
import com.pdv.domains.api.CashierMetadata;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvModule;
import com.sales.domains.api.Seller;
import com.sales.domains.api.Team;
import com.securities.api.Company;
import com.securities.api.ContactNature;

public final class CashierDb extends GuidKeyEntityDb<Cashier, CashierMetadata> implements Cashier {

	private transient final Seller origin;
	private transient PdvModule module;
	
	public CashierDb(final Base base, final UUID id, final PdvModule module){
		super(base, id, "Caissier introuvable !");
		this.module = module;
		
		try {
			this.origin = module.sellers().get(id);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void changeTeam(Team newTeam) throws IOException {
		origin.changeTeam(newTeam);
	}

	@Override
	public Team team() throws IOException {
		return origin.team();
	}

	@Override
	public Company company() throws IOException {
		return origin.company();
	}

	@Override
	public String photo() throws IOException {
		return origin.photo();
	}

	@Override
	public Pdv pdv() throws IOException {
		UUID pdvId = ds.get(dm.pdvIdKey());
		return new PdvDb(base, pdvId, module);
	}

	@Override
	public void changePdv(Pdv pdv) throws IOException {
		if(pdv.isNone())
			throw new IllegalArgumentException("Le point de vente n'existe pas !");
		
		if(pdv.equals(pdv()))
			throw new IllegalArgumentException("Le caissier appartient déjà à ce point de vente !");
		
		ds.set(dm.pdvIdKey(), pdv.id());
	}

	@Override
	public String fax() throws IOException {
		return origin.fax();
	}

	@Override
	public String locationAddress() throws IOException {
		return origin.locationAddress();
	}

	@Override
	public String mail() throws IOException {
		return origin.mail();
	}

	@Override
	public String mobile() throws IOException {
		return origin.mobile();
	}

	@Override
	public String name() throws IOException {
		return origin.name();
	}

	@Override
	public ContactNature nature() throws IOException {
		return origin.nature();
	}

	@Override
	public String phone() throws IOException {
		return origin.phone();
	}

	@Override
	public String poBox() throws IOException {
		return origin.poBox();
	}

	@Override
	public void updateAddresses(String locationAddress, String phone, String mobile, String fax, String mail, String poBox, String webSite) throws IOException {
		origin.updateAddresses(locationAddress, phone, mobile, fax, mail, poBox, webSite);
	}

	@Override
	public String webSite() throws IOException {
		return origin.webSite();
	}
}
