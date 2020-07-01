package com.pdv.domains.api;

public enum PdvPurchaseOrderStatus {
	
	NONE(0, "Non défini"),
	IN_USE(1, "En cours"),
	DONE(2, "Terminée");
	
	private final int id;
	private final String name;
	
	PdvPurchaseOrderStatus(final int id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public static PdvPurchaseOrderStatus get(int id){
		
		PdvPurchaseOrderStatus value = PdvPurchaseOrderStatus.NONE;
		for (PdvPurchaseOrderStatus item : PdvPurchaseOrderStatus.values()) {
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
