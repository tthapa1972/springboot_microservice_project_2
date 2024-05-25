package com.microservices.orderservice.service;

import com.microservices.orderservice.dto.InventoryResponse;
import com.microservices.orderservice.dto.OrderLineItemsDto;
import com.microservices.orderservice.dto.OrderRequest;
import com.microservices.orderservice.model.Order;
import com.microservices.orderservice.model.OrderLineItems;
import com.microservices.orderservice.repository.OrderRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final WebClient.Builder webClientBuilder;

    private final OrderRepository orderRepository;

    private final Tracer tracer;

    public String placeOrder(OrderRequest orderRequest) {

        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToDto).toList();

        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodes =
                order.getOrderLineItemsList()
                        .stream()
                        .map(OrderLineItems::getSkuCode)
                        .toList();

        Span inventoryServiceLookUp = tracer.nextSpan().name("InventoryServiceLookUp");

        try (Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookUp.start())) {

            InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                    .uri("http://inventory-service:8282/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(inventoryResponse -> inventoryResponse.isInStock());

            log.info("inventoryResponses : " + Arrays.toString(inventoryResponses));

            log.info("allProductsInStock : " + allProductsInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
                return "Order Saved Successfully";
            } else {
                throw new IllegalArgumentException("Inventory is not there");
            }
        } finally {
            inventoryServiceLookUp.end();
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;
    }
}
