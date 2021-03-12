package com.recruitmanager.crawler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recruitmanager.model.Company;
import com.recruitmanager.model.Job;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@Transactional
class CrawlerServiceTest {

    @Autowired
    private CrawlerService crawlerService;

    @DisplayName("크롤러 uri 테스트")
    @Test
    void uriTest() throws JsonProcessingException {

        String[] arr = new String[2];

        arr[0] = "soomgo";
        arr[1] = "jpa";

        crawlerService.getCompanyList(arr);
    }

}