package com.pdv.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.infrastructure.core.Horodate;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvFreeProducts;
import com.pdv.domains.api.PdvMetadata;
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.api.PdvProducts;
import com.pdv.domains.api.PdvSessions;
import com.pdv.domains.api.SessionMetadata;
import com.pdv.domains.api.SessionPurchaseOrderMetadata;
import com.sales.domains.api.InvoiceMetadata;
import com.sales.domains.api.PaymentMetadata;
import com.sales.domains.api.PurchaseOrderMetadata;
import com.sales.domains.api.PurchaseOrderStatus;

public class PdvImpl implements Pdv {

	private final transient Base base;
	private final transient UUID id;
	private final transient PdvMetadata dm;
	private final transient DomainStore ds;	
	
	public PdvImpl(final Base base, final UUID id){
		this.base = base;
		this.id = id;
		this.dm = PdvMetadata.create();
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
	public boolean isEqual(Pdv item) {
		return this.id().equals(item.id());
	}

	@Override
	public boolean isNotEqual(Pdv item) {
		return !isEqual(item);
	}

	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public boolean active() throws IOException {
		return ds.get(dm.activeKey());
	}

	@Override
	public void update(String name) throws IOException {
		
		if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);	
		
		ds.set(params);	
	}

	@Override
	public void activate(boolean active) throws IOException {
		ds.set(dm.activeKey(), active);
	}

	@Override
	public PdvProducts productsToSale() {
		return new PdvProductsImpl(base, this);
	}

	@Override
	public PdvFreeProducts freeProducts() {
		return new PdvFreeProductsImpl(base, this);
	}

	@Override
	public PdvSessions sessions() {
		return new PdvSessionsImpl(base, this);
	}	

	@Override
	public double turnover(LocalDate start, LocalDate end) throws IOException {
		
		PurchaseOrderMetadata dmPo = PurchaseOrderMetadata.create();
		InvoiceMetadata dmInv = InvoiceMetadata.create();
		PaymentMetadata dmPay = PaymentMetadata.create();
		SessionPurchaseOrderMetadata dmSpo = SessionPurchaseOrderMetadata.create();
		SessionMetadata dmSs = SessionMetadata.create();
		DomainsStore ds = base.domainsStore(dmPay);
		
		String statement = String.format("SELECT SUM(pay.%s) FROM %s pay "
										+ "JOIN %s inv ON inv.%s=pay.%s "
										+ "left JOIN %s po ON po.%s = inv.%s "
										+ "left JOIN %s spo ON spo.%s = po.%s "										
										+ "left JOIN %s ss ON ss.%s = spo.%s "
										+ "left JOIN %s pd ON pd.%s = ss.%s "
										+ "WHERE (pay.%s BETWEEN ? AND ?) AND pd.%s=? AND po.%s=?", 
										dmPay.paidAmountKey(), dmPay.domainName(),
										dmInv.domainName(), dmInv.keyName(), dmPay.invoiceIdKey(),
										dmPo.domainName(), dmPo.keyName(), dmInv.purchaseOrderIdKey(),
										dmSpo.domainName(), dmSpo.keyName(), dmPo.keyName(),
										dmSs.domainName(), dmSs.keyName(), dmSpo.sessionIdKey(),
										dm.domainName(), dm.keyName(), dmSs.pdvIdKey(),
										dmPay.paymentDateKey(), dm.keyName(), dmPo.statusIdKey());
		
		List<Object> params = new ArrayList<Object>();
		params.add(java.sql.Date.valueOf(start));
		params.add(java.sql.Date.valueOf(end));
		params.add(this.id);
		params.add(PurchaseOrderStatus.ENTIRELY_INVOICED.id());
		
		List<Object> results = ds.find(statement, params);
		return Double.parseDouble(results.get(0) == null ? "0": results.get(0).toString());
	}

	@Override
	public PdvModule module() throws IOException {
		UUID moduleId = ds.get(dm.moduleIdKey());
		return new PdvModuleImpl(base, moduleId);
	}
}
