package com.light.support;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteria {
	private String key;
    private String operation;
    private Object value;
}
