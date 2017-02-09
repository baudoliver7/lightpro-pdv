package com.lightpro.pdv.cmd;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.common.utilities.convert.TimeConvert;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sales.domains.api.PaymentConditionStatus;

public class OrderEdited {
	
	private final LocalDate orderDate;
	private final LocalDate expirationDate;
	private final PaymentConditionStatus paymentCondition;
	private final String cgv;
	private final String notes;
	private final UUID customerId;
	private final UUID sellerId;
	private final List<OrderProductEdited> products;
	
	public OrderEdited(){
		throw new UnsupportedOperationException("#QuotationEdited()");
	}
	
	@JsonCreator
	public OrderEdited( @JsonProperty("orderDate") final Date orderDate, 
						  @JsonProperty("expirationDate") final Date expirationDate, 
						  @JsonProperty("paymentConditionId") final int paymentConditionId,
				    	  @JsonProperty("cgv") final String cgv,
				    	  @JsonProperty("notes") final String notes,
				    	  @JsonProperty("sellerId") final UUID sellerId,
				    	  @JsonProperty("customerId") final UUID customerId,
				    	  @JsonProperty("products") final List<OrderProductEdited> products){
		
		this.orderDate = TimeConvert.toLocalDate(orderDate, ZoneId.systemDefault());
		this.expirationDate = TimeConvert.toLocalDate(expirationDate, ZoneId.systemDefault());
		this.cgv = cgv;
		this.notes = notes;
		this.sellerId = sellerId;
		this.customerId = customerId;
		this.products = products;
		this.paymentCondition = PaymentConditionStatus.get(paymentConditionId);				
	}
	
	public LocalDate orderDate(){
		return orderDate;
	}
	
	public LocalDate expirationDate(){
		return expirationDate;
	}
	
	public PaymentConditionStatus paymentCondition(){
		return paymentCondition;
	}
	
	public String cgv(){
		return cgv;
	}
	
	public String notes(){
		return notes;
	}
	
	public UUID sellerId(){
		return sellerId;
	}
	
	public UUID customerId(){
		return customerId;
	}
	
	public List<OrderProductEdited> products(){
		return products;
	}
}
