package com.recruitmanager.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.recruitmanager.dto.CompanyDto;
import com.recruitmanager.dto.page.PageRequestDTO;
import com.recruitmanager.dto.page.PageResultDTO;
import com.recruitmanager.handler.RestTemplateResponseErrorHandler;
import com.recruitmanager.model.Company;
import com.recruitmanager.model.Job;
import com.recruitmanager.model.QCompany;
import com.recruitmanager.repository.CompanyRepository;
import com.recruitmanager.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;

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

    public PageResultDTO<CompanyDto,Company> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("id").ascending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO);

        Page<Company> result = companyRepository.findAll(booleanBuilder,pageable);

        Function<Company, CompanyDto> fn = (entity->entity.entityToDto());

        return new PageResultDTO<>(result,fn);

    }

    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {

        String type = requestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QCompany qCompany = QCompany.company;
        String keyword = requestDTO.getKeyword();
        BooleanExpression expression = qCompany.id.gt(0L);

        booleanBuilder.and(expression);

        if(keyword == null || keyword.trim().length() == 0) return booleanBuilder;

        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.equals("C")){

            conditionBuilder.or(qCompany.name.contains(keyword));

            conditionBuilder.or(qCompany.detail.contains(keyword));
        }

        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }

    public String getRocketpunchJson(String[] keywords) {

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        RestTemplateResponseErrorHandler erroHandler = new RestTemplateResponseErrorHandler();

        restTemplate.setErrorHandler(erroHandler);

        String url = "http://localhost:8080/api/rocketpunch";

        UriComponents uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("keywords", keywords)
                .build();

        HttpHeaders headers = new HttpHeaders();

        RequestEntity<String> rq = new RequestEntity<>(headers, HttpMethod.GET, uri.toUri());

        String getJson = restTemplate.getForObject(uri.toUri(),String.class);

        return getJson;
    }

}
