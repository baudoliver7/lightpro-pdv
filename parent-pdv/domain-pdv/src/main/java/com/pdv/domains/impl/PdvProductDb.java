package com.pdv.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvProductMetadata;
import com.sales.domains.api.OrderProduct;
import com.sales.domains.api.Pricing;
import com.sales.domains.api.Product;
import com.sales.domains.api.ProductCategory;
import com.sales.domains.api.ProductTaxes;
import com.sales.domains.api.Sales;
import com.securities.api.MesureUnit;
import com.securities.api.Tax;

public final class PdvProductDb extends GuidKeyEntityDb<Product, PdvProductMetadata> implements Product {

	private final transient Product origin;
	
	public PdvProductDb(final Base base, final Product origin, final Pdv pdv) {
		super(base, origin.id(), "Produit introuvable !", keyStatement(), Arrays.asList(pdv.id(), origin.id()));
		this.origin = origin;
	}
	
	private static String keyStatement() {
		return String.format("SELECT id FROM pdv.pdv_products WHERE pdvid=? AND productid=?");
	}

	@Override
	public String barCode() throws IOException {
		return origin.barCode();
	}

	@Override
	public ProductCategory category() throws IOException {
		return origin.category();
	}

	@Override
	public String description() throws IOException {
		return origin.description();
	}

	@Override
	public String emballage() throws IOException {
		return origin.emballage();
	}

	@Override
	public OrderProduct generate(double quantity, LocalDate orderDate, double unitPrice, List<Tax> taxes) throws IOException {
		return origin.generate(quantity, orderDate, unitPrice, taxes);
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
	public Pricing pricing() throws IOException {
		return origin.pricing();
	}

	@Override
	public double quantity() throws IOException {
		return origin.quantity();
	}

	@Override
	public ProductTaxes taxes() throws IOException {
		return origin.taxes();
	}

	@Override
	public MesureUnit unit() throws IOException {
		return origin.unit();
	}

	@Override
	public void update(String name, String internalReference, String barCode, ProductCategory category, String description, MesureUnit unit, String emballage, double quantity) throws IOException {
		origin.update(name, internalReference, barCode, category, description, unit, emballage, quantity);
	}

	@Override
	public String internalReference() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
