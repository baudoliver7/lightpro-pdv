package com.lightpro.pdv.rs;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.infrastructure.core.PaginationSet;
import com.lightpro.pdv.vm.CashierVm;
import com.pdv.domains.api.Cashier;
import com.pdv.domains.api.Cashiers;
import com.pdv.domains.api.Pdv;
import com.securities.api.Secured;

@Path("/pdv/cashier")
public class CashierRs extends PdvBaseRs {
	
	private Cashiers cashiers() throws IOException {
		return pdv().cashiers();
	}
	
	@GET
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAll() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<CashierVm> items = cashiers().all()
													 .stream()
											 		 .map(m -> new CashierVm(m))
											 		 .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});			
	}
	
	@GET
	@Secured
	@Path("/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response search( @QueryParam("page") int page, 
							@QueryParam("pageSize") int pageSize, 
							@QueryParam("filter") String filter) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Cashiers container = cashiers();
						
						List<CashierVm> itemsVm = container.find(page, pageSize, filter).stream()
															 .map(m -> new CashierVm(m))
															 .collect(Collectors.toList());
													
						long count = container.count(filter);
						PaginationSet<CashierVm> pagedSet = new PaginationSet<CashierVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});	
				
	}
	
	@GET
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSingle(@PathParam("id") UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						CashierVm item = new CashierVm(cashiers().get(id));

						return Response.ok(item).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/{id}/is-seller")
	@Produces({MediaType.APPLICATION_JSON})
	public Response isSeller(@PathParam("id") UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {

						Cashier item = cashiers().build(id);
						return Response.ok(cashiers().contains(item)).build();
					}
				});		
	}
	
	@POST
	@Path("/{id}/change-pdv/{newpdvid}")
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response add(@PathParam("id") final UUID id, @PathParam("newpdvid") final UUID newPdvId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Cashier item = cashiers().get(id);
						String oldPdv = item.pdv().name();
						Pdv pdv = pdv().pdvs().get(newPdvId);
								
						item.changePdv(pdv);
						
						log.info(String.format("Transfert du caissier %s du point de vente %s au point de vente %S", item.name(), oldPdv, pdv.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
}
