package com.pdv.domains.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.infrastructure.core.Nonable;
import com.sales.domains.api.ModulePdv;

public interface Pdv extends Nonable {
	UUID id();
	String name() throws IOException;
	boolean active() throws IOException;
	PdvProducts products() throws IOException;
	PdvProductCategories productCategories() throws IOException;
	Sessions sessions() throws IOException;
	PdvModule module() throws IOException;
	ModulePdv modulePdv() throws IOException;
	Cashiers cashiers() throws IOException;
	
	void update(String name) throws IOException;
	void activate(boolean active) throws IOException;
	
	double turnover(LocalDate start, LocalDate end) throws IOException;
}
