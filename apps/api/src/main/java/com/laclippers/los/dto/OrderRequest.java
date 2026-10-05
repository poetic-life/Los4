package com.laclippers.los.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String name;
    private String phone;
    private String address;
    private String paymentMethod;
    private List<Item> items;

    @Data
    public static class Item {
        private Long productId;
        private Integer quantity;
    }
}