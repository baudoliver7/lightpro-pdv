package com.lightpro.pdv.vm;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonGetter;

public class ResumeSalesVm {
	
	private final transient double turnover;
	private final transient String pdv;
	
	public ResumeSalesVm(){
		throw new UnsupportedOperationException("#ResumeSalesVm()");
	}
	
	public ResumeSalesVm(final String pdv, final double turnover) {
		
        this.turnover = turnover;
        this.pdv = pdv;
    }
	
	@JsonGetter
	public String pdv(){
		return pdv;
	}
	
	@JsonGetter
	public double turnover() throws IOException {
		return turnover;
	}
}
