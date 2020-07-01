package com.pdv.domains.api;

public enum SessionStatus {
	
	NONE(0, "Non défini"),
	IN_USE(1, "En cours"),
	CLOSED(2, "Clôturée");
	
	private final int id;
	private final String name;
	
	SessionStatus(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public static SessionStatus get(int id){
		
		SessionStatus value = SessionStatus.NONE;
		for (SessionStatus item : SessionStatus.values()) {
			if(item.id() == id)
				value = item;
		}
		
		return value;
	}

	public int id(){
		return id;
	}
	
	public String toString(){
		return name;
	}
}
