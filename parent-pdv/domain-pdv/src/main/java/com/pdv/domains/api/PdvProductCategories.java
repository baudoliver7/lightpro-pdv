package com.pdv.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;
import com.sales.domains.api.ProductCategory;

public interface PdvProductCategories extends AdvancedQueryable<ProductCategory, UUID>, Updatable<ProductCategory> {
	void add(ProductCategory item) throws IOException;
	PdvProductCategories freeCategories();
	PdvProductCategories selectedCategories();
}
