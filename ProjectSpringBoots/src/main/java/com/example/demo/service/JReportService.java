package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.example.demo.entity.Address;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.model.dto.OrderInfoDto;
import com.example.demo.model.mapper.OrderMapper;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.OrderRepository;
//import com.example.demo.security.CustomUserDetails;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;



@Service
public class JReportService {
 
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AddressRepository repository; 
  
    public void exportJasperReport(HttpServletResponse response) throws JRException, IOException {
//      User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
//      System.out.println(user);
//      List<Order> orders = orderService.getListOrderById(user.getId());
//  	List<OrderInfoDto> orderInfoDtos = new ArrayList<>();
//  	for (Order order: orders) {
//  		
//  		orderInfoDtos.add(OrderMapper.toOrderInfoDto(order));
//  	}
//  	System.out.println(orderInfoDtos.get(0).getTotalPrice());  
  			//Get file and compile it
    	List<Order>orders = orderService.adminGetListOrder();
    	List<OrderInfoDto> rs = new ArrayList<>();
    	for (Order order: orders) {
    		if(order.getStatus()==1) {
        		rs.add(OrderMapper.toOrderInfoDto(order));

    		}
    		
    	}
    	
        File file = ResourceUtils.getFile("classpath:report/Report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rs);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Simplifying Tech");
        //Fill Jasper report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        //Export report
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }
 
//    public void exportJasperReport(HttpServletResponse response) throws JRException, IOException {
//        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
//        System.out.println(user);
//        List<Order> orders = orderService.getListOrderById(user.getId());
//    	List<OrderInfoDto> orderInfoDtos = new ArrayList<>();
//    	for (Order order: orders) {
//    		
//    		orderInfoDtos.add(OrderMapper.toOrderInfoDto(order));
//    	}
//    	System.out.println(orderInfoDtos.get(0).getTotalPrice());  
//    			//Get file and compile it
//        File file = ResourceUtils.getFile("classpath:templates/Orders.jrxml");
//        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orderInfoDtos);
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("createdBy", "Simplifying Tech");
//        //Fill Jasper report
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//        //Export report
//        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
//    }
}