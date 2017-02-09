package com.pdv.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;
import com.sales.domains.api.Product;

public interface PdvProducts extends AdvancedQueryable<Product, UUID>, Updatable<Product> {
	void add(Product item) throws IOException;
}
