package com.nexigroup.spring.jira;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class IssueField {

    private final String id;
    private final String name;
    private final String type;
    private final Object value;
    
}
