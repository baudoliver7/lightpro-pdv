package com.lightpro.pdv.rs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pdv.domains.api.Cashier;
import com.pdv.domains.api.Pdv;
import com.pdv.domains.api.PdvModule;
import com.pdv.domains.impl.PdvModuleDb;
import com.securities.api.BaseRs;
import com.securities.api.Module;
import com.securities.api.ModuleType;

public abstract class PdvBaseRs extends BaseRs {
	
	public PdvBaseRs() {
		super(ModuleType.PDV);
	}

	protected PdvModule pdv() throws IOException {
		return pdv(currentModule);
	}
	
	protected PdvModule pdv(Module module) throws IOException {
		return new PdvModuleDb(base, module);
	}
	
	protected List<Pdv> pdvsEnabled() throws IOException {
			
		if(currentUser.profile().equals(currentCompany.moduleAdmin().profiles().superAdministratorProfile()))
			return pdv().pdvs().all();
		
		List<Pdv> pdvs = new ArrayList<Pdv>();
		
		Cashier cashier = pdv().cashiers().build(currentUser.id());
		if(!cashier.isNone())
			pdvs.add(cashier.pdv());
		else
			pdvs = pdv().pdvs().all();
		
		return pdvs;
	}
}
