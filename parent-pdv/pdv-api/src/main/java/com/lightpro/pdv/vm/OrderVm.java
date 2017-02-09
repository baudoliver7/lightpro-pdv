package com.lightpro.pdv.vm;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sales.domains.api.PurchaseOrder;

public class OrderVm {
	private final transient PurchaseOrder origin;
	
	public OrderVm(){
		throw new UnsupportedOperationException("#OrderVm()");
	}
	
	public OrderVm(final PurchaseOrder origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public UUID getId(){
		return origin.id();
	}
	
	@JsonGetter
	public LocalDate getOrderDate() throws IOException {
		return origin.orderDate();
	}
	
	@JsonGetter
	public LocalDate getExpirationDate() throws IOException {
		return origin.expirationDate();
	}
	
	@JsonGetter
	public String getPaymentCondition() throws IOException {
		return origin.paymentCondition().toString();
	}
	
	@JsonGetter
	public int getPaymentConditionId() throws IOException {
		return origin.paymentCondition().id();
	}
	
	@JsonGetter
	public String getReference() throws IOException {
		return origin.reference();
	}
	
	@JsonGetter
	public double getTotalAmountHt() throws IOException {
		return origin.totalAmountHt();
	}
	
	@JsonGetter
	public double getTotalTaxAmount() throws IOException {
		return origin.totalTaxAmount();
	}
	
	@JsonGetter
	public double getTotalAmountTtc() throws IOException {
		return origin.totalAmountTtc();
	}
	
	@JsonGetter
	public String cgv() throws IOException {
		return origin.cgv();
	}
	
	@JsonGetter
	public String notes() throws IOException {
		return origin.notes();
	}
	
	@JsonGetter
	public UUID getCustomerId() throws IOException {
		return origin.customer().id();
	}
	
	@JsonGetter
	public String getCustomer() throws IOException {
		return origin.customer().fullName();
	}
	
	@JsonGetter
	public UUID getSellerId() throws IOException {
		return origin.seller().id();
	}
	
	@JsonGetter
	public String getSeller() throws IOException {
		return origin.seller().fullName();
	}
	
	@JsonGetter
	public int getNumberOfProducts() throws IOException {
		return origin.products().all().size();
	}
	
	@JsonGetter
	public int getNumberOfInvoices() throws IOException {
		return origin.invoices().all().size();
	}
	
	@JsonGetter
	public String getStatus() throws IOException {
		return origin.status().toString();
	}
	
	@JsonGetter
	public int getStatusId() throws IOException {
		return origin.status().id();
	}
	
	@JsonGetter
	public List<OrderProductVm> getProducts() throws IOException {
		return origin.products().all()
				     .stream()
				     .map(m -> new OrderProductVm(m))
				     .collect(Collectors.toList());
	}
}
