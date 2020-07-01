package com.pdv.domains.api;

import com.infrastructure.core.DomainMetadata;

public class CashierMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public CashierMetadata() {
		this.domainName = "pdv.cashiers";
		this.keyName = "id";
	}
	
	public CashierMetadata(final String domainName, final String keyName){
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
	
	public String pdvIdKey(){
		return "pdvid";
	}
	
	public static CashierMetadata create(){
		return new CashierMetadata();
	}

}
