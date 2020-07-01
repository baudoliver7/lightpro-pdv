package com.lightpro.pdv.vm;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sales.domains.api.Invoice;

public final class InvoiceVm {
	
	public final UUID id;
	public final LocalDate orderDate;
	public final LocalDate dueDate;
	public final String paymentCondition;
	public final String origin;
	public final int paymentConditionId;
	public final String reference;
	public final double totalAmountHt;
	public final double totalTaxAmount;
	public final double totalAmountTtc;
	public final double leftAmountToPay;
	public final double totalAmountPaid;
	public final String description;
	public final String notes;
	public final UUID customerId;
	public final String customer;
	public final UUID sellerId;
	public final String seller;
	public final UUID modulePdvId;
	public final String modulePdv;
	public final int numberOfProducts;
	public final String status;
	public final int statusId;
	public final String step;
	public final int stepId;
	public final String type;
	public final int typeId;
	public final String nature;
	public final int natureId;
	public final UUID purchaseOrderId;
	public final String purchaseOrder;
	public final double netCommercial;
	public final double reduceAmount;
	public final double avoirAmount;
	public final double totalAmountRembourse;
	public final double solde;
	public final String title;
	public final List<SaleTaxVm> taxes;
	
	public InvoiceVm(){
		throw new UnsupportedOperationException("#InvoiceVm()");
	}
	
	public InvoiceVm(final Invoice origin) {
		try {
			this.id = origin.id();
			this.orderDate = origin.orderDate();
			this.dueDate = origin.dueDate();
			this.paymentCondition = origin.paymentCondition().toString();
			this.origin = origin.origin();
			this.paymentConditionId = origin.paymentCondition().id();
			this.reference = origin.reference();
			this.totalAmountHt = origin.saleAmount().totalAmountHt();
			this.totalTaxAmount = origin.saleAmount().totalTaxAmount();
			this.totalAmountTtc = origin.saleAmount().totalAmountTtc();
			this.leftAmountToPay = origin.leftAmountToPay();
			this.totalAmountPaid = origin.totalAmountPaid();
			this.description = origin.description();
			this.notes = origin.notes();
			this.customerId = origin.customer().id();
			this.customer = origin.customer().name();
			this.sellerId = origin.seller().id();
			this.seller = origin.seller().name();
			this.modulePdvId = origin.modulePdv().id();
			this.modulePdv = origin.modulePdv().name();
			this.numberOfProducts = origin.products().all().size();
			this.status = origin.status().toString();
			this.statusId = origin.status().id();
			this.step = origin.step().toString();
			this.stepId = origin.step().id();
			this.type = origin.type().toString();
			this.typeId = origin.type().id();
			this.nature = origin.nature().toString();
			this.natureId = origin.nature().id();
			this.purchaseOrderId = origin.purchaseOrder().id();
			this.purchaseOrder = origin.purchaseOrder().reference();
			this.netCommercial = origin.saleAmount().netCommercial();
			this.reduceAmount = origin.saleAmount().reduceAmount();
			this.avoirAmount = origin.avoirAmount();
			this.totalAmountRembourse = origin.totalAmountRembourse();
			this.solde = origin.solde();
			this.title = origin.title();
			this.taxes = origin.taxes().all().stream().map(m -> new SaleTaxVm(m)).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
    }
}
