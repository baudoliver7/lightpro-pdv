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
import com.lightpro.pdv.vm.ProductCategoryVm;
import com.lightpro.pdv.vm.ProductVm;
import com.lightpro.pdv.vm.ResumeSalesVm;
import com.lightpro.pdv.vm.SessionVm;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvProductCategories;
import com.pdv.domains.api.PdvProducts;
import com.pdv.domains.api.Pdvs;
import com.pdv.domains.api.Session;
import com.sales.domains.api.Product;
import com.sales.domains.api.ProductCategory;
import com.securities.api.Contact;
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
												
						List<PdvVm> items = pdvsEnabled().stream()
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
						
						List<PdvVm> itemsVm = new ArrayList<PdvVm>();
						long count = 0;
						
						if(pdvsEnabled().size() == 1){
							itemsVm.add(new PdvVm(pdvsEnabled().get(0)));
							count = 0;
						}else {
							itemsVm = container.find(page, pageSize, filter).stream()
									 .map(m -> new PdvVm(m))
									 .collect(Collectors.toList());
							
							count = container.count(filter);
						}
																						
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
						Contact person = pdv().contacts().get(cashierId);
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
	@Path("/{id}/product-category-to-sale/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getProductCategoriesToSale(@PathParam("id") UUID id, 
			@QueryParam("page") int page, 
			@QueryParam("pageSize") int pageSize, 
			@QueryParam("filter") String filter) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PdvProductCategories container = pdv().pdvs().get(id).productCategories();
						
						List<ProductCategoryVm> itemsVm = container.find(page, pageSize, filter).stream()
															 .map(m -> new ProductCategoryVm(m))
															 .collect(Collectors.toList());
													
						long count = container.count(filter);
						PaginationSet<ProductCategoryVm> pagedSet = new PaginationSet<ProductCategoryVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
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
			@QueryParam("filter") String filter,
			@QueryParam("categoryId") UUID categoryId) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Pdv pdv = pdv().pdvs().get(id);
						ProductCategory category = pdv.productCategories().build(categoryId);
						PdvProducts container = pdv.products().of(category);
						
						List<ProductVm> itemsVm = container.find(page, pageSize, filter).stream()
															 .map(m -> new ProductVm(m))
															 .collect(Collectors.toList());
													
						long count = container.count(filter);
						PaginationSet<ProductVm> pagedSet = new PaginationSet<ProductVm>(itemsVm, page, count);
						
						return Response.ok(pagedSet).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/{id}/free-product-category/search")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getFreeProductCategories(@PathParam("id") UUID id, 
			@QueryParam("page") int page, 
			@QueryParam("pageSize") int pageSize, 
			@QueryParam("filter") String filter) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						PdvProductCategories container = pdv().pdvs().get(id).productCategories().freeCategories();
						
						List<ProductCategoryVm> itemsVm = container.find(page, pageSize, filter).stream()
															 .map(m -> new ProductCategoryVm(m))
															 .collect(Collectors.toList());
													
						long count = container.count(filter);
						PaginationSet<ProductCategoryVm> pagedSet = new PaginationSet<ProductCategoryVm>(itemsVm, page, count);
						
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
			@QueryParam("filter") String filter,
			@QueryParam("categoryId") UUID categoryId) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						Pdv pdv = pdv().pdvs().get(id);
						ProductCategory category = pdv.productCategories().freeCategories().build(categoryId);
						PdvProducts container = pdv.products().freeProducts().of(category);
						
						List<ProductVm> itemsVm = container.find(page, pageSize, filter).stream()
															 .map(m -> new ProductVm(m))
															 .collect(Collectors.toList());
													
						long count = container.count(filter);
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
													 .products().all()
													 .stream()
													 .map(m -> new ProductVm(m))
													 .collect(Collectors.toList());

						return Response.ok(items).build();
					}
				});		
	}
	
	@GET
	@Secured
	@Path("/{id}/product-category-to-sale")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getProductCategoriesToSaleSearch(@PathParam("id") UUID id) throws IOException {	
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
						
						List<ProductCategoryVm> items = pdv().pdvs().get(id)
													 .productCategories().all()
													 .stream()
													 .map(m -> new ProductCategoryVm(m))
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

						List<ResumeSalesVm> items = new ArrayList<ResumeSalesVm>();
						
						for (Pdv pdv : pdvsEnabled()) {
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
						
						Pdv pdv = pdv().pdvs().add(cmd.name());
						
						log.info(String.format("Création du point de vente %s", pdv.name()));
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
						
						Pdv item = pdv().pdvs().get(id);
						item.activate(active);
						
						if(active)
							log.info(String.format("Activation du point de vente %s", item.name()));
						else
							log.info(String.format("Désactivation du point de vente %s", item.name()));
						
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
						Contact person = pdv().contacts().get(personId);
						Session session = pdv.sessions().add(person);
						
						log.info(String.format("Création d'une nouvelle session %s", pdv.name()));
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
																			
						Pdv pdv = pdv().pdvs().get(id);
						Product item = pdv.products().freeProducts().get(productId);
						pdv.products().add(item);
						
						log.info(String.format("Ajout du produit %s au point de vente %s", item.name(), pdv.name()));
						return Response.status(Response.Status.OK).build();
					}
				});		
	}
	
	@POST
	@Secured
	@Path("/{id}/product-category/{productcategoryid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response addProductCategory(@PathParam("id") final UUID id, @PathParam("productcategoryid") final UUID productCategoryId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
													
						Pdv pdv = pdv().pdvs().get(id);
						ProductCategory item = pdv.productCategories().freeCategories().get(productCategoryId);
						pdv.productCategories().add(item);
						
						log.info(String.format("Ajout de la catégorie de produit %s au point de vente %s", item.name(), pdv.name()));
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
						
						log.info(String.format("Mise à jour des données du point de vente %s", item.name()));
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
						
						log.info(String.format("Suppression du point de vente %s", item.name()));
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
						Product item = pdv.products().get(productId);
						pdv.products().delete(item);
						
						log.info(String.format("Suppression du produit %s du point de vente %s", item.name(), pdv.name()));
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
	
	@DELETE
	@Secured
	@Path("/{id}/product-category/{productcategoryid}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deletePdvProductCategory(@PathParam("id") final UUID id, @PathParam("productcategoryid") final UUID productCategoryId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
												
						Pdv pdv = pdv().pdvs().get(id);
						ProductCategory item = pdv.productCategories().get(productCategoryId);
						pdv.productCategories().delete(item);
						
						log.info(String.format("Suppression de la catégorie de produit %s du point de vente %s", item.name(), pdv.name()));
						return Response.status(Response.Status.OK).build();
					}
				});	
	}
}
