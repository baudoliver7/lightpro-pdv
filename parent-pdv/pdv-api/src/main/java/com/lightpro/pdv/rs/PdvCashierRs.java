package com.lightpro.pdv.rs;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.infrastructure.core.PaginationSet;
import com.lightpro.pdv.cmd.CashierEdited;
import com.lightpro.pdv.vm.CashierVm;
import com.pdv.domains.api.Cashier;
import com.pdv.domains.api.Cashiers;
import com.sales.domains.api.Seller;
import com.securities.api.Secured;

@Path("/pdv/pdv/{pdvid}/cashier")
public class PdvCashierRs extends PdvBaseRs {

	private transient UUID pdvId;
	
	@PathParam("pdvid")
	void setPdv(UUID id){
		pdvId = id;
	}
	
	private Cashiers cashiers() throws IOException {
		return pdv().pdvs().get(pdvId).cashiers();
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
						
						List<CashierVm> itemsVm = container.find(page, pageSize, filter)
														    .stream()
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
	
	@POST
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response add(final CashierEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Cashiers cashiers = cashiers();
						Seller seller = pdv().sellers().build(cmd.id());
						
						Cashier cashier = cashiers.add(seller);	
						
						log.info(String.format("Ajout du caissier %s au point de vente %s", seller.name(), pdv().pdvs().get(pdvId).name()));
						return Response.ok(new CashierVm(cashier)).build();
					}
				});		
	}
	
	@DELETE
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response delete(@PathParam("id") final UUID id) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Cashier item = cashiers().get(id);
						String pdvName = item.pdv().name();
						cashiers().delete(item);
						
						log.info(String.format("Retrait du caissier %s du point de vente %s", item.name(), pdvName));
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
