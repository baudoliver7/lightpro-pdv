package com.lightpro.pdv.rs;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lightpro.pdv.cmd.OrderEdited;
import com.lightpro.pdv.cmd.OrderProductEdited;
import com.lightpro.pdv.cmd.PaymentCmd;
import com.lightpro.pdv.vm.InvoiceVm;
import com.lightpro.pdv.vm.OrderVm;
import com.lightpro.pdv.vm.PaymentVm;
import com.lightpro.pdv.vm.ResumeSalesVm;
import com.lightpro.pdv.vm.SessionVm;
import com.pdv.domains.api.Session;
import com.pdv.domains.api.PdvPurchaseOrder;
import com.pdv.domains.api.PdvPurchaseOrderStatus;
import com.pdv.domains.api.PdvPurchaseOrders;
import com.sales.domains.api.Invoice;
import com.sales.domains.api.InvoiceReceipt;
import com.sales.domains.api.Invoices;
import com.sales.domains.api.OrderProduct;
import com.sales.domains.api.Payment;
import com.sales.domains.api.PaymentConditionStatus;
import com.sales.domains.api.Product;
import com.sales.domains.api.PurchaseOrder;
import com.sales.domains.api.Seller;
import com.sales.domains.impl.RemiseNone;
import com.securities.api.Contact;
import com.securities.api.PaymentMode;
import com.securities.api.Secured;

@Path("/pdv/session")
public class SessionRs extends PdvBaseRs {

