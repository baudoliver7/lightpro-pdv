package com.pdv.domains.impl;

import java.io.IOException;
import java.util.Arrays;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvProductCategoryMetadata;
import com.sales.domains.api.ProductCategory;
import com.sales.domains.api.ProductCategoryType;
import com.sales.domains.api.Products;
import com.sales.domains.api.Sales;

public final class PdvProductCategoryDb extends GuidKeyEntityDb<ProductCategory, PdvProductCategoryMetadata> implements ProductCategory {

	private transient final ProductCategory origin;
	
	public PdvProductCategoryDb(final Base base, final ProductCategory origin, final Pdv pdv) {
		super(base, origin.id(), "Catégorie de produit introuvable !", keyStatement(), Arrays.asList(pdv.id(), origin.id()));
		this.origin = origin;
	}

	private static String keyStatement() {
		return String.format("SELECT id FROM pdv.pdv_product_categories WHERE pdvid=? AND categoryid=?");
	}
	
	@Override
	public String description() throws IOException {
		return origin.description();
	}

	@Override
	public Sales module() throws IOException {
		return origin.module();
	}

	@Override
	public String name() throws IOException {
		return origin.name();
	}

	@Override
	public Products products() throws IOException {
		return origin.products();
	}

	@Override
	public ProductCategoryType type() throws IOException {
		return origin.type();
	}

	@Override
	public void update(String name, String description) throws IOException {
		origin.update(name, description);
	}
}
