package com.pdv.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.pdv.domains.api.PdvPurchaseOrder;
import com.pdv.domains.api.PdvPurchaseOrderMetadata;
import com.sales.domains.api.Invoice;
import com.sales.domains.api.Invoices;
import com.sales.domains.api.ModulePdv;
import com.sales.domains.api.OrderProducts;
import com.sales.domains.api.PaymentConditionStatus;
import com.sales.domains.api.PurchaseOrder;
import com.sales.domains.api.PurchaseOrderReceipt;
import com.sales.domains.api.PurchaseOrderStatus;
import com.sales.domains.api.SaleAmount;
import com.sales.domains.api.SaleTaxes;
import com.sales.domains.api.Sales;
import com.sales.domains.api.Team;
import com.sales.domains.impl.PurchaseOrderDb;
import com.securities.api.Contact;
import com.securities.api.PaymentMode;

public final class PdvPurchaseOrderDb extends GuidKeyEntityDb<PdvPurchaseOrder, PdvPurchaseOrderMetadata> implements PdvPurchaseOrder {
	
	private transient final PurchaseOrder origin;
	
	public PdvPurchaseOrderDb(Base base, UUID id, final Sales sales) {
		super(base, id, "Commande introuvable !");
		origin = new PurchaseOrderDb(base, id, sales);
	}

	@Override
	public Contact customer() throws IOException {
		return origin.customer();
	}

	@Override
	public String description() throws IOException {
		return origin.description();
	}

	@Override
	public Sales module() throws IOException {
		return origin.module();
	}

	@Override
	public String notes() throws IOException {
		return origin.notes();
	}

	@Override
	public LocalDate orderDate() throws IOException {
		return origin.orderDate();
	}

	@Override
	public String reference() throws IOException {
		return origin.reference();
	}

	@Override
	public SaleAmount saleAmount() throws IOException {
		return origin.saleAmount();
	}

	@Override
	public SaleTaxes taxes() throws IOException {
		return origin.taxes();
	}

	@Override
	public String title() throws IOException {
		return origin.title();
	}

	@Override
	public void updateAmounts() throws IOException {
		origin.updateAmounts();
	}

	@Override
	public double amountInvoiced() throws IOException {
		return origin.amountInvoiced();
	}

	@Override
	public void cancel() throws IOException {
		origin.cancel();
	}

	@Override
	public String cgv() throws IOException {
		return origin.cgv();
	}

	@Override
	public LocalDate expirationDate() throws IOException {
		return origin.expirationDate();
	}

	@Override
	public Invoices invoices() throws IOException {
		return origin.invoices();
	}

	@Override
	public double leftAmountToInvoice() throws IOException {
		return origin.leftAmountToInvoice();
	}

	@Override
	public LocalDate livraisonDate() throws IOException {
		return origin.livraisonDate();
	}

	@Override
	public int livraisonDelayInDays() throws IOException {
		return origin.livraisonDelayInDays();
	}

	@Override
	public void markEntirelyInvoiced() throws IOException {
		origin.markEntirelyInvoiced();
	}

	@Override
	public void markSold(LocalDate date, boolean isDeliverDirectly) throws IOException {
		origin.markSold(date, isDeliverDirectly);
	}

	@Override
	public ModulePdv modulePdv() throws IOException {
		return origin.modulePdv();
	}

	@Override
	public PaymentConditionStatus paymentCondition() throws IOException {
		return origin.paymentCondition();
	}

	@Override
	public OrderProducts products() throws IOException {
		return origin.products();
	}

	@Override
	public void reOpen() throws IOException {
		origin.reOpen();
	}

	@Override
	public Contact seller() throws IOException {
		return origin.seller();
	}

	@Override
	public LocalDate soldDate() throws IOException {
		return origin.soldDate();
	}

	@Override
	public PurchaseOrderStatus status() throws IOException {
		return origin.status();
	}

	@Override
	public void update(LocalDate date, LocalDate expirationDate, PaymentConditionStatus paymentCondition, String cgv, String description, String notes, Contact customer, Contact seller, int livraisonDelayInDays) throws IOException {
		origin.update(date, expirationDate, paymentCondition, cgv, description, notes, customer, seller, livraisonDelayInDays);
	}

	@Override
	public PurchaseOrderReceipt cash(LocalDate paymentDate, String object, double receivedAmount, PaymentMode mode, String transactionReference, Contact cashier) throws IOException {
		return origin.cash(paymentDate, object, receivedAmount, mode, transactionReference, cashier);
	}

	@Override
	public PurchaseOrderReceipt cashReceipt() throws IOException {
		return origin.cashReceipt();
	}

	@Override
	public Invoice generateInvoice() throws IOException {
		markSold(orderDate(), true);
		
		Invoice invoice = invoices().addFinalInvoice(orderDate(), title(), description(), notes(), paymentCondition(), customer(), seller());
		invoice.validate();
		
		return invoice;
	}

	@Override
	public void markPaid() throws IOException {
		origin.markPaid();
	}

	@Override
	public void changeCustomer(Contact customer) throws IOException {
		origin.changeCustomer(customer); 
	}

	@Override
	public void changeSeller(Contact seller) throws IOException {
		origin.changeSeller(seller);
	}

	@Override
	public double paidAmount() throws IOException {
		return origin.paidAmount();
	}

	@Override
	public double realPaidAmount() throws IOException {
		return origin.realPaidAmount();
	}

	@Override
	public double returnAmount() throws IOException {
		return origin.returnAmount();
	}

	@Override
	public Team team() throws IOException {
		return origin.team();
	}
}
