package com.recruitmanager.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.recruitmanager.Configures.EncodingCongifures;
import com.recruitmanager.dto.CompanyDto;
import com.recruitmanager.model.Company;
import com.recruitmanager.repository.CompanyRepository;
import com.recruitmanager.utils.ApiUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc @Log4j2 @EncodingCongifures
public class CrawlerApiControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Gson gson;
    @Autowired private CompanyRepository companyRepository;

    @DisplayName("로켓펀치 크롤링 테스트")
    @Test
    void getRocektpunchList_correct() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/rocketpunch")
                .accept(MediaType.APPLICATION_JSON)
                .param("keywords","soomgo")
                .param("keywords","jpa"))
                .andExpect(unauthenticated())
                .andExpect(status().isOk())
                .andReturn();

        log.info("response header names : " + result.getResponse().getHeaderNames());
        log.info("response header values: " + result.getResponse());
        log.info("clist result : "+result.getResponse().getContentAsString());
        log.info("clist result json : " + gson.fromJson(result.getResponse().getContentAsString(),ApiUtils.ApiResult.class));
        ApiUtils.ApiResult<List<CompanyDto>> response = gson.fromJson(result.getResponse().getContentAsString(),ApiUtils.ApiResult.class);
        log.info("clist : "+response.getResponse());
        assertNull(response.getError());

        List<Company> list = companyRepository.findAll();

        assertNotNull(list);
        assertTrue(response.getResponse().size() == list.size());

    }

}
