package com.lightpro.pdv.rs;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Callable;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.infrastructure.datasource.Base;
import com.infrastructure.pgsql.PgBase;
import com.pdv.domains.api.PdvModule;
import com.securities.api.Company;
import com.securities.api.Module;
import com.securities.api.ModuleType;
import com.securities.api.Secured;
import com.securities.impl.CompanyDb;

@Path("/pdv/module")
public class PdvModuleRs extends PdvBaseRs {
	@POST
	@Secured
	@Path("/install")
	@Produces({MediaType.APPLICATION_JSON})
	public Response installModule() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
											
						Module module = pdv(currentCompany.modulesSubscribed().get(ModuleType.PDV));					
						module.install();
						
						return Response.status(Response.Status.OK).build();	
					}
				});					
	}
	
	@POST
	@Secured
	@Path("/uninstall")
	@Produces({MediaType.APPLICATION_JSON})
	public Response uninstallModule() throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {
											
						pdv().uninstall();
						
						return Response.status(Response.Status.OK).build();
					}
				});			
	}
	
	@POST
	@Secured
	@Path("/{companyId}/uninstall")
	@Produces({MediaType.APPLICATION_JSON})
	public Response uninstallModule(@PathParam("companyId") final UUID companyId) throws IOException {
		
		return createHttpResponse(
				new Callable<Response>(){
					@Override
					public Response call() throws IOException {

						Base base = PgBase.getInstance(currentUser.id(), companyId);
						
						try {
											
							Company company = new CompanyDb(base, companyId);
							Module module = company.modulesInstalled().get(ModuleType.PDV);
							PdvModule pdv = pdv(module);
							
							pdv.uninstall();
							
							base.commit();
						} catch (IllegalArgumentException e) {
							base.rollback();
							throw e;
						} 
						catch (Exception e) {
							base.rollback();
							throw e;
						} finally {
							base.terminate();
						}						
						
						return Response.status(Response.Status.OK).build();
					}
				});			
	}
}
