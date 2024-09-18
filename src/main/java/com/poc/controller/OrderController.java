package com.poc.controller;

import com.poc.entity.Order;
import com.poc.entity.OrderRequest;
import com.poc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<String> saveOrder(@RequestBody OrderRequest orderRequest) {

        Order order = new Order();
        order.setUniqueId(orderRequest.getUniqueId());
        order.setOrderNumber(orderRequest.getOrderNumber());
        order.setStatusCode(orderRequest.getStatusCode());
        order.setStatusDescription(orderRequest.getStatusDescription());
        orderService.saveOrder(order);

        try {
            String filePath = "D:/test/order_" + orderRequest.getOrderNumber() + ".pdf";
            orderService.savePdfToFile(orderRequest.getEncodedPdf(), filePath);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to save PDF", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Order saved successfully", HttpStatus.OK);
    }
}
