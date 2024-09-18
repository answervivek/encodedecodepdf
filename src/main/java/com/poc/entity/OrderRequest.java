package com.poc.entity;

import lombok.Data;

@Data
public class OrderRequest {
    private String uniqueId;
    private String orderNumber;
    private String statusCode;
    private String statusDescription;
    private String encodedPdf;
}
