package com.lightpro.pdv.vm;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.sales.domains.api.Payment;

public final class PaymentVm {
	public final UUID id;
	public final LocalDate paymentDate;
	public final String reference;
	public final String object;
	public final UUID modeId;
	public final String mode;
	public final String transactionReference;
	public final double paidAmount;
	public final double providedAmount;
	public final double affectedPaidAmount;
	public final String origin;
	public final UUID originId;
	public final String step;
	public final int stepId;
	public final UUID modulePdvId;
	public final String modulePdv;
	public final String status;
	public final int statusId;
	public final String type;
	public final int typeId;
	public final int natureId;
	public final String nature;
	public final int originTypeId;
	public final String originType;
	
	public PaymentVm(){
		throw new UnsupportedOperationException("#PaymentVm()");
	}
	
	public PaymentVm(final Payment origin) {
		try {
			this.id = origin.id();
			this.paymentDate = origin.paymentDate();
			this.reference = origin.reference();
			this.object = origin.object();
			this.origin = origin.origin();
			this.mode = origin.mode().name();
			this.modeId = origin.mode().id();
			this.transactionReference = origin.transactionReference();
			this.paidAmount = origin.paidAmount();
			this.providedAmount = origin.providedAmount();
			this.affectedPaidAmount = origin.affectedPaidAmount();
			this.originId = origin.originId();
			this.step = origin.step().toString();
			this.stepId = origin.step().id();
			this.modulePdvId = origin.modulePdv().id();
			this.modulePdv = origin.modulePdv().name();
			this.status = origin.status().toString();
			this.statusId = origin.status().id();
			this.type = origin.type().toString();
			this.typeId = origin.type().id();
			this.natureId = origin.nature().id();
			this.nature = origin.nature().toString();
			this.originTypeId = origin.originType().id();
			this.originType = origin.originType().toString();			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
    }
}
