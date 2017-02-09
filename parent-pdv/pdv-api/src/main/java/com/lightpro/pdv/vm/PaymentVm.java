package com.lightpro.pdv.vm;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sales.domains.api.Payment;

public class PaymentVm {
	private final transient Payment origin;
	
	public PaymentVm(){
		throw new UnsupportedOperationException("#PaymentVm()");
	}
	
	public PaymentVm(final Payment origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public UUID getId(){
		return origin.id();
	}
	
	public LocalDate getPaymentDate() throws IOException{
		return origin.paymentDate();
	}
	
	public String getReference() throws IOException{
		return origin.reference();
	}
	
	@JsonGetter
	public String getObject() throws IOException {
		return origin.object();
	}
	
	@JsonGetter
	public int getModeId() throws IOException {
		return origin.mode().id();
	}
	
	@JsonGetter
	public String getMode() throws IOException {
		return origin.mode().toString();
	}	
	
	@JsonGetter
	public double getPaidAmount() throws IOException {
		return origin.paidAmount();
	}
	
	public String getInvoice() throws IOException{
		return origin.invoice().reference();
	}
	
	public UUID getInvoiceId() throws IOException{
		return origin.invoice().id();
	}
}
