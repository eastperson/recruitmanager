package com.recruitmanager.dto;

import com.recruitmanager.model.Company;
import com.recruitmanager.model.Job;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class JobDto {

    private String name;

    private String stat;

    private String date;

    private String link;

    public JobDto(Job source) {
        copyProperties(source, this);
    }
}
