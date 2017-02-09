package com.pdv.domains.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.common.utilities.convert.UUIDConvert;
import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvSessions;
import com.pdv.domains.api.Session;
import com.pdv.domains.api.SessionMetadata;
import com.pdv.domains.api.SessionStatus;
import com.securities.api.Person;
import com.securities.api.Sequence;
import com.securities.api.Sequence.SequenceReserved;
import com.securities.api.SequenceMetadata;
import com.securities.impl.SequenceImpl;

public class PdvSessionsImpl implements PdvSessions {

	private transient final Base base;
	private final transient SessionMetadata dm;
	private final transient DomainsStore ds;
	private final transient Pdv pdv;
	
	public PdvSessionsImpl(final Base base, final Pdv pdv){		
		this.base = base;
		this.dm = SessionMetadata.create();		
		this.ds = this.base.domainsStore(this.dm);	
		this.pdv = pdv;
	}
	
	@Override
	public List<Session> find(String arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Session> find(int arg0, int arg1, String arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int totalCount(String arg0) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Session> all() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session build(UUID id) {
		return new SessionImpl(base, id);
	}

	@Override
	public boolean contains(Session session) {
		try {
			return session.isPresent() && session.pdv().isEqual(pdv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Session get(UUID id) throws IOException {
		Session item = build(id);
		
		if(!contains(item))
			throw new NotFoundException("La session n'a pas été trouvée !");
		
		return item;
	}

	@Override
	public Session add(Person person) throws IOException {

		if(hasSessionInProgress(person)){
			throw new IllegalArgumentException("L'utilisateur a déjà une session active !");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(dm.pdvIdKey(), pdv.id());	
		params.put(dm.cashierIdKey(), person.id());
		params.put(dm.referenceKey(), sequence().generate());
		params.put(dm.statusIdKey(), SessionStatus.IN_USE.id());
		params.put(dm.openedDateKey(), java.sql.Timestamp.valueOf(LocalDateTime.now()));
		params.put(dm.closedDateKey(), null);
		params.put(dm.pdvIdKey(), pdv.id());
		
		UUID id = UUID.randomUUID();
		ds.set(id, params);
		
		return build(id);
	}

	private Sequence sequence() throws IOException{
		SequenceMetadata dm = SequenceImpl.dm();
		DomainsStore ds = base.domainsStore(dm);
	
		SequenceReserved code = SequenceReserved.PDV_SESSION;		
		List<Object> values = ds.find(String.format("SELECT %s FROM %s WHERE %s=?", dm.keyName(), dm.domainName(), dm.codeIdKey()), Arrays.asList(code.id()));
		
		if(values.isEmpty())
			throw new IllegalArgumentException(String.format("Vous devez configurer la séquence de %s !", code.toString()));
		
		return new SequenceImpl(base, UUIDConvert.fromObject(values.get(0)));
	}

	@Override
	public boolean hasSessionInProgress(Person person) {
		
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
	public Session sessionInProgress(Person person) throws IOException {
		
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
