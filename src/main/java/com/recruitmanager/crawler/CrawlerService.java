package com.recruitmanager.crawler;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.recruitmanager.dto.CompanyDto;
import com.recruitmanager.handler.RestTemplateResponseErrorHandler;
import com.recruitmanager.model.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.parser.AcceptLanguage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service @Log4j2
@RequiredArgsConstructor
public class CrawlerService {

    private final ObjectMapper objectMapper;

    private final Gson gson;

    public List<CompanyDto> getCompanyList(String[] keywords) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        log.info("MessageConverters : "+restTemplate.getMessageConverters());

        RestTemplateResponseErrorHandler erroHandler = new RestTemplateResponseErrorHandler();

        restTemplate.setErrorHandler(erroHandler);

        String url = "http://54.180.98.206:8000/get";

        log.info("=====================================================");
        log.info("url : " + url);

        UriComponents uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("keywords",keywords)
                .build();

        log.info("uri : " + uri);

        HttpHeaders headers = new HttpHeaders();

        List<Charset> charsets = new ArrayList<>();
        charsets.add(StandardCharsets.UTF_8);

        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.TEXT_PLAIN);

        //headers.setContentLanguage(Locale.KOREA);
        //headers.setAcceptCharset(charsets);
        //headers.setAccept(mediaTypes);

        //headers.setContentType(MediaType.valueOf("application/json;charset=EUC-KR"));

        RequestEntity<String> rq = new RequestEntity<>(headers, HttpMethod.GET, uri.toUri());

        log.info("request : " + rq);

        //ResponseEntity<String> resultStr = restTemplate.exchange(rq, String.class);

        String getJson = restTemplate.getForObject(uri.toUri(),String.class);

        //log.info("result list : "+resultStr.getBody());
        //log.info("response header : "+ resultStr.getHeaders());

        //System.out.println(resultStr.getBody());

        //List<Company> list = gson.fromJson(resultStr.getBody(),ArrayList.class);

        //log.info("list : "+list);

        log.info("get json : " + getJson);

        getJson = getJson.substring(1,getJson.lastIndexOf("\""));

        log.info("get json : " + getJson);

        getJson = getJson.replaceAll("\\\\\"","\"");
        getJson = getJson.replaceAll("\\\\\\\\\\\"","\\\\");
        getJson = getJson.replaceAll("\\\\\\\\","\\\\");
        getJson = getJson.replaceAll("\\\\ ", "");

        log.info("get json : " + getJson);

        Type type = new TypeToken<List<CompanyDto>>() {
        }.getType();

        List<CompanyDto> clist = gson.fromJson(getJson,type);

        log.info("clist : " + clist);

        return clist;
    }
}
