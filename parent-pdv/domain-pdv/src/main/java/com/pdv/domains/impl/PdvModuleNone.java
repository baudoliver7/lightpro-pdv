package com.pdv.domains.impl;

import java.io.IOException;

import com.infrastructure.core.GuidKeyEntityNone;
import com.pdv.domains.api.Cashiers;
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.api.PdvPurchaseOrders;
import com.pdv.domains.api.Pdvs;
import com.pdv.domains.api.Sessions;
import com.sales.domains.api.ModulePdvs;
import com.sales.domains.api.Sellers;
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

public final class PdvModuleNone extends GuidKeyEntityNone<PdvModule> implements PdvModule {

	@Override
	public void activate(boolean arg0) throws IOException {
		

	}

	@Override
	public Company company() throws IOException {
		
		return null;
	}

	@Override
	public String description() throws IOException {
		
		return null;
	}

	@Override
	public Features featuresProposed() throws IOException {
		
		return null;
	}

	@Override
	public Module install() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public boolean isActive() {
		
		return false;
	}

	@Override
	public boolean isInstalled() {
		
		return false;
	}

	@Override
	public boolean isSubscribed() {
		
		return false;
	}

	@Override
	public String name() throws IOException {
		
		return null;
	}

	@Override
	public int order() throws IOException {
		
		return 0;
	}

	@Override
	public String shortName() throws IOException {
		
		return null;
	}

	@Override
	public ModuleType type() throws IOException {
		
		return null;
	}

	@Override
	public Module uninstall() throws IOException {
		throw new UnsupportedOperationException("Opération non supportée !");
	}

	@Override
	public Pdvs pdvs() throws IOException {
		
		return null;
	}

	@Override
	public PdvPurchaseOrders orders() throws IOException {
		
		return null;
	}

	@Override
	public Sessions sessions() throws IOException {
		
		return null;
	}

	@Override
	public Contacts contacts() throws IOException {
		
		return null;
	}

	@Override
	public Cashiers cashiers() {
		
		return null;
	}

	@Override
	public PaymentModes paymentModes() throws IOException {
		
		return null;
	}

	@Override
	public Features featuresAvailable() throws IOException {
		
		return null;
	}

	@Override
	public Features featuresSubscribed() throws IOException {
		
		return null;
	}

	@Override
	public Indicators indicators() throws IOException {
		
		return null;
	}

	@Override
	public Module subscribe() throws IOException {
		
		return null;
	}

	@Override
	public FeatureSubscribed subscribeTo(Feature arg0) throws IOException {
		
		return null;
	}

	@Override
	public Module unsubscribe() throws IOException {
		
		return null;
	}

	@Override
	public void unsubscribeTo(Feature arg0) throws IOException {
		
		
	}

	@Override
	public Log log() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sellers sellers() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModulePdvs modulePdvs() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
