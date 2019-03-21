package com.ochabmateusz.partridge.partridgeimageapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @ToString
public class Tags {

    private String directoryName;
    private String tagName;
    private String tagDescription;

}
