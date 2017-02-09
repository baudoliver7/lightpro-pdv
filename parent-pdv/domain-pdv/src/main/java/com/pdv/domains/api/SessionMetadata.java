package com.pdv.domains.api;

import com.infrastructure.core.DomainMetadata;

public class SessionMetadata implements DomainMetadata {

	private final transient String domainName;
	private final transient String keyName;
	
	public SessionMetadata() {
		this.domainName = "pdv.sessions";
		this.keyName = "id";
	}
	
	public SessionMetadata(final String domainName, final String keyName){
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

	public String referenceKey(){
		return "reference";
	}
	
	public String cashierIdKey(){
		return "cashierid";
	}
	
	public String statusIdKey(){
		return "statusid";
	}
	
	public String openedDateKey(){
		return "openeddate";
	}
	
	public String closedDateKey(){
		return "closeddate";
	}
	
	public String pdvIdKey(){
		return "pdvid";
	}
	
	public static SessionMetadata create(){
		return new SessionMetadata();
	}
}
