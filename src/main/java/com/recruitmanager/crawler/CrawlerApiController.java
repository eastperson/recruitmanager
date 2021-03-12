package com.recruitmanager.crawler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recruitmanager.dto.CompanyDto;
import com.recruitmanager.model.Company;
import com.recruitmanager.model.Job;
import com.recruitmanager.service.CompanyService;
import com.recruitmanager.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class CrawlerApiController {

    private final CrawlerService crawlerService;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;

    @GetMapping("/rocketpunch")
    public ApiUtils.ApiResult<List<CompanyDto>> getRocketpunch(String[] keywords, Model model) throws JsonProcessingException {

        log.info("keywords : " + Arrays.toString(keywords));

        List<CompanyDto> clist = (ArrayList<CompanyDto>) crawlerService.getCompanyList(keywords);

        List<Company> companies = new ArrayList<>();

        clist.stream().forEach(dto -> {
            List<Job> jobList = new ArrayList<>();
            dto.getJobs().stream().forEach(job -> {
                jobList.add(modelMapper.map(job,Job.class));
            });
            Company com = modelMapper.map(dto,Company.class);
            com.setJobList(jobList);
            companies.add(com);
        });

        companyService.insertAll(companies);

        return ApiUtils.success(clist);
    }

}
