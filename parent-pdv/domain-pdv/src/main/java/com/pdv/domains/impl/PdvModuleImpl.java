package com.pdv.domains.impl;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.Horodate;
import com.infrastructure.datasource.Base;
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.api.Pdvs;
import com.pdv.domains.api.Session;
import com.pdv.domains.api.SessionPurchaseOrders;
import com.pdv.domains.api.Sessions;
import com.sales.domains.api.Customers;
import com.sales.domains.api.Products;
import com.sales.domains.api.Sales;
import com.sales.domains.impl.SalesImpl;
import com.securities.api.Company;
import com.securities.api.Membership;
import com.securities.api.Module;
import com.securities.api.ModuleType;
import com.securities.api.Persons;
import com.securities.impl.BasisModule;
import com.securities.impl.MembershipImpl;

public class PdvModuleImpl implements PdvModule {

	private final transient Base base;
	private final transient Module origin;
	
	public PdvModuleImpl(Base base, final UUID id){
		this.base = base;
		this.origin = new BasisModule(base, id);
	}
	
	private final Sales sales() throws IOException {
		Module module = company().modules().get(ModuleType.SALES);
		return new SalesImpl(base, module.id());
	}
	
	@Override
	public Pdvs pdvs() {
		return new PdvsImpl(base, this);
	}

	@Override
	public Products productCatalog() throws IOException {
		return sales().products();
	}
	
	@Override
	public Persons persons() throws IOException {
		return company().persons();
	}

	@Override
	public Customers customers() throws IOException {
		return sales().customers();
	}

	@Override
	public Membership membership() throws IOException {
		return new MembershipImpl(base);		
	}

	@Override
	public Company company() throws IOException {
		return origin.company();
	}

	@Override
	public String description() throws IOException {
		return origin.description();
	}

	@Override
	public void install() throws IOException {
		origin.install();
	}

	@Override
	public boolean isAvailable() {
		return origin.isAvailable();
	}

	@Override
	public boolean isInstalled() {
		return origin.isInstalled();
	}

	@Override
	public boolean isSubscribed() {
		return origin.isSubscribed();
	}

	@Override
	public String name() throws IOException {
		return origin.name();
	}

	@Override
	public ModuleType type() throws IOException {
		return origin.type();
	}

	@Override
	public void uninstall() throws IOException {
		origin.uninstall();
	}

	@Override
	public UUID id() {
		return origin.id();
	}

	@Override
	public boolean isEqual(Module item) {
		return origin.isEqual(item);
	}

	@Override
	public boolean isNotEqual(Module item) {
		return origin.isNotEqual(item);
	}

	@Override
	public boolean isPresent() {
		return origin.isPresent();
	}

	@Override
	public Horodate horodate() {
		return origin.horodate();
	}

	@Override
	public int order() throws IOException {
		return origin.order();
	}

	@Override
	public String shortName() throws IOException {
		return origin.shortName();
	}

	@Override
	public SessionPurchaseOrders orders(Session session) throws IOException {
		return new SessionPurchaseOrdersImpl(base, session, sales().orders());
	}

	@Override
	public Sessions sessions() throws IOException {
		return new SessionsImpl(base, this);
	}

}
