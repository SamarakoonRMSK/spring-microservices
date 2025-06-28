package com.order.order.service;

import com.inventory.inventory.dto.InventoryDTO;
import com.order.order.common.ErrorOrderResponse;
import com.order.order.common.OrderResponse;
import com.order.order.common.SuccessOrderResponse;
import com.order.order.dto.OrderDTO;
import com.order.order.model.Orders;
import com.order.order.repo.OrderRepo;
import com.product.product.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final WebClient inventoryWebClient;
    private final WebClient productWebClient;
    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;

    public List<OrderDTO> getAllOrders() {
        List<Orders>orderList = orderRepo.findAll();
        return modelMapper.map(orderList, new TypeToken<List<OrderDTO>>(){}.getType());
    }

    public OrderResponse saveOrder(OrderDTO OrderDTO) {

        Integer itemId = OrderDTO.getItemId();

        try {
            InventoryDTO inventoryResponse = inventoryWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/v1/item/{itemId}").build(itemId))
                    .retrieve()
                    .bodyToMono(InventoryDTO.class)
                    .block();

            assert inventoryResponse != null;

            Integer productId = inventoryResponse.getProductId();
            ProductDTO productResponse = productWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/v1/product/{productId}").build(productId))
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .block();

            assert productResponse != null;

            if(inventoryResponse.getQuantity() > 0){
                if(productResponse.getForSale() == 1){
                    orderRepo.save(modelMapper.map(OrderDTO, Orders.class));
                    return new SuccessOrderResponse(OrderDTO);
                }else{
                    return new ErrorOrderResponse("This item is not for sale");
                }

            }else{
                return new ErrorOrderResponse("Item not available, please try later");
            }
        }catch (WebClientResponseException e) {
            if(e.getStatusCode().is5xxServerError()){
                return new ErrorOrderResponse("Item not found");
            }
        }

        return null;
    }

    public OrderDTO updateOrder(OrderDTO OrderDTO) {
        orderRepo.save(modelMapper.map(OrderDTO, Orders.class));
        return OrderDTO;
    }

    public String deleteOrder(Integer orderId) {
        orderRepo.deleteById(orderId);
        return "Order deleted";
    }

    public OrderDTO getOrderById(Integer orderId) {
        Orders order = orderRepo.getOrderById(orderId);
        return modelMapper.map(order, OrderDTO.class);
    }
}
