package com.pdv.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.api.PdvPurchaseOrderStatus;
import com.pdv.domains.api.Session;
import com.pdv.domains.api.SessionMetadata;
import com.pdv.domains.api.PdvPurchaseOrders;
import com.pdv.domains.api.SessionStatus;
import com.sales.domains.api.PaymentMetadata;
import com.securities.api.Contact;

public final class SessionDb extends GuidKeyEntityDb<Session, SessionMetadata> implements Session {
	
	private final PdvModule module;
	
	public SessionDb(final Base base, final UUID id, final PdvModule module){
		super(base, id, "Session introuvable !");
		this.module = module;
	}
	
	@Override
	public String reference() throws IOException {
		return ds.get(dm.referenceKey());
	}

	@Override
	public Contact cashier() throws IOException {
		UUID personId = ds.get(dm.cashierIdKey());
		return pdv().module().contacts().get(personId);
	}

	@Override
	public LocalDateTime openedDate() throws IOException {
		java.sql.Timestamp date = ds.get(dm.openedDateKey());
		return date.toLocalDateTime();
	}

	@Override
	public LocalDateTime closedDate() throws IOException {
		java.sql.Timestamp date = ds.get(dm.closedDateKey());
		
		if(date == null)
			return null;
		
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
		return new PdvDb(base, pdvId, module);
	}

	@Override
	public PdvPurchaseOrders orders() throws IOException {
		return pdv().module().orders().of(this);
	}

	@Override
	public void terminate() throws IOException {
		
		if(orders().with(PdvPurchaseOrderStatus.IN_USE).count() > 0)
			throw new IllegalArgumentException("Vous avez des commandes en cours : veuillez les traiter, puis ensuite, clôturez la session.");
		
		ds.set(dm.closedDateKey(), java.sql.Timestamp.valueOf(LocalDateTime.now()));
		ds.set(dm.statusIdKey(), SessionStatus.CLOSED.id()); 
	}

	@Override
	public double turnover() throws IOException {		
		PaymentMetadata dmPay = PaymentMetadata.create();
		DomainsStore ds = base.domainsStore(dmPay);
		
		String statement = String.format("SELECT SUM(pay.%s) FROM %s pay "
										+ "WHERE pay.%s BETWEEN ? AND ? AND pay.%s=? AND pay.%s=?", 
										dmPay.paidAmountKey(), dmPay.domainName(),										
										dmPay.paymentDateKey(), dmPay.modulePdvIdKey(), dmPay.cashierIdKey());
		
		List<Object> params = new ArrayList<Object>();
		params.add(java.sql.Date.valueOf(openedDate().toLocalDate()));
		params.add(closedDate() == null ? java.sql.Date.valueOf(LocalDate.now()) : java.sql.Date.valueOf(closedDate().toLocalDate()));
		params.add(pdv().id());
		params.add(cashier().id());
		
		List<Object> results = ds.find(statement, params);
		return Double.parseDouble(results.get(0) == null ? "0": results.get(0).toString());
	}
}
