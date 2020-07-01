package com.pdv.domains.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.infrastructure.core.GuidKeyEntityDb;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.pdv.domains.api.Cashiers;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvMetadata;
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.api.PdvProductCategories;
import com.pdv.domains.api.PdvProducts;
import com.pdv.domains.api.Sessions;
import com.sales.domains.api.ModulePdv;
import com.sales.domains.api.PaymentMetadata;
import com.sales.domains.api.PaymentStatus;
import com.sales.domains.api.PaymentType;
import com.sales.domains.api.Sales;
import com.sales.domains.impl.ProductCategoryNone;
import com.sales.domains.impl.SalesDb;
import com.securities.api.Module;
import com.securities.api.ModuleType;

public final class PdvDb extends GuidKeyEntityDb<Pdv, PdvMetadata> implements Pdv {
	
	private final PdvModule module;
	
	public PdvDb(final Base base, final UUID id, final PdvModule module){
		super(base, id, "Point de vente introuvable !");
		this.module = module;
	}
	
	private Sales sales() throws IOException{
		Module module = this.module.company().modulesInstalled().get(ModuleType.SALES);
		return new SalesDb(base, module);
	}
	
	@Override
	public String name() throws IOException {
		return ds.get(dm.nameKey());
	}

	@Override
	public boolean active() throws IOException {
		return ds.get(dm.activeKey());
	}

	@Override
	public void update(String name) throws IOException {
		
		if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Vous devez spécifier un nom !");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);	
		
		ds.set(params);	
	}

	@Override
	public void activate(boolean active) throws IOException {
		ds.set(dm.activeKey(), active);
	}

	@Override
	public Sessions sessions() throws IOException {
		return module().sessions().of(this);
	}	

	@Override
	public double turnover(LocalDate start, LocalDate end) throws IOException {
		
		PaymentMetadata dmPay = PaymentMetadata.create();
		DomainsStore ds = base.domainsStore(dmPay);
		
		String statement = String.format("SELECT SUM(pay.%s) FROM %s pay "
										+ "WHERE (pay.%s BETWEEN ? AND ?) AND pay.%s=? AND pay.%s=? AND pay.%s=?", 
										dmPay.paidAmountKey(), dmPay.domainName(),
										dmPay.paymentDateKey(), dmPay.modulePdvIdKey(), dmPay.statusIdKey(), dmPay.typeIdKey());
		
		List<Object> params = new ArrayList<Object>();
		params.add(java.sql.Date.valueOf(start));
		params.add(java.sql.Date.valueOf(end));
		params.add(this.id);
		params.add(PaymentStatus.VALIDATED.id());
		params.add(PaymentType.ENCAISSEMENT.id());
		
		List<Object> results = ds.find(statement, params);
		return Double.parseDouble(results.get(0) == null ? "0": results.get(0).toString());
	}

	@Override
	public PdvModule module() throws IOException {
		return module;
	}

	@Override
	public Cashiers cashiers() throws IOException {
		return module().cashiers().of(this);
	}

	@Override
	public PdvProducts products() throws IOException {
		return new PdvProductsDb(base, this, sales(), new ProductCategoryNone(), false);
	}

	@Override
	public PdvProductCategories productCategories() throws IOException {
		return new PdvProductCategoriesDb(base, module(), sales(), this, false);
	}

	@Override
	public ModulePdv modulePdv() throws IOException {
		return module().modulePdvs().get(id());
	}
}
