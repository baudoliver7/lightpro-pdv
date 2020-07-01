package com.lightpro.pdv.vm;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sales.domains.api.PurchaseOrder;

public final class OrderVm {
	
	public final UUID id;
	public final LocalDate orderDate;
	public final LocalDate expirationDate;
	public final String paymentCondition;
	public final int paymentConditionId;
	public final String reference;
	public final double totalAmountHt;
	public final double totalTaxAmount;
	public final double totalAmountTtc;
	public final String cgv;
	public final String notes;
	public final UUID customerId;
	public final String customer;
	public final UUID sellerId;
	public final String seller;
	public final long numberOfProducts;
	public final long numberOfInvoices;
	public final String status;
	public final int statusId;
	public final List<OrderProductVm> products;
	
	public OrderVm(){
		throw new UnsupportedOperationException("#OrderVm()");
	}
	
	public OrderVm(final PurchaseOrder origin) {
		try {
			this.id = origin.id();
			this.orderDate = origin.orderDate();
			this.expirationDate = origin.expirationDate();
			this.paymentCondition = origin.paymentCondition().toString();
			this.paymentConditionId = origin.paymentCondition().id();
			this.reference = origin.reference();
			this.cgv = origin.cgv();
			this.notes = origin.notes();
			this.customerId = origin.customer().id();
			this.customer = origin.customer().name();
			this.sellerId = origin.seller().id();
			this.seller = origin.seller().name();
			this.numberOfProducts = origin.products().count();
			this.numberOfInvoices = origin.invoices().count();
			this.status = origin.status().toString();
			this.statusId = origin.status().id();					
			this.totalAmountHt = origin.saleAmount().totalAmountHt();
			this.totalTaxAmount = origin.saleAmount().totalTaxAmount();
			this.totalAmountTtc = origin.saleAmount().totalAmountTtc();		
			this.products = origin.products().all().stream().map(m -> new OrderProductVm(m)).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
    }
}
