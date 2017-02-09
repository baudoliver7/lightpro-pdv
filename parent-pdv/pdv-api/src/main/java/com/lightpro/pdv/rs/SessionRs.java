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
import com.lightpro.pdv.vm.OrderVm;
import com.lightpro.pdv.vm.PaymentVm;
import com.lightpro.pdv.vm.SessionVm;
import com.pdv.domains.api.Session;
import com.pdv.domains.api.SessionPurchaseOrders;
import com.sales.domains.api.Customer;
import com.sales.domains.api.OrderProduct;
import com.sales.domains.api.Payment;
import com.sales.domains.api.Product;
import com.sales.domains.api.PurchaseOrder;
import com.sales.domains.api.PurchaseOrders;
import com.securities.api.Secured;
import com.securities.api.User;

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
	@Path("/{id}/order/in-progress")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getOrdersInProgress(@PathParam("id") final UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<OrderVm> items = pdv().sessions().get(id)
												   .orders().inProgress()
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
												   .orders().done()
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
						
						Session session = pdv().sessions().get(id);
						SessionPurchaseOrders orders = session.orders();
						Payment payment = orders.pay(cmd.paymentDate(), cmd.orderId(), cmd.paymentMode(), cmd.montantVerse());											
						
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
						PurchaseOrders containers = session.orders();
						Customer customer = pdv().customers().build(cmd.customerId());
						User seller = pdv().membership().get(cmd.sellerId());
						PurchaseOrder item = containers.add(cmd.orderDate(), cmd.expirationDate(), cmd.paymentCondition(), cmd.cgv(), cmd.notes(), customer, seller);
						
						for (OrderProductEdited ope : cmd.products()) {
							OrderProduct op = item.products().build(ope.id());
							
							if(ope.deleted())
								item.products().delete(op);
							else
							{
								Product product = session.pdv().productsToSale().get(ope.productId());
								
								if(op.isPresent())
									op.update(ope.quantity(), 0, ope.reductionAmount(), null, product);
								else
									item.products().add(ope.quantity(), 0, ope.reductionAmount(), null, product, true);
							}								
						}
						
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
						Customer customer = pdv().customers().build(cmd.customerId());
						User seller = pdv().membership().get(cmd.sellerId());
						PurchaseOrder item = session.orders().get(orderId);
						item.update(cmd.orderDate(), cmd.expirationDate(), cmd.paymentCondition(), cmd.cgv(), cmd.notes(), customer, seller);
						
						for (OrderProductEdited ope : cmd.products()) {
							OrderProduct op = item.products().build(ope.id());
							
							if(ope.deleted())
								item.products().delete(op);
							else
							{
								Product product = session.pdv().productsToSale().get(ope.productId());
								
								if(op.isPresent())
									op.update(ope.quantity(), 0, ope.reductionAmount(), null, product);
								else
									item.products().add(ope.quantity(), 0, ope.reductionAmount(), null, product, true);
							}								
						}
						
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
						
						PurchaseOrders containers = pdv().sessions().get(id).orders();
						PurchaseOrder item = containers.get(orderId);
						containers.delete(item);
						
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
