package com.recruitmanager.crawler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.recruitmanager.model.Company;
import com.recruitmanager.model.Job;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class GsonTest {


    @DisplayName("gson 테스트")
    @Test
    void gsonTest() throws JsonProcessingException {

        Company company = new Company();

        company.setId(1L);
        company.setName("이름");
        company.setLink("링크");
        company.setLogo("로고");

        Job job = new Job();
        job.setName("잡 이름");
        job.setDate("잡 데이트");
        job.setStat("잡 스텟");
        job.setLink("링크");

        log.info("company : " + company);
        log.info("job : " + job);

        List<Job> jobs = new ArrayList<>();
        jobs.add(job);
        jobs.add(job);

        company.setJobs(jobs);
        //job.setCompany(company);

        log.info("company : " + company);
        log.info("job : " + job);

        Gson gson = new Gson();

        String json_comapny = gson.toJson(company);
        String json_job = gson.toJson(job);

        log.info(json_comapny);
        log.info(json_job);


        Company jsonToCompany = gson.fromJson("{\"name\":\"이름\",\"link\":\"링크\",\"id\":\"1\",\"logo\":\"로고\"}",Company.class);
        Job jsonToJob = gson.fromJson("{\"id\":1,\"name\":\"이름\",\"date\":\"데이트\",\"stat\":\"스탯\",\"link\":\"링크\"}",Job.class);

        log.info("json to company : "+jsonToJob);
        log.info("json to job : "+jsonToJob);

        Company jsonToCompany2 = gson.fromJson("{\"name\":\"이름\",\"link\":\"링크\",\"id\":\"1\",\"logo\":\"로고\"" +
                                    ",\"jobs\":[{\"id\":1,\"name\":\"이름\",\"date\":\"데이트\",\"stat\":\"스탯\",\"link\":\"링크\"}" +
                                    ",{\"id\":2,\"name\":\"이름\",\"date\":\"데이트\",\"stat\":\"스탯\",\"link\":\"링크\"}]}",Company.class);

        log.info("json to company2 : "+jsonToCompany2);

        List<Company> clist = gson.fromJson("[{\"name\":\"이름\",\"link\":\"링크\",\"id\":\"1\",\"logo\":\"로고\"" +
                ",\"jobs\":[{\"id\":1,\"name\":\"이름\",\"date\":\"데이트\",\"stat\":\"스탯\",\"link\":\"링크\"}" +
                ",{\"id\":2,\"name\":\"이름\",\"date\":\"데이트\",\"stat\":\"스탯\",\"link\":\"링크\"}]}]",ArrayList.class);

        log.info("company list : " + clist);

    }

}
