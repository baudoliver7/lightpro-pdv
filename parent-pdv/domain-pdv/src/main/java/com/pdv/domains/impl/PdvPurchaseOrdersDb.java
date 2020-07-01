package com.pdv.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvMetadata;
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.api.PdvPurchaseOrder;
import com.pdv.domains.api.Session;
import com.pdv.domains.api.SessionMetadata;
import com.pdv.domains.api.PdvPurchaseOrderMetadata;
import com.pdv.domains.api.PdvPurchaseOrderStatus;
import com.pdv.domains.api.PdvPurchaseOrders;
import com.sales.domains.api.ModulePdv;
import com.sales.domains.api.PaymentConditionStatus;
import com.sales.domains.api.PurchaseOrder;
import com.sales.domains.api.PurchaseOrderMetadata;
import com.sales.domains.api.PurchaseOrderStatus;
import com.sales.domains.api.Sales;
import com.sales.domains.api.Seller;
import com.securities.api.Contact;

public final class PdvPurchaseOrdersDb extends GuidKeyAdvancedQueryableDb<PdvPurchaseOrder, PdvPurchaseOrderMetadata> implements PdvPurchaseOrders {
	
	private final transient Session session;
	private final transient PdvModule module;
	private final transient Pdv pdv;
	private final transient PdvPurchaseOrderStatus status;
	private final Sales sales;
	
	public PdvPurchaseOrdersDb(final Base base, final PdvModule module, final Sales sales, final Pdv pdv, final Session session, final PdvPurchaseOrderStatus status){		
		super(base, "Commande introuvable !");
		this.sales = sales;
		this.session = session;
		this.module = module;
		this.pdv = pdv;
		this.status = status;
	}

	@Override
	public void delete(PdvPurchaseOrder item) throws IOException {
		
		if(contains(item)) {
			ds.delete(item.id());
			sales.purchases().delete(item);	
		}
	}

	@Override
	public PdvPurchaseOrder add(LocalDate date, LocalDate expirationDate, PaymentConditionStatus paymentCondition, String cgv, String description, String notes, Contact customer, Seller seller, int livraisonDelayInDays) throws IOException {	
		
		if(pdv.isNone())
			throw new IllegalArgumentException("Vous devez spécifier un point de vente !");
		
		ModulePdv modulePointDeVente = pdv.modulePdv();
		PurchaseOrder item = sales.purchases().of(modulePointDeVente).add(date, expirationDate, paymentCondition, cgv, description, notes, customer, seller, livraisonDelayInDays);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.sessionIdKey(), session.id());	
		
		ds.set(item.id(), params);	
		
		return build(item.id());
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		PurchaseOrderMetadata poDm = new PurchaseOrderMetadata();
		SessionMetadata sessDm = new SessionMetadata();
		PdvMetadata pdvDm = new PdvMetadata();
		
		String statement = String.format("%s po "
				+ "JOIN %s pdvpo on pdvpo.%s=po.%s "
				+ "LEFT JOIN %s sess on sess.%s=pdvpo.%s "
				+ "LEFT JOIN %s pdv on pdv.%s=sess.%s "
				+ "WHERE po.%s ILIKE ? AND pdv.%s=?",
				poDm.domainName(), 
				dm.domainName(), dm.keyName(), poDm.keyName(),
				sessDm.domainName(), sessDm.keyName(), dm.sessionIdKey(),
				pdvDm.domainName(), pdvDm.keyName(), sessDm.pdvIdKey(),
				poDm.referenceKey(), pdvDm.moduleIdKey());
		
		params.add("%" + filter + "%");
		params.add(module.id());
		
		if(!pdv.isNone()){
			statement = String.format("%s AND pdv.%s=?", statement, pdvDm.keyName());
			params.add(pdv.id());
		}
		
		if(!session.isNone()){
			statement = String.format("%s AND sess.%s=?", statement, sessDm.keyName());
			params.add(session.id());
		}
		
		if(status != PdvPurchaseOrderStatus.NONE){
			if(status == PdvPurchaseOrderStatus.IN_USE) {
				statement = String.format("%s AND po.%s=?", statement, poDm.statusIdKey());
				params.add(PurchaseOrderStatus.CREATED.id());
			}
			
			if(status == PdvPurchaseOrderStatus.DONE) {
				statement = String.format("%s AND (po.%s=? OR po.%s=?)", statement, poDm.statusIdKey(), poDm.statusIdKey());
				params.add(PurchaseOrderStatus.ENTIRELY_INVOICED.id());
				params.add(PurchaseOrderStatus.PAID.id());
			}
		}
		
		HorodateMetadata horodateDm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY po.%s DESC", horodateDm.dateCreatedKey());
		
		String keyResult = String.format("po.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected PdvPurchaseOrder newOne(UUID id) {
		return new PdvPurchaseOrderDb(base, id, sales);
	}

	@Override
	public PdvPurchaseOrder none() {
		return new PdvPurchaseOrderNone();
	}

	@Override
	public PdvPurchaseOrders of(Pdv pdv) throws IOException {
		return new PdvPurchaseOrdersDb(base, module, sales, pdv, new SessionNone(), status);
	}

	@Override
	public PdvPurchaseOrders of(Session session) throws IOException {
		return new PdvPurchaseOrdersDb(base, module, sales, session.pdv(), session, status);
	}

	@Override
	public PdvPurchaseOrders with(PdvPurchaseOrderStatus status) throws IOException {
		return new PdvPurchaseOrdersDb(base, module, sales, pdv, session, status);
	}
}
