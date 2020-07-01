package com.pdv.domains.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.infrastructure.core.DomainMetadata;
import com.infrastructure.core.EntityBase;
import com.infrastructure.datasource.Base;
import com.pdv.domains.api.CashierMetadata;
import com.pdv.domains.api.Cashiers;
import com.pdv.domains.api.PdvMetadata;
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.api.PdvProductCategoryMetadata;
import com.pdv.domains.api.PdvProductMetadata;
import com.pdv.domains.api.PdvPurchaseOrderMetadata;
import com.pdv.domains.api.PdvPurchaseOrderStatus;
import com.pdv.domains.api.Pdvs;
import com.pdv.domains.api.SessionMetadata;
import com.pdv.domains.api.PdvPurchaseOrders;
import com.pdv.domains.api.Sessions;
import com.sales.domains.api.InvoiceMetadata;
import com.sales.domains.api.ModulePdvs;
import com.sales.domains.api.PaymentMetadata;
import com.sales.domains.api.PurchaseOrderMetadata;
import com.sales.domains.api.Sales;
import com.sales.domains.api.Sellers;
import com.sales.domains.impl.SalesDb;
import com.securities.api.Company;
import com.securities.api.Contacts;
import com.securities.api.Feature;
import com.securities.api.FeatureSubscribed;
import com.securities.api.Features;
import com.securities.api.Indicators;
import com.securities.api.Log;
import com.securities.api.Module;
import com.securities.api.ModuleType;
import com.securities.api.PaymentModes;
import com.securities.api.Sequence;
import com.securities.api.Sequence.SequenceReserved;
import com.securities.api.Sequences;

public final class PdvModuleDb extends EntityBase<PdvModule, UUID> implements PdvModule {

	private final transient Base base;
	private final transient Module origin;
	
	public PdvModuleDb(Base base, final Module module){
		super(module.id());
		this.base = base;
		this.origin = module;
	}
	
	private Sales sales() throws IOException{
		Module module = company().modulesInstalled().get(ModuleType.SALES);
		return new SalesDb(base, module);
	}
	
	@Override
	public Pdvs pdvs() {
		return new PdvsDb(base, this);
	}
	
	@Override
	public Contacts contacts() throws IOException {
		return company().moduleAdmin().contacts();
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
	public Module install() throws IOException {
		
		// 1 - vérification des prérequis
		if(!company().modulesInstalled().contains(ModuleType.SALES))
			throw new IllegalArgumentException("Le module Ventes doit être installé avant de continuer l'action !");
				
		Module module = origin.install();
		
		Sequences sequences = company().moduleAdmin().sequences();
		sequences.reserved(SequenceReserved.PDV_SESSION);
		
		return new PdvModuleDb(base, module);
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
	public Module uninstall() throws IOException {
		
		UUID salesId = sales().id();
		
		PurchaseOrderMetadata poDm = PurchaseOrderMetadata.create();
		base.executeUpdate(String.format("UPDATE %s SET %s=%s WHERE %s=?", poDm.domainName(), poDm.modulePdvIdKey(), poDm.moduleIdKey(), poDm.moduleIdKey()), Arrays.asList(salesId));
		
		InvoiceMetadata invDm = InvoiceMetadata.create();
		base.executeUpdate(String.format("UPDATE %s SET %s=%s WHERE %s=?", invDm.domainName(), invDm.modulePdvIdKey(), invDm.moduleIdKey(), invDm.moduleIdKey()), Arrays.asList(salesId));
		
		PaymentMetadata payDm = PaymentMetadata.create();
		base.executeUpdate(String.format("UPDATE %s SET %s=%s WHERE %s=?", payDm.domainName(), payDm.modulePdvIdKey(), payDm.moduleIdKey(), payDm.moduleIdKey()), Arrays.asList(salesId));
		
		List<DomainMetadata> domains = 
				Arrays.asList(
					PdvPurchaseOrderMetadata.create(),
					SessionMetadata.create(),										
					CashierMetadata.create(),
					PdvProductMetadata.create(),
					PdvProductCategoryMetadata.create(),
					PdvMetadata.create()
				);		
		
		for (DomainMetadata domainMetadata : domains) {
			base.deleteAll(domainMetadata); 
		}
		
		Sequences sequences = company().moduleAdmin().sequences();
		Sequence sessionSequence = sequences.reserved(SequenceReserved.PDV_SESSION);
		
		try {
			sequences.delete(sessionSequence); 
		} catch (Exception ignore) {}
		
		// finaliser
		Module module = origin.uninstall();
		return new PdvModuleDb(base, module);
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
	public PdvPurchaseOrders orders() throws IOException {
		return new PdvPurchaseOrdersDb(base, this, sales(), new PdvNone(), new SessionNone(), PdvPurchaseOrderStatus.NONE);
	}

	@Override
	public Sessions sessions() throws IOException {
		return new SessionsDb(base, this, new PdvNone());
	}

	@Override
	public void activate(boolean active) throws IOException {
		origin.activate(active);
	}

	@Override
	public boolean isActive() {
		return origin.isActive();
	}

	@Override
	public Features featuresProposed() throws IOException {
		return origin.featuresProposed();
	}

	@Override
	public Cashiers cashiers() {
		return new CashiersDb(base, this, new PdvNone());
	}

	@Override
	public PaymentModes paymentModes() throws IOException {
		return sales().paymentModes();
	}

	@Override
	public Features featuresAvailable() throws IOException {
		return origin.featuresAvailable();
	}

	@Override
	public Features featuresSubscribed() throws IOException {
		return origin.featuresSubscribed();
	}

	@Override
	public Indicators indicators() throws IOException {
		return origin.indicators();
	}

	@Override
	public Module subscribe() throws IOException {
		return origin.subscribe();
	}

	@Override
	public FeatureSubscribed subscribeTo(Feature feature) throws IOException {
		return origin.subscribeTo(feature);
	}

	@Override
	public Module unsubscribe() throws IOException {
		return origin.unsubscribe();
	}

	@Override
	public void unsubscribeTo(Feature feature) throws IOException {
		origin.unsubscribeTo(feature);
	}

	@Override
	public Log log() throws IOException {
		return origin.log();
	}

	@Override
	public Sellers sellers() throws IOException {
		return sales().sellers();
	}

	@Override
	public ModulePdvs modulePdvs() throws IOException {
		return sales().modulePdvs();
	}
}
