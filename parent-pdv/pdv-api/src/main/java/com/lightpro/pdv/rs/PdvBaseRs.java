package com.lightpro.pdv.rs;

import java.io.IOException;

import com.pdv.domains.api.PdvModule;
import com.pdv.domains.impl.PdvModuleImpl;
import com.securities.api.BaseRs;
import com.securities.api.Module;
import com.securities.api.ModuleType;

public abstract class PdvBaseRs extends BaseRs {
	protected PdvModule pdv() throws IOException {
		Module module = currentCompany().modules().get(ModuleType.PDV);
		return new PdvModuleImpl(base(), module.id());
	}
}
