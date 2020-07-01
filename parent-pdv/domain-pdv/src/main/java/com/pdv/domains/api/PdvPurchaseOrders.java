package com.pdv.domains.api;

import java.io.IOException;
import java.time.LocalDate;

import com.infrastructure.core.GuidKeyAdvancedQueryable;
import com.sales.domains.api.PaymentConditionStatus;
import com.sales.domains.api.Seller;
import com.securities.api.Contact;

public interface PdvPurchaseOrders extends GuidKeyAdvancedQueryable<PdvPurchaseOrder> {
	PdvPurchaseOrders of(Pdv pdv) throws IOException;
	PdvPurchaseOrders of(Session session) throws IOException;
	PdvPurchaseOrders with(PdvPurchaseOrderStatus status) throws IOException;
	
	PdvPurchaseOrder add(LocalDate date, LocalDate expirationDate, PaymentConditionStatus paymentCondition, String cgv, String description, String notes, Contact customer, Seller seller, int livraisonDelayInDays) throws IOException;
}
