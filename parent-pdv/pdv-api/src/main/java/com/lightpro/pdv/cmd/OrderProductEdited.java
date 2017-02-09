package com.lightpro.pdv.cmd;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderProductEdited {
	
	private final UUID id;
	private final int quantity;
	private final double unitPrice;
	private final double reductionAmount;
	private final UUID productId;
	private final boolean deleted;
	
	public OrderProductEdited(){
		throw new UnsupportedOperationException("#OrderProductEdited()");
	}
	
	@JsonCreator
	public OrderProductEdited(@JsonProperty("id") final UUID id,
							  @JsonProperty("quantity") final int quantity, 
					    	  @JsonProperty("unitPrice") final double unitPrice,
					    	  @JsonProperty("reductionAmount") final double reductionAmount,
					    	  @JsonProperty("productId") final UUID productId,
					    	  @JsonProperty("deleted") final boolean deleted){
		
		this.id = id;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.reductionAmount = reductionAmount;
		this.productId = productId;
		this.deleted = deleted;
	}
	
	public UUID id(){
		return id;
	}
	
	public int quantity(){
		return quantity;
	}
	
	public double unitPrice(){
		return unitPrice;
	}
	
	public double reductionAmount(){
		return reductionAmount;
	}
	
	public UUID productId(){
		return productId;
	}
	
	public boolean deleted(){
		return deleted;
	}
}
