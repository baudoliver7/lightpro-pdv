package com.pdv.domains.api;

import java.io.IOException;

import com.sales.domains.api.ModulePdvs;
import com.sales.domains.api.Sellers;
import com.securities.api.Contacts;
import com.securities.api.Module;
import com.securities.api.PaymentModes;

public interface PdvModule extends Module {
	Pdvs pdvs() throws IOException;	
	PdvPurchaseOrders orders() throws IOException;
	Sessions sessions() throws IOException;
	Contacts contacts() throws IOException;
	Cashiers cashiers() throws IOException;
	PaymentModes paymentModes() throws IOException;
	Sellers sellers() throws IOException;
	ModulePdvs modulePdvs() throws IOException;
}
