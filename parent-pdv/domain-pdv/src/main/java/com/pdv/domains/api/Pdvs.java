package com.pdv.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;

public interface Pdvs extends AdvancedQueryable<Pdv, UUID> {
	Pdv add(String name) throws IOException;	
}
