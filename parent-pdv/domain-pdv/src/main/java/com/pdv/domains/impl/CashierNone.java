package com.pdv.domains.impl;

import java.io.IOException;

import com.infrastructure.core.GuidKeyEntityNone;
import com.pdv.domains.api.Cashier;
import com.pdv.domains.api.Pdv;
import com.sales.domains.api.Team;
import com.sales.domains.impl.TeamNone;
import com.securities.api.Company;
import com.securities.api.ContactNature;
import com.securities.impl.CompanyNone;

public final class CashierNone extends GuidKeyEntityNone<Cashier> implements Cashier {

	@Override
	public void changeTeam(Team team) throws IOException {

	}

	@Override
	public Team team() throws IOException {
		return new TeamNone();
	}

	@Override
	public Company company() throws IOException {
		return new CompanyNone();
	}

	@Override
	public String fax() throws IOException {
		return null;
	}

	@Override
	public String locationAddress() throws IOException {
		return null;
	}

	@Override
	public String mail() throws IOException {
		return null;
	}

	@Override
	public String mobile() throws IOException {
		return null;
	}

	@Override
	public String name() throws IOException {
		return "Aucun caissier";
	}

	@Override
	public ContactNature nature() throws IOException {
		return ContactNature.NONE;
	}

	@Override
	public String phone() throws IOException {
		return null;
	}

	@Override
	public String photo() throws IOException {
		return null;
	}

	@Override
	public String poBox() throws IOException {
		return null;
	}

	@Override
	public void updateAddresses(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) throws IOException {

	}

	@Override
	public String webSite() throws IOException {
		return null;
	}

	@Override
	public boolean isNone() {
		return true;
	}

	@Override
	public Pdv pdv() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changePdv(Pdv pdv) throws IOException {
		// TODO Auto-generated method stub

	}

}
