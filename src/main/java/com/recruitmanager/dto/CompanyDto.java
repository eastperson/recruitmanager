package com.recruitmanager.dto;

import com.recruitmanager.model.Company;
import com.recruitmanager.model.Job;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class CompanyDto {

    private String name;

    private String logo;

    private String link;

    private String detail;

    private List<JobDto> jobs;

    public CompanyDto(Company source){

        copyProperties(source,this);

        List<JobDto> jobs = new ArrayList<>();

        source.getJobList().forEach(job -> {
            jobs.add(new JobDto(job));
        });

        this.setJobs(jobs);
    }
}
