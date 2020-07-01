package com.lightpro.pdv.cmd;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import com.common.utilities.convert.TimeConvert;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentCmd {
	
	private final LocalDate paymentDate;
	private final UUID paymentModeId;
	private final String transactionReference;
	private final double montantVerse;
	private final UUID orderId;
	
	public PaymentCmd(){
		throw new UnsupportedOperationException("#PaymentCmd()");
	}
	
	@JsonCreator
	public PaymentCmd(@JsonProperty("paymentDate") final Date paymentDate,
					  @JsonProperty("paymentModeId") final UUID paymentModeId,
					  @JsonProperty("transactionReference") final String transactionReference, 
					  @JsonProperty("montantVerse") final double montantVerse, 
					  @JsonProperty("orderId") final UUID orderId){
		
		this.paymentDate = TimeConvert.toLocalDate(paymentDate, ZoneId.systemDefault());
		this.paymentModeId = paymentModeId;
		this.montantVerse = montantVerse;
		this.orderId = orderId;		
		this.transactionReference = transactionReference;
	}
	
	public UUID paymentModeId(){
		return paymentModeId;
	}
	
	public double montantVerse(){
		return montantVerse;
	}
	
	public UUID orderId(){
		return orderId;
	}
	
	public LocalDate paymentDate(){
		return paymentDate;
	}
	
	public String transactionReference(){
		return transactionReference;
	}
}
