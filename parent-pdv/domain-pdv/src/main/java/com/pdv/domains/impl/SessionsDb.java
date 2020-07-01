package com.pdv.domains.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.core.GuidKeyAdvancedQueryableDb;
import com.infrastructure.core.HorodateMetadata;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.QueryBuilder;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvMetadata;
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.api.Session;
import com.pdv.domains.api.SessionMetadata;
import com.pdv.domains.api.SessionStatus;
import com.pdv.domains.api.Sessions;
import com.securities.api.Contact;
import com.securities.api.Sequence;
import com.securities.api.Sequence.SequenceReserved;

public final class SessionsDb extends GuidKeyAdvancedQueryableDb<Session, SessionMetadata> implements Sessions {

	private final transient PdvModule module;
	private final transient Pdv pdv;
	
	public SessionsDb(final Base base, final PdvModule module, final Pdv pdv){		
		super(base, "Session introuvable !");	
		this.module = module;
		this.pdv = pdv;
	}

	@Override
	protected QueryBuilder buildQuery(String filter) throws IOException {
		List<Object> params = new ArrayList<Object>();
		filter = StringUtils.defaultString(filter);
		
		PdvMetadata pdvDm = new PdvMetadata();
		String statement = String.format("%s sess "
				+ "JOIN %s pdv ON pdv.%s=sess.%s "
				+ "WHERE sess.%s ILIKE ? AND pdv.%s=?",
				dm.domainName(), 
				pdvDm.domainName(), pdvDm.keyName(), dm.pdvIdKey(),
				dm.referenceKey(), pdvDm.moduleIdKey());
		
		params.add("%" + filter + "%");
		params.add(module.id());
		
		if(!pdv.isNone()){
			statement = String.format("%s AND sess.%s=?", statement, dm.pdvIdKey());
			params.add(pdv.id());
		}
		
		HorodateMetadata horodateDm = HorodateMetadata.create();
		String orderClause = String.format("ORDER BY sess.%s DESC", horodateDm.dateCreatedKey());
		
		String keyResult = String.format("sess.%s", dm.keyName());
		return base.createQueryBuilder(ds, statement, params, keyResult, orderClause);
	}

	@Override
	protected Session newOne(UUID id) {
		return new SessionDb(base, id, module);
	}

	@Override
	public Session none() {
		return new SessionNone();
	}

	@Override
	public Sessions of(Pdv pdv) throws IOException {
		return new SessionsDb(base, module, pdv);
	}
	
	@Override
	public Session add(Contact person) throws IOException {

		if(pdv.isNone())
			throw new IllegalArgumentException("Vous devez spécifier un point de vente !");
		
		if(hasSessionInProgress(person)){
			throw new IllegalArgumentException("L'utilisateur a déjà une session active !");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.pdvIdKey(), pdv.id());	
		params.put(dm.cashierIdKey(), person.id());
		
		Sequence sequence = pdv.module().company().moduleAdmin().sequences().reserved(SequenceReserved.PDV_SESSION);
		params.put(dm.referenceKey(), sequence.generate());
		params.put(dm.statusIdKey(), SessionStatus.IN_USE.id());
		params.put(dm.openedDateKey(), java.sql.Timestamp.valueOf(LocalDateTime.now()));
		params.put(dm.closedDateKey(), null);
		params.put(dm.pdvIdKey(), pdv.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}

	@Override
	public boolean hasSessionInProgress(Contact person) {
		
		if(pdv.isNone())
			throw new IllegalArgumentException("Vous devez spécifier un point de vente !");
		
		String statement = String.format("SELECT COUNT(%s) FROM %s "
										+ "WHERE %s=? AND %s=? AND %s=?",
										dm.keyName(), dm.domainName(), 
										dm.cashierIdKey(), dm.pdvIdKey(), dm.statusIdKey());
		
		List<Object> params = new ArrayList<Object>();
		params.add(person.id());
		params.add(pdv.id());
		params.add(SessionStatus.IN_USE.id());
		
		List<Object> results = null;
		
		try {
			results = ds.find(statement, params);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Integer.parseInt(results == null ? "0" : results.get(0).toString()) > 0;
	}

	@Override
	public Session sessionInProgress(Contact person) throws IOException {
		
		if(pdv.isNone())
			throw new IllegalArgumentException("Vous devez spécifier un point de vente !");
		
		String statement = String.format("SELECT %s FROM %s "
										+ "WHERE %s=? AND %s=? AND %s=?",
										dm.keyName(), dm.domainName(), 
										dm.cashierIdKey(), dm.pdvIdKey(), dm.statusIdKey());

		List<Object> params = new ArrayList<Object>();
		params.add(person.id());
		params.add(pdv.id());
		params.add(SessionStatus.IN_USE.id());
		
		List<Object> results = null;
		
		results = ds.find(statement, params);
		
		if(results.isEmpty())
			throw new IOException("L'utilisateur n'a pas de session active !");
		
		return build(UUIDConvert.fromObject(results.get(0)));
	}
}
