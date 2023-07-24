package com.example.demo.controller.anonymous;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Address;
import com.example.demo.repository.AddressRepository;
import com.example.demo.service.JReportService;

import net.sf.jasperreports.engine.JRException;


@RestController
public class ReportController {
 
    @Autowired
    private AddressRepository repository;
    @Autowired
    private JReportService service;
 
    @GetMapping("/getAddress")
    public List<Address> getAddress() {
        List<Address> address = (List<Address>) repository.findAll();
        return address;
    }
 
         
	@GetMapping(value = "/report", produces = MediaType.APPLICATION_PDF_VALUE)
    public void createPDF(HttpServletResponse response) throws IOException, JRException {
        //response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
  
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        service.exportJasperReport(response);
        
        System.out.println(123);
        System.out.println("modelandview ");


    }
 
    
 
}
