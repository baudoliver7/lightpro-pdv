package com.pdv.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.sales.domains.api.Seller;

public interface Cashiers extends AdvancedQueryable<Cashier, UUID> {
	Cashier add(Seller seller) throws IOException;
	
	Cashiers of(Pdv pdv) throws IOException;
}
