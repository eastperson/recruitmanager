package com.recruitmanager.service;

import com.recruitmanager.model.Company;
import com.recruitmanager.model.Job;
import com.recruitmanager.repository.CompanyRepository;
import com.recruitmanager.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;

    @Transactional
    public Long insertAll(List<Company> clist) {

        clist.stream().forEach(company -> {
            log.info("company : "+company);
            companyRepository.save(company);
            company.getJobList().stream().forEach(job -> {
                job.setCompany(company);
                log.info("job : " + job);
                jobRepository.save(job);
            });
        });

        return Long.valueOf(clist.size());
    }

}
