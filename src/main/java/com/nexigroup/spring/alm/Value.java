package com.nexigroup.spring.alm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Value {
	private String value;
    private String alias;
    private String referenceValue;
    
	public Value(String value) {
		this.value = value;
	}
    
    
}
