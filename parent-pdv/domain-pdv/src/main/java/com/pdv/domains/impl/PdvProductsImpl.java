package com.pdv.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvMetadata;
import com.pdv.domains.api.PdvProductMetadata;
import com.pdv.domains.api.PdvProducts;
import com.sales.domains.api.Product;
import com.sales.domains.api.ProductMetadata;
import com.sales.domains.impl.ProductImpl;

public class PdvProductsImpl implements PdvProducts {

	private transient final Base base;
	private final transient PdvProductMetadata dm;
	private final transient DomainsStore ds;
	private final transient Pdv pdv;
	
	public PdvProductsImpl(final Base base, final Pdv pdv){		
		this.base = base;
		this.dm = PdvProductMetadata.create();		
		this.ds = this.base.domainsStore(this.dm);	
		this.pdv = pdv;
	}
	
	@Override
	public List<Product> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	public List<Product> find(int page, int pageSize, String filter) throws IOException {
		List<Product> values = new ArrayList<Product>();
		
		HorodateMetadata hm = HorodateImpl.dm();
		ProductMetadata pDm = ProductImpl.dm();
		PdvMetadata pdvDm = PdvMetadata.create();
		String statement = String.format("SELECT p.%s FROM %s p "
										+ "JOIN %s pp ON pp.%s = p.%s "
										+ "left JOIN %s pd ON pd.%s = pp.%s "
										+ "WHERE (p.%s ILIKE ? OR p.%s ILIKE ? OR p.%s ILIKE ?) AND pd.%s=? "
										+ "ORDER BY pp.%s DESC LIMIT ? OFFSET ? ", 
										pDm.keyName(), pDm.domainName(), 
										dm.domainName(), dm.productIdKey(), pDm.keyName(),
										pdvDm.domainName(), pdvDm.keyName(), dm.pdvIdKey(),
										pDm.nameKey(), pDm.barCodeKey(), pDm.descriptionKey(), pdvDm.keyName(),
										hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(pdv.id());
		
		if(pageSize > 0){
			params.add(pageSize);
			params.add((page - 1) * pageSize);
		}else{
			params.add(null);
			params.add(0);
		}
		
		List<DomainStore> results = ds.findDs(statement, params);
		for (DomainStore domainStore : results) {
			values.add(build(UUIDConvert.fromObject(domainStore.key()))); 
		}		
		
		return values;
	}

	@Override
	public int totalCount(String filter) throws IOException {
		
		ProductMetadata pDm = ProductImpl.dm();
		PdvMetadata pdvDm = PdvMetadata.create();
		String statement = String.format("SELECT COUNT(p.%s) FROM %s p "
										+ "JOIN %s pp ON pp.%s = p.%s "
										+ "left JOIN %s pd ON pd.%s = pp.%s "
										+ "WHERE (p.%s ILIKE ? OR p.%s ILIKE ? OR p.%s ILIKE ?) AND pd.%s=? ", 
										pDm.keyName(), pDm.domainName(), 
										dm.domainName(), dm.productIdKey(), pDm.keyName(),
										pdvDm.domainName(), pdvDm.keyName(), dm.pdvIdKey(),
										pDm.nameKey(), pDm.barCodeKey(), pDm.descriptionKey(), pdvDm.keyName());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add("%" + filter + "%");
		params.add(pdv.id());
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());	
	}

	@Override
	public List<Product> all() throws IOException {
		return find(0, 0, "");
	}

	@Override
	public Product build(UUID id) {
		try {
			return pdv.module().productCatalog().build(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean contains(Product product) {
		
		ProductMetadata pDm = ProductImpl.dm();
		PdvMetadata pdvDm = PdvMetadata.create();
		String statement = String.format("SELECT COUNT(p.%s) FROM %s p "
										+ "JOIN %s pp ON pp.%s = p.%s "
										+ "left JOIN %s pd ON pd.%s = pp.%s "
										+ "WHERE pp.%s=? AND pp.%s=? ",
										pDm.keyName(), pDm.domainName(), 
										dm.domainName(), dm.productIdKey(), pDm.keyName(),
										pdvDm.domainName(), pdvDm.keyName(), dm.pdvIdKey(),
										dm.productIdKey(), dm.pdvIdKey());
		
		List<Object> params = new ArrayList<Object>();
		params.add(product.id());
		params.add(pdv.id());
		
		List<Object> results = null;
		
		try {
			results = ds.find(statement, params);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Integer.parseInt(results == null ? "0" : results.get(0).toString()) > 0;	
	}

	@Override
	public Product get(UUID id) throws IOException {
		Product item = build(id);
		
		if(!contains(item))
			throw new NotFoundException("Le produit n'a pas été trouvé !");
		
		return item;
	}

	@Override
	public void delete(Product item) throws IOException {
		String statement = String.format("DELETE FROM %s WHERE %s=? AND %s=? ", 
									      dm.domainName(), dm.productIdKey(), dm.pdvIdKey());
		ds.execute(statement, Arrays.asList(item.id(), pdv.id()));		
	}

	@Override
	public void add(Product item) throws IOException {
		if(contains(item))
			return;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.pdvIdKey(), pdv.id());	
		params.put(dm.productIdKey(), item.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
	}
}
