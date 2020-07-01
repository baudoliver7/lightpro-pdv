package com.pdv.domains.impl;

import java.io.IOException;
import java.time.LocalDateTime;

import com.infrastructure.core.GuidKeyEntityNone;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.Session;
import com.pdv.domains.api.PdvPurchaseOrders;
import com.pdv.domains.api.SessionStatus;
import com.securities.api.Contact;
import com.securities.impl.ContactNone;

public final class SessionNone extends GuidKeyEntityNone<Session> implements Session {

	@Override
	public String reference() throws IOException {
		return null;
	}

	@Override
	public Contact cashier() throws IOException {
		return new ContactNone();
	}

	@Override
	public LocalDateTime openedDate() throws IOException {
		return null;
	}

	@Override
	public LocalDateTime closedDate() throws IOException {
		return null;
	}

	@Override
	public SessionStatus status() throws IOException {
		return null;
	}

	@Override
	public Pdv pdv() throws IOException {
		return new PdvNone();
	}

	@Override
	public PdvPurchaseOrders orders() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public void terminate() throws IOException {

	}

	@Override
	public double turnover() throws IOException {
		return 0;
	}
}
