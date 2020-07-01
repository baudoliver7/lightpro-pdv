package com.pdv.domains.api;

import com.infrastructure.core.DomainMetadata;

public class PdvPurchaseOrderMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public PdvPurchaseOrderMetadata() {
		this.domainName = "pdv.purchaseorders";
		this.keyName = "id";
	}
	
	public PdvPurchaseOrderMetadata(final String domainName, final String keyName){
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

	public String sessionIdKey(){
		return "sessionid";
	}
	
	public static PdvPurchaseOrderMetadata create(){
		return new PdvPurchaseOrderMetadata();
	}
}
