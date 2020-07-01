package com.pdv.domains.api;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import com.infrastructure.core.Nonable;
import com.securities.api.Contact;

public interface Session extends Nonable {
	UUID id();
	String reference() throws IOException;
	Contact cashier() throws IOException;
	LocalDateTime openedDate() throws IOException;
	LocalDateTime closedDate() throws IOException;
	SessionStatus status() throws IOException;
	Pdv pdv() throws IOException;
	PdvPurchaseOrders orders() throws IOException;
	
	void terminate() throws IOException;
	double turnover() throws IOException;
}
