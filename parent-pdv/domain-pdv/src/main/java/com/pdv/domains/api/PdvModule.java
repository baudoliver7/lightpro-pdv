package com.pdv.domains.api;

import java.io.IOException;

import com.sales.domains.api.Customers;
import com.sales.domains.api.Products;
import com.securities.api.Membership;
import com.securities.api.Module;
import com.securities.api.Persons;

public interface PdvModule extends Module {
	Pdvs pdvs() throws IOException;
	Products productCatalog() throws IOException;
	SessionPurchaseOrders orders(final Session session) throws IOException;
	Sessions sessions() throws IOException;
	Persons persons() throws IOException;
	Customers customers() throws IOException;
	Membership membership() throws IOException;
}