	@GET
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSingle(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Session item = pdv().sessions().get(id);

						return Response.ok(new SessionVm(item)).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/{id}/order")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAllOrders(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<OrderVm> items = pdv().sessions().get(id)
												   .orders()
												   .all()
												   .stream()
												   .map(m -> new OrderVm(m))
												   .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/{id}/order/{orderid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSingleOrder(@PathParam("id") final UUID id, @PathParam("orderid") final UUID orderId) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PurchaseOrder order = pdv().sessions().get(id)
												   .orders().get(orderId);

						return Response.ok(new OrderVm(order)).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/{id}/order/in-progress")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getOrdersInProgress(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<OrderVm> items = pdv().sessions().get(id)
												   .orders().with(PdvPurchaseOrderStatus.IN_USE).all()
												   .stream()
												   .map(m -> new OrderVm(m))
												   .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/{id}/order/done")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getOrdersDone(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<OrderVm> items = pdv().sessions().get(id)
												   .orders().with(PdvPurchaseOrderStatus.DONE).all()
												   .stream()
												   .map(m -> new OrderVm(m))
												   .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});		
	}
	
	@POST
	@Secured
	@Path("/{id}/order/{orderid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response payOrder(@PathParam("id") final UUID id, @PathParam("orderid") final UUID orderId, final PaymentCmd cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws Exception {
						
						Payment payment;
						
						Session session = pdv().sessions().get(id);
						PurchaseOrder order = session.orders().get(orderId);
						PaymentMode mode = pdv().paymentModes().build(cmd.paymentModeId());						
						
						Invoices invoices = order.invoices();
						if(invoices.count() == 0){
							payment = order.cash(cmd.paymentDate(), String.format("Paiement commande N°%s", order.reference()), cmd.montantVerse(), mode, cmd.transactionReference(), session.cashier());
						}else{
							// Effectuer le paiement sur la facture déjà générée
							Invoice invoice = invoices.all().get(0);
							
							double paidAmount = invoice.saleAmount().totalAmountTtc(); // régler toute la facture
							if(cmd.montantVerse() < paidAmount)
								throw new IllegalArgumentException("Le montant réglé est inférieur au montant à payer !");
														
							InvoiceReceipt receipt = invoice.cash(cmd.paymentDate(), String.format("Encaissement %s", invoice.title()), paidAmount, mode, cmd.transactionReference(), session.cashier());
							receipt.validate(false);
							invoice.markPaid();
							
							payment = receipt;
						}																
												
						log.info(String.format("Paiement de la commande N°%s", order.reference()));
						return Response.ok(new PaymentVm(payment)).build();
					}
				});		
	}
	
	@POST
	@Secured
	@Path("/{id}/terminate")
	@Produces({MediaType.APPLICATION_JSON})
	public Response terminate(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws Exception {
						
						Session session = pdv().sessions().get(id);
						session.terminate();
						
						log.info(String.format("Clôture de la session %s", session.reference()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@POST
	@Secured
	@Path("/{id}/order")
	@Produces({MediaType.APPLICATION_JSON})
	public Response addOrder(@PathParam("id") final UUID id, final OrderEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws Exception {
						
						Session session = pdv().sessions().get(id);
						PdvPurchaseOrders containers = session.orders();
						Contact customer = pdv().contacts().build(cmd.customerId());
						Seller seller = pdv().sellers().build(cmd.sellerId());
						PurchaseOrder item = containers.add(cmd.orderDate(), cmd.expirationDate(), PaymentConditionStatus.DIRECT_PAYMENT, cmd.cgv(), cmd.description(), cmd.notes(), customer, seller, 0);
						
						for (OrderProductEdited ope : cmd.products()) {
							OrderProduct op = item.products().build(ope.id());
							
							if(ope.deleted())
								item.products().delete(op);
							else
							{
								Product product = session.pdv().products().get(ope.productId());
								
								if(op.isNone())
									item.products().add(product.category(), product, product.name(), product.description(), ope.quantity(), ope.unitPrice(), new RemiseNone(), product.taxes().all());									
								else
									op.update(product.name(), product.description(), ope.quantity(), ope.unitPrice(), new RemiseNone(), product.taxes().all());
							}								
						}
						
						log.info(String.format("Création de la commande N° %s", item.reference()));
						return Response.ok(new OrderVm(item)).build();
					}
				});		
	}
	
	@PUT
	@Secured
	@Path("/{id}/order/{orderid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateOder(@PathParam("id") final UUID id, @PathParam("orderid") final UUID orderId, final OrderEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws Exception {
						
						Session session = pdv().sessions().get(id);
						Contact customer = pdv().contacts().build(cmd.customerId());
						Seller seller = pdv().sellers().build(cmd.sellerId());
						PurchaseOrder item = session.orders().get(orderId);
						item.update(cmd.orderDate(), cmd.expirationDate(), PaymentConditionStatus.DIRECT_PAYMENT, cmd.cgv(), cmd.description(), cmd.notes(), customer, seller, 0);
						
						for (OrderProductEdited ope : cmd.products()) {
							OrderProduct op = item.products().build(ope.id());
							
							if(ope.deleted())
								item.products().delete(op);
							else
							{
								Product product = session.pdv().products().get(ope.productId());
								
								if(op.isNone())
									item.products().add(product.category(), product, product.name(), product.description(), ope.quantity(), ope.unitPrice(), new RemiseNone(), product.taxes().all());									
								else
									op.update(product.name(), product.description(), ope.quantity(), ope.unitPrice(), new RemiseNone(), product.taxes().all());
							}								
						}
						
						log.info(String.format("Mise à jour des données de la commande N° %s", item.reference()));
						return Response.ok(new OrderVm(item)).build();
					}
				});		
	}
	
	@DELETE
	@Secured
	@Path("/{id}/order/{orderid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response delete(@PathParam("id") final UUID id, @PathParam("orderid") final UUID orderId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PdvPurchaseOrders containers = pdv().sessions().get(id).orders();
						PdvPurchaseOrder item = containers.get(orderId);
						containers.delete(item);
						
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
	
	@POST
	@Secured
	@Path("/{id}/order/{orderid}/invoice")
	@Produces({MediaType.APPLICATION_JSON})
	public Response makeInvoice(@PathParam("id") final UUID id, @PathParam("orderid") final UUID orderId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws Exception {
						
						PdvPurchaseOrder purchaseOrder = pdv().sessions().get(id).orders().get(orderId);
						Invoice invoice = purchaseOrder.generateInvoice();
						
						return Response.ok(new InvoiceVm(invoice)).build();
					}
				});		
	}	

	@GET
	@Secured
	@Path("/{id}/turnover")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getTurnover(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Session session = pdv().sessions().get(id);
						double turnover = session.turnover();

						return Response.ok(new ResumeSalesVm(session.pdv().name(), turnover)).build();
					}
				});		
	}	
}
