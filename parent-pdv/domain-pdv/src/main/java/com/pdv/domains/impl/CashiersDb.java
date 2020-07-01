package com.pdv.domains.impl;

import java.io.IOException;
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
import com.pdv.domains.api.Cashier;
import com.pdv.domains.api.CashierMetadata;
import com.pdv.domains.api.Cashiers;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvMetadata;
import com.pdv.domains.api.PdvModule;
import com.sales.domains.api.Seller;
import com.sales.domains.api.SellerMetadata;
import com.securities.api.ContactMetadata;

public final class CashiersDb extends GuidKeyAdvancedQueryableDb<Cashier, CashierMetadata> implements Cashiers {

	private transient final PdvModule module;
	private transient final Pdv pdv;
	
	public CashiersDb(final Base base, final PdvModule module, final Pdv pdv){
		super(base, "Caissier introuvable !");
		this.module = module;
		this.pdv = pdv;
	}

	@Override
	public Cashier add(Seller seller) throws IOException {
		
		if(pdv.isNone())
			throw new IllegalArgumentException("Vous devez spécifier un point de vente !");
		
		if(seller.isNone())
			throw new IllegalArgumentException("Vous devez spécifier un vendeur !");
		
		Cashier cashier = build(seller.id());
		
		if(contains(cashier))
			throw new IllegalArgumentException("Le caissier exerce déjà à ce point de vente !");
		
		if(ds.exists(seller.id()))
			throw new IllegalArgumentException("Le caissier exerce déjà dans un autre point de vente  !");
			
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.pdvIdKey(), pdv.id());	
		
		ds.set(seller.id(), params);		
	
		return cashier;
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		SellerMetadata selDm = SellerMetadata.create();
		PdvMetadata pdvDm = PdvMetadata.create();
		ContactMetadata persDm = ContactMetadata.create();
		
		String statement = String.format("%s ca "
				+ "JOIN %s sl ON sl.%s = ca.%s "
                + "left JOIN %s pd ON pd.%s = ca.%s "
				+ "JOIN view_contacts vctc ON vctc.%s = ca.%s "
				+ "WHERE (vctc.name1 ILIKE ? OR vctc.name2 ILIKE ?) AND pd.%s=? ", 
				dm.domainName(), 
				selDm.domainName(), selDm.keyName(), dm.keyName(),
				pdvDm.domainName(), pdvDm.keyName(), dm.pdvIdKey(),
				persDm.keyName(), dm.keyName(),
				pdvDm.moduleIdKey());
		
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(module.id());
		
		if(!pdv.isNone()){
			statement = String.format("%s AND pd.%s=?", statement, pdvDm.keyName());
			params.add(pdv.id());
		}
		
		HorodateMetadata horodateDm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY ca.%s DESC", horodateDm.dateCreatedKey());
		
		String keyResult = String.format("ca.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected Cashier newOne(UUID id) {
		try {
			return new CashierDb(base, id, pdv.module());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Cashier none() {
		return new CashierNone();
	}

	@Override
	public Cashiers of(Pdv pdv) throws IOException {
		return new CashiersDb(base, module, pdv);
	}
}
