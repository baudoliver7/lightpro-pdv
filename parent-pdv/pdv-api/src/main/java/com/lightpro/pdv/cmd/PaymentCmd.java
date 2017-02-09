package com.lightpro.pdv.cmd;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import com.common.utilities.convert.TimeConvert;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sales.domains.api.PaymentMode;

public class PaymentCmd {
	
	private final LocalDate paymentDate;
	private final int paymentModeId;
	private final double montantVerse;
	private final UUID orderId;
	
	public PaymentCmd(){
		throw new UnsupportedOperationException("#PaymentCmd()");
	}
	
	@JsonCreator
	public PaymentCmd(@JsonProperty("paymentDate") final Date paymentDate,
					  @JsonProperty("paymentModeId") final int paymentModeId, 
					  @JsonProperty("montantVerse") final double montantVerse, 
					  @JsonProperty("orderId") final UUID orderId){
		
		this.paymentDate = TimeConvert.toLocalDate(paymentDate, ZoneId.systemDefault());
		this.paymentModeId = paymentModeId;
		this.montantVerse = montantVerse;
		this.orderId = orderId;		
	}
	
	public PaymentMode paymentMode(){
		return PaymentMode.get(paymentModeId);
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
}
