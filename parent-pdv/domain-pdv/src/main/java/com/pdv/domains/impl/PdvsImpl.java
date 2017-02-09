package com.pdv.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.core.impl.HorodateImpl;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainStore;
import com.infrastructure.datasource.DomainsStore;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvMetadata;
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.api.Pdvs;

public class PdvsImpl implements Pdvs {

	private transient final Base base;
	private final transient PdvMetadata dm;
	private final transient DomainsStore ds;
	private final transient PdvModule module;
	
	public PdvsImpl(final Base base, final PdvModule module){
		this.base = base;
		this.dm = PdvMetadata.create();
		this.ds = this.base.domainsStore(this.dm);	
		this.module = module;
	}
	
	@Override
	public List<Pdv> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	public List<Pdv> find(int page, int pageSize, String filter) throws IOException {
		List<Pdv> values = new ArrayList<Pdv>();
		
		HorodateMetadata hm = HorodateImpl.dm();
		String statement = String.format("SELECT %s FROM %s "
				+ "WHERE %s ILIKE ? AND %s=? "
				+ "ORDER BY %s DESC LIMIT ? OFFSET ?", dm.keyName(), dm.domainName(), dm.nameKey(), dm.moduleIdKey(), hm.dateCreatedKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add(module.id());
		
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
		String statement = String.format("SELECT COUNT(%s) FROM %s WHERE %s ILIKE ? AND %s=?", dm.keyName(), dm.domainName(), dm.nameKey(), dm.moduleIdKey());
		
		List<Object> params = new ArrayList<Object>();
		filter = (filter == null) ? "" : filter;
		params.add("%" + filter + "%");
		params.add(module.id());
		
		List<Object> results = ds.find(statement, params);
		return Integer.parseInt(results.get(0).toString());	
	}

	@Override
	public List<Pdv> all() throws IOException {
		return find(0, 0, "");
	}

	@Override
	public Pdv build(UUID id) {
		return new PdvImpl(base, id);
	}

	@Override
	public boolean contains(Pdv item) {
		try {
			return ds.exists(item.id()) && item.module().isEqual(module);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Pdv get(UUID id) throws IOException {
		Pdv item = build(id);
		
		if(!contains(item))
			throw new NotFoundException("Le produit n'a pas été trouvé !");
		
		return item;
	}

	@Override
	public void delete(Pdv item) throws IOException {
		ds.delete(item.id());
	}

	@Override
	public Pdv add(String name) throws IOException {
		
		if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Invalid name : it can't be empty!");
        }
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.nameKey(), name);	
		params.put(dm.activeKey(), true);	
		params.put(dm.moduleIdKey(), module.id());	
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);	
	}
}
