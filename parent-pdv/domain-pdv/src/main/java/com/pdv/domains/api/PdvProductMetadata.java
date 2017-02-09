package com.pdv.domains.api;

import com.infrastructure.core.DomainMetadata;

public class PdvProductMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public PdvProductMetadata() {
		this.domainName = "pdv.pdv_products";
		this.keyName = "id";
	}
	
	public PdvProductMetadata(final String domainName, final String keyName){
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

	public String productIdKey(){
		return "productid";
	}
	
	public String pdvIdKey(){
		return "pdvid";
	}
	
	public static PdvProductMetadata create(){
		return new PdvProductMetadata();
	}
}
