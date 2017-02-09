package com.pdv.domains.api;

import java.io.IOException;
import java.util.UUID;

import com.infrastructure.core.AdvancedQueryable;
import com.infrastructure.core.Updatable;

public interface Pdvs extends AdvancedQueryable<Pdv, UUID>, Updatable<Pdv> {
	Pdv add(String name) throws IOException;	
}
