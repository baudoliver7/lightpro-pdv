package com.pdv.domains.api;

import com.infrastructure.core.DomainMetadata;

public class SessionPurchaseOrderMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public SessionPurchaseOrderMetadata() {
		this.domainName = "pdv.session_purchases";
		this.keyName = "id";
	}
	
	public SessionPurchaseOrderMetadata(final String domainName, final String keyName){
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
	
	public static SessionPurchaseOrderMetadata create(){
		return new SessionPurchaseOrderMetadata();
	}
}
