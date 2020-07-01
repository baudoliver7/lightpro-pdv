package com.pdv.domains.api;

import java.io.IOException;

import com.sales.domains.api.Invoice;
import com.sales.domains.api.PurchaseOrder;

public interface PdvPurchaseOrder extends PurchaseOrder {
	Invoice generateInvoice() throws IOException;
}
