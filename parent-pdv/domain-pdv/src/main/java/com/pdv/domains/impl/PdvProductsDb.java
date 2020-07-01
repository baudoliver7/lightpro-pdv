package com.pdv.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.UseCode;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvMetadata;
import com.pdv.domains.api.PdvProductCategoryMetadata;
import com.pdv.domains.api.PdvProductMetadata;
import com.pdv.domains.api.PdvProducts;
import com.sales.domains.api.Product;
import com.sales.domains.api.ProductCategory;
import com.sales.domains.api.ProductCategoryMetadata;
import com.sales.domains.api.ProductMetadata;
import com.sales.domains.api.Sales;
import com.sales.domains.impl.ProductNone;

public final class PdvProductsDb extends GuidKeyAdvancedQueryableDb<Product, PdvProductMetadata> implements PdvProducts {

	private final transient Pdv pdv;
	private final transient ProductCategory category;
	private final transient boolean isFree;
	private final Sales sales;
	
	public PdvProductsDb(final Base base, final Pdv pdv, final Sales sales, final ProductCategory category, final boolean isFree){		
		super(base, "Produit introuvable !");
		this.pdv = pdv;
		this.category = category;
		this.isFree = isFree;
		this.sales = sales;
		
		if(pdv.isNone())
			throw new IllegalArgumentException("Le point de vente doit être spécifié !");
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		if(isFree)
			return buildQueryFreeProduct(filter);
		else
			return buildQueryProductToSale(filter);		
	}

	private QueryBuilder buildQueryFreeProduct(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		ProductMetadata pDm = new ProductMetadata();
		ProductCategoryMetadata pdcatDm = ProductCategoryMetadata.create();
		PdvProductCategoryMetadata ppcDm = PdvProductCategoryMetadata.create();
		String statement = String.format("%s p "
				                        + "JOIN %s pdcat ON pdcat.%s=p.%s "
										+ "WHERE p.%s NOT IN (SELECT pp.%s FROM %s pp WHERE pp.%s=?) "
				                        + "AND (p.%s ILIKE ? OR p.%s ILIKE ? OR p.%s ILIKE ?) AND pdcat.%s=? AND pdcat.%s=? "
				                        + "AND pdcat.%s IN (SELECT ppc.%s FROM %s ppc WHERE ppc.%s=?)",
										pDm.domainName(), 
										pdcatDm.domainName(), pdcatDm.keyName(), pDm.categoryIdKey(),
										pDm.keyName(), dm.productIdKey(), dm.domainName(), dm.pdvIdKey(), 
										pDm.nameKey(), pDm.barCodeKey(), pDm.descriptionKey(), pdcatDm.moduleIdKey(), pdcatDm.useCodeKey(),
										pdcatDm.keyName(), ppcDm.categoryIdKey(), ppcDm.domainName(), ppcDm.pdvIdKey());
		
		params.add(pdv.id());
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(sales.id());
		params.add(UseCode.USER.id());
		params.add(pdv.id());
		
		if(!category.isNone()){
			statement = String.format("%s AND pdcat.%s=?", statement, pdcatDm.keyName());
			params.add(category.id());
		}
		
		HorodateMetadata hm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY p.%s DESC", hm.dateCreatedKey());
		
		String keyResult = String.format("p.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);		
	}
	
	private QueryBuilder buildQueryProductToSale(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
				
		ProductMetadata pDm = new ProductMetadata();
		ProductCategoryMetadata pdcatDm = new ProductCategoryMetadata();
		PdvMetadata pdvDm = PdvMetadata.create();
		String statement = String.format("%s p "
										+ "JOIN %s pp ON pp.%s = p.%s "
										+ "left JOIN %s pd ON pd.%s = pp.%s "
										+ "JOIN %s pdcat ON pdcat.%s=p.%s "										
										+ "WHERE (p.%s ILIKE ? OR p.%s ILIKE ? OR p.%s ILIKE ?) AND pd.%s=? AND p.%s=? AND pdcat.%s=? ", 
										pDm.domainName(), 
										dm.domainName(), dm.productIdKey(), pDm.keyName(),
										pdvDm.domainName(), pdvDm.keyName(), dm.pdvIdKey(),
										pdcatDm.domainName(), pdcatDm.keyName(), pDm.categoryIdKey(),										
										pDm.nameKey(), pDm.barCodeKey(), pDm.descriptionKey(), pdvDm.keyName(), pDm.moduleIdKey(), pdcatDm.useCodeKey());
		
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");		
		params.add(pdv.id());
		params.add(sales.id());
		params.add(UseCode.USER.id());
		
		if(!category.isNone()){
			statement = String.format("%s AND pdcat.%s=?", statement, pdcatDm.keyName());
			params.add(category.id());
		}
		
		HorodateMetadata hm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY p.%s DESC", hm.dateCreatedKey());
		
		String keyResult = String.format("p.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}
	
	@Override
	public void delete(Product item) throws IOException {
		String statement = String.format("DELETE FROM %s WHERE %s=? AND %s=? ", 
									      dm.domainName(), dm.productIdKey(), dm.pdvIdKey());
		ds.execute(statement, Arrays.asList(item.id(), pdv.id()));		
	}

	@Override
	public void add(Product item) throws IOException {
		if(contains(item))
			return;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.pdvIdKey(), pdv.id());	
		params.put(dm.productIdKey(), item.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
	}

	@Override
	public PdvProducts of(ProductCategory category) throws IOException {
		return new PdvProductsDb(base, pdv, sales, category, isFree);
	}

	@Override
	public PdvProducts freeProducts() {
		return new PdvProductsDb(base, pdv, sales, category, true);
	}

	@Override
	public PdvProducts selectedProducts() {
		return new PdvProductsDb(base, pdv, sales, category, false);
	}

	@Override
	protected Product newOne(UUID id) {
		Product origin;
		
		try {
			origin = sales.products().get(id);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		if(isFree)
			return origin;
		else
			return new PdvProductDb(base, origin, pdv);
	}

	@Override
	public Product none() {
		return new ProductNone();
	}
}
