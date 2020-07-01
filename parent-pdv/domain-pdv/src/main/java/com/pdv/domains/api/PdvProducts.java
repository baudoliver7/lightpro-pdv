package com.pdv.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.sales.domains.api.Product;
import com.sales.domains.api.ProductCategory;

public interface PdvProducts extends AdvancedQueryable<Product, UUID> {
	void add(Product item) throws IOException;
	PdvProducts of(ProductCategory category) throws IOException;
	
	PdvProducts freeProducts();
	PdvProducts selectedProducts();
}
