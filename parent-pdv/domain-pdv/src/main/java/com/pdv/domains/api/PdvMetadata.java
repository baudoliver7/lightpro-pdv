package com.pdv.domains.api;

import com.infrastructure.core.DomainMetadata;

public class PdvMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public PdvMetadata() {
		this.domainName = "pdv.pdvs";
		this.keyName = "id";
	}
	
	public PdvMetadata(final String domainName, final String keyName){
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

	public String nameKey(){
		return "name";
	}
	
	public String activeKey(){
		return "active";
	}
	
	public String moduleIdKey(){
		return "moduleid";
	}
	
	public static PdvMetadata create(){
		return new PdvMetadata();
	}
}
