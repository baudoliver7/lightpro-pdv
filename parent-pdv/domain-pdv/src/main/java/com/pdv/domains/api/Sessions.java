package com.pdv.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.securities.api.Contact;

public interface Sessions extends AdvancedQueryable<Session, UUID> {
	Session add(Contact cashier) throws IOException;
	boolean hasSessionInProgress(Contact person);
	Session sessionInProgress(Contact person) throws IOException;
	
	Sessions of(Pdv pdv) throws IOException;
}
