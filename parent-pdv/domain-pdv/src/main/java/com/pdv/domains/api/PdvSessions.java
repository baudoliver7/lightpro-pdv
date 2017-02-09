package com.pdv.domains.api;

import java.io.IOException;

import com.securities.api.Person;

public interface PdvSessions extends Sessions {
	Session add(Person cashier) throws IOException;
	boolean hasSessionInProgress(Person person);
	Session sessionInProgress(Person person) throws IOException;		
}
