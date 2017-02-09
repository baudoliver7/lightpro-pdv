package com.pdv.domains.impl;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import com.infrastructure.datasource.Base;
import com.infrastructure.datasource.DomainsStore;
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.api.Session;
import com.pdv.domains.api.SessionMetadata;
import com.pdv.domains.api.Sessions;

public class SessionsImpl implements Sessions {

	private transient final Base base;
	private final transient SessionMetadata dm;
	@SuppressWarnings("unused")
	private final transient DomainsStore ds;
	private final transient PdvModule module;
	
	public SessionsImpl(final Base base, final PdvModule module){		
		this.base = base;
		this.dm = SessionMetadata.create();		
		this.ds = this.base.domainsStore(this.dm);	
		this.module = module;
	}
	
	@Override
	public List<Session> find(String filter) throws IOException {
		return find(0, 0, filter);
	}

	@Override
	public List<Session> find(int page, int pageSize, String filter) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int totalCount(String filter) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Session> all() throws IOException {
		return find(0, 0, "");
	}

	@Override
	public Session build(UUID id) {
		return new SessionImpl(base, id);
	}

	@Override
	public boolean contains(Session item) {
		try {
			return item.isPresent() && item.pdv().module().isEqual(module);
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

}
