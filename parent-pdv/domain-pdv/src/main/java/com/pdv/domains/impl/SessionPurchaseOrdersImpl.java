package com.pdv.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.NotFoundException;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.pdv.domains.api.Session;
import com.pdv.domains.api.SessionPurchaseOrderMetadata;
import com.pdv.domains.api.SessionPurchaseOrders;
import com.sales.domains.api.Customer;
import com.sales.domains.api.Invoice;
import com.sales.domains.api.Payment;
import com.sales.domains.api.PaymentConditionStatus;
import com.sales.domains.api.PaymentMode;
import com.sales.domains.api.PurchaseOrder;
import com.sales.domains.api.PurchaseOrderMetadata;
import com.sales.domains.api.PurchaseOrderStatus;
import com.sales.domains.api.PurchaseOrders;
import com.securities.api.User;

public class SessionPurchaseOrdersImpl implements SessionPurchaseOrders {
	
	private transient final Base base;
	private final transient SessionPurchaseOrderMetadata dm;
	private final transient DomainsStore ds;
	private final transient Session session;
	private final transient PurchaseOrders origin;
	
	public SessionPurchaseOrdersImpl(final Base base, final Session session, final PurchaseOrders order){		
		this.base = base;
		this.dm = SessionPurchaseOrderMetadata.create();		
		this.ds = this.base.domainsStore(this.dm);	
		this.session = session;
		this.origin = order;
	}

	@Override
	public List<PurchaseOrder> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	public List<PurchaseOrder> find(int page, int pageSize, String filter) throws IOException {
		List<PurchaseOrder> values = new ArrayList<PurchaseOrder>();
		
		HorodateMetadata hm = HorodateImpl.dm();
		PurchaseOrderMetadata pDm = PurchaseOrderMetadata.create();
		String statement = String.format("SELECT %s FROM %s WHERE %s=? "
				+ "AND %s IN (SELECT %s FROM %s WHERE %s ILIKE ?) "
				+ "ORDER BY %s ASC LIMIT ? OFFSET ?", 
				dm.keyName(), dm.domainName(), dm.sessionIdKey(), 
				dm.keyName(), pDm.keyName(), pDm.domainName(), pDm.referenceKey(),
				hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add(session.id());
		params.add("%" + filter + "%");
		
		if(pageSize > 0){
			params.add(pageSize);
			params.add((page - 1) * pageSize);
		}else{
			params.add(null);
			params.add(0);
		}
		
		List<DomainStore> results = ds.findDs(statement, params);
		for (DomainStore domainStore : results) {
			values.add(build(UUIDConvert.fromObject(domainStore.key()))); 
		}		
		
		return values;
	}

	@Override
	public int totalCount(String filter) throws IOException {
		PurchaseOrderMetadata pDm = PurchaseOrderMetadata.create();
		String statement = String.format("SELECT COUNT(%s) FROM %s WHERE %s=? "
				+ "AND %s IN (SELECT %s FROM %s WHERE %s ILIKE ?) ",
				dm.keyName(), dm.domainName(), dm.sessionIdKey(), 
				dm.keyName(), pDm.keyName(), pDm.domainName(), pDm.referenceKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add(session.id());
		params.add("%" + filter + "%");
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());	
	}

	@Override
	public List<PurchaseOrder> all() throws IOException {
		return find(0, 0, "");
	}

	@Override
	public PurchaseOrder build(UUID id) {
		return origin.build(id);
	}

	@Override
	public boolean contains(PurchaseOrder item) {
		return origin.contains(item) && ds.exists(item.id());
	}

	@Override
	public PurchaseOrder get(UUID id) throws IOException {
		PurchaseOrder item = origin.get(id);
		
		if(!contains(item))
			throw new NotFoundException("Le bon de commande n'a pas été trouvé dans le point de vente !");
		
		return item;
	}

	@Override
	public void delete(PurchaseOrder item) throws IOException {
		
		if(contains(item))
		{
			ds.delete(item.id());
			origin.delete(item);		
		}
	}

	@Override
	public PurchaseOrder add(LocalDate date, LocalDate expirationDate, PaymentConditionStatus paymentCondition, String cgv, String notes, Customer customer, User seller) throws IOException {		
		PurchaseOrder item = origin.add(date, expirationDate, paymentCondition, cgv, notes, customer, seller);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.sessionIdKey(), session.id());	
		
		ds.set(item.id(), params);	
		
		return item;
	}

	@Override
	public Payment pay(LocalDate paymentDate, UUID orderId, PaymentMode mode, double montantVerse) throws IOException {
		
		if(montantVerse <= 0)
			throw new IllegalArgumentException("Vous devez spéficier un montant versé !");
		
		if(mode == PaymentMode.NONE)
			throw new IllegalArgumentException("Vous devez spéficier un mode de paiement !");
		
		// 1 - générer une facture
		PurchaseOrder order = get(orderId);
		Invoice invoice = order.invoices().generateFinalInvoice(paymentDate);
		
		// 2 - faire le paiement
		double paidAmount = 0;
		double totalAmountTtc = invoice.totalAmountTtc();
		
		if(montantVerse >= totalAmountTtc)
			paidAmount = totalAmountTtc;
		else
			paidAmount = montantVerse;
		
		return invoice.payments().add(paymentDate, String.format("Paiement de la facture %s du point de vente %s", invoice.reference(), session.pdv().name()), paidAmount, mode);		
	}

	@Override
	public List<PurchaseOrder> inProgress() throws IOException {
		return session.orders()
					   .all()
					   .stream()
					   .filter(m -> {
							try {
								return m.status() == PurchaseOrderStatus.DRAFT;
							} catch (IOException e) {
								e.printStackTrace();
							}
							return false;
						})
					   .map(m -> m)
					   .collect(Collectors.toList());
	}

	@Override
	public List<PurchaseOrder> done() throws IOException {
		return session.orders()
				   .all()
				   .stream()
				   .filter(m -> {
						try {
							return m.status() == PurchaseOrderStatus.ENTIRELY_INVOICED;
						} catch (IOException e) {
							e.printStackTrace();
						}
						return false;
					})
				   .map(m -> m)
				   .collect(Collectors.toList());
	}
}
