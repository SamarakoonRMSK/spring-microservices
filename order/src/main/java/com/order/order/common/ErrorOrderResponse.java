package com.order.order.common;

import lombok.*;


@AllArgsConstructor
@Data
public class ErrorOrderResponse implements OrderResponse {
    private String errorMessage;
}
