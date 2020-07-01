package com.pdv.domains.impl;

import java.io.IOException;
import java.time.LocalDate;

import com.infrastructure.core.GuidKeyEntityNone;
import com.pdv.domains.api.Cashiers;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.api.PdvProductCategories;
import com.pdv.domains.api.PdvProducts;
import com.pdv.domains.api.Sessions;
import com.sales.domains.api.ModulePdv;
import com.sales.domains.impl.ModulePdvNone;

public final class PdvNone extends GuidKeyEntityNone<Pdv> implements Pdv {

	@Override
	public boolean isNone() {
		return true;
	}

	@Override
	public String name() throws IOException {
		return "Aucun point de vente";
	}

	@Override
	public boolean active() throws IOException {
		return false;
	}

	@Override
	public Sessions sessions() {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public PdvModule module() throws IOException {
		return new PdvModuleNone();
	}

	@Override
	public Cashiers cashiers() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public void update(String name) throws IOException {

	}

	@Override
	public void activate(boolean active) throws IOException {

	}

	@Override
	public double turnover(LocalDate start, LocalDate end) throws IOException {
		return 0;
	}

	@Override
	public PdvProducts products() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public PdvProductCategories productCategories() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public ModulePdv modulePdv() throws IOException {
		return new ModulePdvNone();
	}
}
