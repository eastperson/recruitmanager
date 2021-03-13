package com.recruitmanager.controller;

import com.recruitmanager.handler.RestTemplateResponseErrorHandler;
import com.recruitmanager.model.Company;
import com.recruitmanager.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {

    private final CompanyRepository companyRepository;

    @GetMapping
    public String main(Model model){

        List<Company> clist = companyRepository.findAll();

        model.addAttribute("clist",clist);

        return "index";
    }

    @GetMapping("/rocketpunch")
    public String rocketpunch(String[] keywords, RedirectAttributes redirectAttributes){

        log.info("start===================================================================");

        log.info("keywords : " + Arrays.toString(keywords));

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        RestTemplateResponseErrorHandler erroHandler = new RestTemplateResponseErrorHandler();

        restTemplate.setErrorHandler(erroHandler);

        String url = "http://localhost:8080/api/rocketpunch";

        UriComponents uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("keywords",keywords)
                .build();

        HttpHeaders headers = new HttpHeaders();

        RequestEntity<String> rq = new RequestEntity<>(headers, HttpMethod.GET, uri.toUri());

        String getJson = restTemplate.getForObject(uri.toUri(),String.class);

        redirectAttributes.addFlashAttribute("message","크롤링이 실패했습니다.");

        if(getJson != null) {
            redirectAttributes.addFlashAttribute("message","크롤링이 완료되었습니다.");
        }

        log.info("get json : "+getJson);

        log.info("end===================================================================");

        return "redirect:/";
    }

}
