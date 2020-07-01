package com.pdv.domains.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvMetadata;
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.api.Pdvs;

public final class PdvsDb extends GuidKeyAdvancedQueryableDb<Pdv, PdvMetadata> implements Pdvs {

	private final transient PdvModule module;
	
	public PdvsDb(final Base base, final PdvModule module){
		super(base, "Point de vente introuvable !");
		this.module = module;
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

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		String statement = String.format("%s pdv "
				+ "WHERE pdv.%s ILIKE ? AND pdv.%s=?",
				dm.domainName(), 
				dm.nameKey(), dm.moduleIdKey());
		
		params.add("%" + filter + "%");
		params.add(module.id());
		
		HorodateMetadata horodateDm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY pdv.%s DESC", horodateDm.dateCreatedKey());
		
		String keyResult = String.format("pdv.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected Pdv newOne(UUID id) {
		return new PdvDb(base, id, module);
	}

	@Override
	public Pdv none() {
		return new PdvNone();
	}
}
