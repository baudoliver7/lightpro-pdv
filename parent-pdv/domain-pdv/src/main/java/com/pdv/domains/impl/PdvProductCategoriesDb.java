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
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.api.PdvProductCategories;
import com.pdv.domains.api.PdvProductCategoryMetadata;
import com.sales.domains.api.ProductCategory;
import com.sales.domains.api.ProductCategoryMetadata;
import com.sales.domains.api.Sales;
import com.sales.domains.impl.ProductCategoryNone;

public final class PdvProductCategoriesDb extends GuidKeyAdvancedQueryableDb<ProductCategory, PdvProductCategoryMetadata> implements PdvProductCategories {

	private final transient Pdv pdv;
	private final transient PdvModule module;
	private final transient boolean isFree;
	private final transient Sales sales;
	
	public PdvProductCategoriesDb(final Base base, final PdvModule module, final Sales sales, final Pdv pdv, final boolean isFree){		
		super(base, "Catégorie de produit introuvable !");	
		this.pdv = pdv;
		this.module = module;
		this.isFree = isFree;
		this.sales = sales;
		
		if(pdv.isNone())
			throw new IllegalArgumentException("Vous devez spécifier un point de vente !");
	}
	
	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {		
		if(isFree)
			return buildQueryFreeProductCategory(filter);
		else
			return buildQueryProductCategoryToSale(filter);
	}
	
	private QueryBuilder buildQueryFreeProductCategory(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		ProductCategoryMetadata pDm = ProductCategoryMetadata.create();
		String statement = String.format("%s pdcat "
										+ "WHERE pdcat.%s NOT IN (SELECT pp.%s FROM %s pp WHERE pp.%s=?) "
				                        + "AND (pdcat.%s ILIKE ? OR pdcat.%s ILIKE ?) AND pdcat.%s=? AND pdcat.%s=?",
										pDm.domainName(), 
										pDm.keyName(), dm.categoryIdKey(), dm.domainName(), dm.pdvIdKey(), 
										pDm.nameKey(), pDm.descriptionKey(), pDm.moduleIdKey(), pDm.useCodeKey());
		
		params.add(pdv.id());
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(sales.id());
		params.add(UseCode.USER.id());
		
		HorodateMetadata hm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY pdcat.%s DESC", hm.dateCreatedKey());
		
		String keyResult = String.format("pdcat.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);		
	}
	
	private QueryBuilder buildQueryProductCategoryToSale(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		ProductCategoryMetadata pDm = ProductCategoryMetadata.create();
		PdvMetadata pdvDm = PdvMetadata.create();
		String statement = String.format("%s pdcat "
										+ "JOIN %s pp ON pp.%s = pdcat.%s "
										+ "left JOIN %s pdv ON pdv.%s = pp.%s "
										+ "WHERE (pdcat.%s ILIKE ? OR pdcat.%s ILIKE ?) AND pdcat.%s=? AND pdv.%s=? AND pdcat.%s=?",
										pDm.domainName(), 
										dm.domainName(), dm.categoryIdKey(), pDm.keyName(),
										pdvDm.domainName(), pdvDm.keyName(), dm.pdvIdKey(),
										pDm.nameKey(), pDm.descriptionKey(), pDm.moduleIdKey(), pdvDm.keyName(), pDm.useCodeKey());
		
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(sales.id());
		params.add(pdv.id());
		params.add(UseCode.USER.id());
		
		HorodateMetadata hm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY pdcat.%s DESC", hm.dateCreatedKey());
		
		String keyResult = String.format("pdcat.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	public void delete(ProductCategory item) throws IOException {
		if(isFree)
			throw new RuntimeException("Erreur fatale : tentative de retrait d'une catégorie à vendre dans les catégories libres !");
		
		if(pdv.products().of(item).count() > 0)
			throw new IllegalArgumentException("Impossible de retirer la catégorie : vous devez retirer tous les produits à vendre de cette catégorie avant de continuer cette action !");
		
		String statement = String.format("DELETE FROM %s WHERE %s=? AND %s=? ", 
									      dm.domainName(), dm.categoryIdKey(), dm.pdvIdKey());
		ds.execute(statement, Arrays.asList(item.id(), pdv.id()));		
	}

	@Override
	public void add(ProductCategory item) throws IOException {
		if(isFree)
			throw new RuntimeException("Erreur fatale : tentative d'ajout d'une catégorie à vendre dans les catégories libres !");
		
		if(contains(item))
			return;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.pdvIdKey(), pdv.id());	
		params.put(dm.categoryIdKey(), item.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
	}

	@Override
	protected ProductCategory newOne(UUID id) {
		ProductCategory category;
		try {
			category = sales.productCategories().get(id);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if(isFree)
			return category;
		else			
			return new PdvProductCategoryDb(base, category, pdv);
	}

	@Override
	public ProductCategory none() {
		return new ProductCategoryNone();
	}

	@Override
	public PdvProductCategories freeCategories() {
		return new PdvProductCategoriesDb(base, module, sales, pdv, true);
	}

	@Override
	public PdvProductCategories selectedCategories() {
		return new PdvProductCategoriesDb(base, module, sales, pdv, false);
	}
}
