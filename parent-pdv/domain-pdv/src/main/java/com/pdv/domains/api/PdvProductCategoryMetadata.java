package com.pdv.domains.api;

import com.infrastructure.core.DomainMetadata;

public class PdvProductCategoryMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public PdvProductCategoryMetadata() {
		this.domainName = "pdv.pdv_product_categories";
		this.keyName = "id";
	}
	
	public PdvProductCategoryMetadata(final String domainName, final String keyName){
		this.domainName = domainName;
		this.keyName = keyName;
	}
	
	@Override
	public String domainName() {
		return this.domainName;
	}

	@Override
	public String keyName() {
		return this.keyName;
	}

	public String categoryIdKey(){
		return "categoryid";
	}
	
	public String pdvIdKey(){
		return "pdvid";
	}
	
	public static PdvProductCategoryMetadata create(){
		return new PdvProductCategoryMetadata();
	}
}
