package com.lightpro.pdv.rs;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.infrastructure.core.PaginationSet;
import com.lightpro.pdv.cmd.PdvEdited;
import com.lightpro.pdv.vm.PdvVm;
import com.lightpro.pdv.vm.ProductVm;
import com.lightpro.pdv.vm.ResumeSalesVm;
import com.lightpro.pdv.vm.SessionVm;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvFreeProducts;
import com.pdv.domains.api.PdvProducts;
import com.pdv.domains.api.Pdvs;
import com.pdv.domains.api.Session;
import com.sales.domains.api.Product;
import com.securities.api.Person;
import com.securities.api.Secured;

@Path("/pdv/pdv")
public class PdvRs extends PdvBaseRs {
	@GET
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAll() throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<PdvVm> items = pdv().pdvs().all()
													 .stream()
											 		 .map(m -> new PdvVm(m))
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
						
						Pdvs container = pdv().pdvs();
						
						List<PdvVm> itemsVm = container.find(page, pageSize, filter).stream()
															 .map(m -> new PdvVm(m))
															 .collect(Collectors.toList());
													
						int count = container.totalCount(filter);
						PaginationSet<PdvVm> pagedSet = new PaginationSet<PdvVm>(itemsVm, page, count);
						
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
						
						PdvVm item = new PdvVm(pdv().pdvs().get(id));

						return Response.ok(item).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/{id}/session-in-progress")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSessionInProgress(@PathParam("id") UUID id, @QueryParam("cashierId") UUID cashierId) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Session item = null;
						Person person = pdv().persons().get(cashierId);
						Pdv pdv = pdv().pdvs().get(id);
						
						if(pdv.sessions().hasSessionInProgress(person)){
							item = pdv.sessions().sessionInProgress(person);
							return Response.ok(new SessionVm(item)).build();
						}else
							return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/{id}/product-to-sale/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getProductsToSale(@PathParam("id") UUID id, 
			@QueryParam("page") int page, 
			@QueryParam("pageSize") int pageSize, 
			@QueryParam("filter") String filter) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PdvProducts container = pdv().pdvs().get(id).productsToSale();
						
						List<ProductVm> itemsVm = container.find(page, pageSize, filter).stream()
															 .map(m -> new ProductVm(m))
															 .collect(Collectors.toList());
													
						int count = container.totalCount(filter);
						PaginationSet<ProductVm> pagedSet = new PaginationSet<ProductVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/{id}/free-product/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getFreeProducts(@PathParam("id") UUID id, 
			@QueryParam("page") int page, 
			@QueryParam("pageSize") int pageSize, 
			@QueryParam("filter") String filter) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PdvFreeProducts container = pdv().pdvs().get(id).freeProducts();
						
						List<ProductVm> itemsVm = container.find(page, pageSize, filter).stream()
															 .map(m -> new ProductVm(m))
															 .collect(Collectors.toList());
													
						int count = container.totalCount(filter);
						PaginationSet<ProductVm> pagedSet = new PaginationSet<ProductVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/{id}/product-to-sale")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getProductsToSaleSearch(@PathParam("id") UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<ProductVm> items = pdv().pdvs().get(id)
													 .productsToSale().all()
													 .stream()
													 .map(m -> new ProductVm(m))
													 .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/resume-sales")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getResumeSales(@QueryParam("start") String startStr, @QueryParam("end") String endStr) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						if(StringUtils.isBlank(startStr) || StringUtils.isBlank(endStr) )
							throw new IllegalArgumentException("Vous devez renseigner une période !");
						
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						
						LocalDate start = LocalDate.parse(startStr, formatter);
						LocalDate end = LocalDate.parse(endStr, formatter);
						
						Pdvs pdvs = pdv().pdvs();
						
						List<ResumeSalesVm> items = new ArrayList<ResumeSalesVm>();
						
						for (Pdv pdv : pdvs.all()) {
							double turnover = pdv.turnover(start, end);
							
							if(pdv.active() || turnover > 0)
								items.add(new ResumeSalesVm(pdv.name(), turnover));							
						}

						return Response.ok(items).build();
					}
				});			
	}
	
	@POST
	@Secured
	@Produces({MediaType.APPLICATION_JSON})
	public Response add(final PdvEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						pdv().pdvs().add(cmd.name());
						
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@POST
	@Secured
	@Path("/{id}/activate")
	@Produces({MediaType.APPLICATION_JSON})
	public Response activate(@PathParam("id") final UUID id, final boolean active) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						pdv().pdvs().get(id).activate(active);
						
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@POST
	@Secured
	@Path("/{id}/session")
	@Produces({MediaType.APPLICATION_JSON})
	public Response addNewSession(@PathParam("id") final UUID id, final String personIdStr) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						UUID personId = UUID.fromString(personIdStr);
						
						Pdv pdv = pdv().pdvs().get(id);
						Person person = pdv().persons().get(personId);
						Session session = pdv.sessions().add(person);
						
						return Response.ok(new SessionVm(session)).build();
					}
				});		
	}
	
	@POST
	@Secured
	@Path("/{id}/product/{productid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response addProduct(@PathParam("id") final UUID id, @PathParam("productid") final UUID productId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Product item = pdv().productCatalog().get(productId);								
						pdv().pdvs().get(id).productsToSale().add(item);
						
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@PUT
	@Secured
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") final UUID id, final PdvEdited cmd) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Pdv item = pdv().pdvs().get(id);
						item.update(cmd.name());
						
						return Response.status(Response.Status.OK).build();
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
						
						Pdv item = pdv().pdvs().get(id);
						pdv().pdvs().delete(item);
						
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
	
	@DELETE
	@Secured
	@Path("/{id}/product/{productid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deleteProductFromPdvCatalog(@PathParam("id") final UUID id, @PathParam("productid") final UUID productId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
												
						Pdv pdv = pdv().pdvs().get(id);
						Product item = pdv.productsToSale().get(productId);
						pdv.productsToSale().delete(item);
						
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
