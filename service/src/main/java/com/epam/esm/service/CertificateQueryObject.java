package com.epam.esm.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class CertificateQueryObject {
    private String name;
    @JsonAlias({"tagName"})
    private List<String> tagNames;
    private String description;

    public CertificateQueryObject(String name, List<String> tagName, String description) {
        this.name = name;
        this.tagNames = tagName;
        this.description = description;
    }
}
