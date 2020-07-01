package com.pdv.domains.api;

import java.io.IOException;

import com.sales.domains.api.Seller;

public interface Cashier extends Seller {
	Pdv pdv() throws IOException;
	void changePdv(Pdv pdv) throws IOException;
}
