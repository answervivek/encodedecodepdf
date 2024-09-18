package com.poc.service;

import com.poc.entity.Order;
import com.poc.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void savePdfToFile(String encodedPdf, String filePath) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPdf);
        Files.write(Paths.get(filePath), decodedBytes);
    }
}
