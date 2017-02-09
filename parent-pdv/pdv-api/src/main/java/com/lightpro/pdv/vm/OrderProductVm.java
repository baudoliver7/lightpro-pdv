package com.lightpro.pdv.vm;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sales.domains.api.OrderProduct;
import com.sales.domains.api.OrderProductTax;

public class OrderProductVm {
	private final transient OrderProduct origin;
	
	public OrderProductVm(){
		throw new UnsupportedOperationException("#OrderProductVm()");
	}
	
	public OrderProductVm(final OrderProduct origin) {
        this.origin = origin;
    }
	
	@JsonGetter
	public UUID getId(){
		return origin.id();
	}
	
	@JsonGetter
	public int getQuantity() throws IOException {
		return origin.quantity();
	}
	
	@JsonGetter
	public double getUnitPrice() throws IOException {
		return origin.unitPrice();
	}
	
	@JsonGetter
	public double getUnitPriceApplied() throws IOException {
		return origin.unitPriceApplied();
	}
	
	@JsonGetter
	public double getReductionAmount() throws IOException {
		return origin.reductionAmount();
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
	public String getOrder() throws IOException {
		return origin.order().reference();
	}
	
	@JsonGetter
	public UUID getOrderId() throws IOException {
		return origin.order().id();
	}
	
	@JsonGetter
	public String getProduct() throws IOException {
		return origin.product().name();
	}
	
	@JsonGetter
	public UUID getProductId() throws IOException {
		return origin.product().id();
	}
	
	@JsonGetter
	public String getDescription() throws IOException {
		return origin.description();
	}
	
	@JsonGetter
	public String getTaxesDescription() throws IOException {
		String description = "";
		
		for (OrderProductTax tax : origin.taxes().all()) {
			description += String.format("%s (%d %s), ", tax.tax().shortName(), tax.tax().rate(), "%");
		}
		
		return description;
	}
	
	@JsonGetter
	public String getMesureUnitShortName() throws IOException {
		return origin.product().unit().shortName();
	}
	
	@JsonGetter
	public String getMesureUnitFullName() throws IOException {
		return origin.product().unit().fullName();
	}
}
