package com.pdv.domains.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.sales.domains.api.Payment;
import com.sales.domains.api.PaymentMode;
import com.sales.domains.api.PurchaseOrder;
import com.sales.domains.api.PurchaseOrders;

public interface SessionPurchaseOrders extends PurchaseOrders {
	Payment pay(LocalDate paymentDate, UUID orderId, PaymentMode mode, double montantVerse) throws IOException;
	List<PurchaseOrder> inProgress() throws IOException;
	List<PurchaseOrder> done() throws IOException;
}
