package com.recruitmanager.controller;

import com.recruitmanager.dto.CompanyDto;
import com.recruitmanager.dto.page.PageRequestDTO;
import com.recruitmanager.dto.page.PageResultDTO;
import com.recruitmanager.handler.RestTemplateResponseErrorHandler;
import com.recruitmanager.model.Company;
import com.recruitmanager.repository.CompanyRepository;
import com.recruitmanager.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {

    private final CompanyRepository companyRepository;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;

    @GetMapping
    public String main(Model model, Integer page, Integer size, String type, String keyword){

        if(type == null) type = "C";
        if(page == null || page <= 0) page = 1;
        if(size == null || size <= 0) size = 10;
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().size(size).page(page).type(type).keyword(keyword).build();

        PageResultDTO<CompanyDto,Company> pageResultDTO = companyService.getList(pageRequestDTO);

        List<CompanyDto> companyDtoList = new ArrayList<>();

        for(CompanyDto companyDto : pageResultDTO.getDtoList()){
            companyDtoList.add(companyDto);
        }

        //List<Company> clist = companyRepository.findAll();

        model.addAttribute("companyDtoList",companyDtoList);
        model.addAttribute(pageResultDTO);
        model.addAttribute(pageRequestDTO);

        return "index";
    }

    @GetMapping("/rocketpunch")
    public String rocketpunch(String[] keywords, RedirectAttributes redirectAttributes){

        log.info("start===================================================================");

        log.info("keywords : " + Arrays.toString(keywords));

        String getJson = companyService.getRocketpunchJson(keywords);

        redirectAttributes.addFlashAttribute("message","크롤링이 실패했습니다.");

        if(getJson != null) {
            redirectAttributes.addFlashAttribute("message","크롤링이 완료되었습니다.");
            redirectAttributes.addFlashAttribute("keyworeds",keywords);
        }

        log.info("get json : "+getJson);

        log.info("end===================================================================");

        return "redirect:/";
    }



}
