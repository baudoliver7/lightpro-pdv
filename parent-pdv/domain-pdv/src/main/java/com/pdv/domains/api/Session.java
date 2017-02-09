package com.pdv.domains.api;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import com.infrastructure.core.Recordable;
import com.securities.api.Person;

public interface Session extends Recordable<UUID, Session> {
	String reference() throws IOException;
	Person cashier() throws IOException;
	LocalDateTime openedDate() throws IOException;
	LocalDateTime closedDate() throws IOException;
	SessionStatus status() throws IOException;
	Pdv pdv() throws IOException;
	SessionPurchaseOrders orders() throws IOException;
	
	void terminate() throws IOException;
}
