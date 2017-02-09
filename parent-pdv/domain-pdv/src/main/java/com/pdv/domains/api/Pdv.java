package com.pdv.domains.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.infrastructure.core.Recordable;

public interface Pdv extends Recordable<UUID, Pdv>{
	String name() throws IOException;
	boolean active() throws IOException;
	PdvProducts productsToSale();
	PdvFreeProducts freeProducts();
	PdvSessions sessions();
	PdvModule module()throws IOException;
	
	void update(String name) throws IOException;
	void activate(boolean active) throws IOException;
	
	double turnover(LocalDate start, LocalDate end) throws IOException;
}
