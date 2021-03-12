package com.recruitmanager.dto;

import com.recruitmanager.model.Company;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Data
public class JobDto {

    private String name;

    private String stat;

    private String date;

    private String link;
}
