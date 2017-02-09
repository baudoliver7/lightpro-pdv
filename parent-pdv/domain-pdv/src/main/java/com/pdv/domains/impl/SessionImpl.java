package com.pdv.domains.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.Session;
import com.pdv.domains.api.SessionMetadata;
import com.pdv.domains.api.SessionPurchaseOrders;
import com.pdv.domains.api.SessionStatus;
import com.securities.api.Person;
import com.securities.impl.PersonImpl;

public class SessionImpl implements Session {

	private final transient Base base;
	private final transient UUID id;
	private final transient SessionMetadata dm;
	private final transient DomainStore ds;	
	
	public SessionImpl(final Base base, final UUID id){
		this.base = base;
		this.id = id;
		this.dm = SessionMetadata.create();
		this.ds = this.base.domainsStore(this.dm).createDs(id);	
	}
	
	@Override
	public Horodate horodate() {
		return new HorodateImpl(ds);
	}

	@Override
	public UUID id() {
		return this.id;
	}

	@Override
	public boolean isPresent() {
		return base.domainsStore(dm).exists(id);
	}

	@Override
	public boolean isEqual(Session item) {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(Session item) {
		return !isEqual(item);
	}

	@Override
	public String reference() throws IOException {
		return ds.get(dm.referenceKey());
	}

	@Override
	public Person cashier() throws IOException {
		UUID personId = ds.get(dm.cashierIdKey());
		return new PersonImpl(base, personId);
	}

	@Override
	public LocalDateTime openedDate() throws IOException {
		java.sql.Timestamp date = ds.get(dm.openedDateKey());
		return date.toLocalDateTime();
	}

	@Override
	public LocalDateTime closedDate() throws IOException {
		java.sql.Timestamp date = ds.get(dm.closedDateKey());
		return date.toLocalDateTime();
	}

	@Override
	public SessionStatus status() throws IOException {
		int statusId = ds.get(dm.statusIdKey());
		return SessionStatus.get(statusId);
	}

	@Override
	public Pdv pdv() throws IOException {
		UUID pdvId = ds.get(dm.pdvIdKey());
		return new PdvImpl(base, pdvId);
	}

	@Override
	public SessionPurchaseOrders orders() throws IOException {
		return pdv().module().orders(this);
	}

	@Override
	public void terminate() throws IOException {
		
		if(!orders().inProgress().isEmpty())
			throw new IllegalArgumentException("Vous avez des commandes en cours : veuillez les traiter, puis ensuite, clôturez la session.");
		
		ds.set(dm.closedDateKey(), java.sql.Timestamp.valueOf(LocalDateTime.now()));
		ds.set(dm.statusIdKey(), SessionStatus.CLOSED.id()); 
	}
}
