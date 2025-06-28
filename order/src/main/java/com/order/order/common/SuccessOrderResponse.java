package com.order.order.common;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.order.order.dto.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Data
public class SuccessOrderResponse implements OrderResponse {
    @JsonUnwrapped
    private OrderDTO orderDTO;

}
