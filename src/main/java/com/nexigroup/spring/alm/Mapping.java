package com.nexigroup.spring.alm;

import java.util.HashMap;
import java.util.Map;

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
public class Mapping {
	
	private MappingField origin;
	private MappingField target;
	private String transformationId;
	private Map<String, String> configuration = new HashMap<>();
	
	
	public Mapping(MappingField origin) {
		this.origin = origin;
	}
	

	
//	private String transformationId;
	

}
