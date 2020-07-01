package com.pdv.domains.impl;

import java.io.IOException;
import java.time.LocalDate;

import com.infrastructure.core.GuidKeyEntityNone;
import com.pdv.domains.api.PdvPurchaseOrder;
import com.sales.domains.api.Invoice;
import com.sales.domains.api.Invoices;
import com.sales.domains.api.ModulePdv;
import com.sales.domains.api.OrderProducts;
import com.sales.domains.api.PaymentConditionStatus;
import com.sales.domains.api.PurchaseOrderReceipt;
import com.sales.domains.api.PurchaseOrderStatus;
import com.sales.domains.api.SaleAmount;
import com.sales.domains.api.SaleTaxes;
import com.sales.domains.api.Sales;
import com.sales.domains.api.Seller;
import com.sales.domains.api.Team;
import com.sales.domains.impl.InvoiceNone;
import com.sales.domains.impl.ModulePdvNone;
import com.sales.domains.impl.SalesNone;
import com.sales.domains.impl.SellerNone;
import com.sales.domains.impl.TeamNone;
import com.securities.api.Contact;
import com.securities.api.PaymentMode;
import com.securities.impl.ContactNone;

public final class PdvPurchaseOrderNone extends GuidKeyEntityNone<PdvPurchaseOrder> implements PdvPurchaseOrder {

	@Override
	public double amountInvoiced() throws IOException {
		return 0;
	}

	@Override
	public void cancel() throws IOException {

	}

	@Override
	public PurchaseOrderReceipt cash(LocalDate arg0, String arg1, double arg2, PaymentMode arg3, String arg4, Contact arg5)
			throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public PurchaseOrderReceipt cashReceipt() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public String cgv() throws IOException {
		return null;
	}

	@Override
	public LocalDate expirationDate() throws IOException {
		return null;
	}

	@Override
	public Invoices invoices() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public double leftAmountToInvoice() throws IOException {
		return 0;
	}

	@Override
	public LocalDate livraisonDate() throws IOException {
		return null;
	}

	@Override
	public int livraisonDelayInDays() throws IOException {
		return 0;
	}

	@Override
	public void markEntirelyInvoiced() throws IOException {

	}

	@Override
	public void markSold(LocalDate solded, boolean isDeliverDirectly) throws IOException {

	}

	@Override
	public ModulePdv modulePdv() throws IOException {
		return new ModulePdvNone();
	}

	@Override
	public PaymentConditionStatus paymentCondition() throws IOException {
		return PaymentConditionStatus.NONE;
	}

	@Override
	public OrderProducts products() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public void reOpen() throws IOException {

	}

	@Override
	public Seller seller() throws IOException {
		return new SellerNone();
	}

	@Override
	public LocalDate soldDate() throws IOException {		
		return null;
	}

	@Override
	public PurchaseOrderStatus status() throws IOException {
		return PurchaseOrderStatus.NONE;
	}

	@Override
	public void update(LocalDate arg0, LocalDate arg1, PaymentConditionStatus arg2, String arg3, String arg4,
			String arg5, Contact arg6, Contact arg7, int arg8) throws IOException {

	}

	@Override
	public Contact customer() throws IOException {
		return new ContactNone();
	}

	@Override
	public String description() throws IOException {
		return null;
	}
	
	@Override
	public Sales module() throws IOException {
		return new SalesNone();
	}

	@Override
	public String notes() throws IOException {
		return null;
	}

	@Override
	public LocalDate orderDate() throws IOException {
		return null;
	}

	@Override
	public String reference() throws IOException {
		return null;
	}

	@Override
	public SaleAmount saleAmount() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public SaleTaxes taxes() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public String title() throws IOException {
		return null;
	}

	@Override
	public void updateAmounts() throws IOException {

	}

	@Override
	public Invoice generateInvoice() throws IOException {
		return new InvoiceNone();
	}

	@Override
	public void markPaid() throws IOException {
		
	}

	@Override
	public void changeCustomer(Contact arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeSeller(Contact arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double paidAmount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double realPaidAmount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double returnAmount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Team team() throws IOException {
		return new TeamNone();
	}
}
