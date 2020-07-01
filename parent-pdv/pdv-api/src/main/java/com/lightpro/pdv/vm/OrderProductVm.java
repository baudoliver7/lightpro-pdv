package com.lightpro.pdv.vm;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sales.domains.api.OrderProduct;

public final class OrderProductVm {
	
	public final UUID id;
	public final double quantity;
	public final double unitPrice;
	public final double reduceAmount;
	public final double totalAmountHt;
	public final double totalTaxAmount;
	public final double totalAmountTtc;
	public final String order;
	public final UUID orderId;
	public final String name;
	public final String product;
	public final UUID productId;
	public final String category;
	public final UUID categoryId;
	public final String pricingMode;
	public final int pricingModeId;
	public final String description;
	public final String taxesDescription;
	public final double netCommercial;
	public final List<SaleTaxVm> taxes;
	
	public OrderProductVm(){
		throw new UnsupportedOperationException("#OrderProductVm()");
	}
	
	public OrderProductVm(final OrderProduct origin) {
		try {
			this.id = origin.id();
			this.quantity = origin.quantity();
			this.unitPrice = origin.unitPrice();
	        this.reduceAmount = origin.saleAmount().reduceAmount();
	        this.totalAmountHt = origin.saleAmount().totalAmountHt();
	        this.totalTaxAmount = origin.saleAmount().totalTaxAmount();
	        this.totalAmountTtc = origin.saleAmount().totalAmountTtc();
	        this.order = origin.order().reference();
			this.orderId = origin.order().id();
	        this.name = origin.name();
	        this.product = origin.product().name();
	        this.productId = origin.product().id();
	        this.category = origin.category().name();
	        this.categoryId = origin.category().id();
	        this.pricingMode = origin.product().pricing().mode().toString();
	        this.pricingModeId = origin.product().pricing().mode().id();
	        this.description = origin.description();
	        this.taxesDescription = origin.taxes().toString();
	        this.netCommercial = origin.saleAmount().netCommercial();
	        this.taxes = origin.taxes().all().stream().map(m -> new SaleTaxVm(m)).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
}
