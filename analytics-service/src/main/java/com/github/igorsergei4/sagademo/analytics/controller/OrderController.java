package com.github.igorsergei4.sagademo.analytics.controller;

import com.github.igorsergei4.sagademo.analytics.dto.OrderInfoDto;
import com.github.igorsergei4.sagademo.analytics.dto.ResponsePage;
import com.github.igorsergei4.sagademo.analytics.service.entity.OrderService;
import com.github.igorsergei4.sagademo.order.dto.RegisterOrderParams;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final String CREATE_ORDER_API = "/api/orders";

    private final RestClient registerOrderRestClient;

    private final OrderService orderService;

    public OrderController(
            @Value("${url.order-service}") String orderServiceUrl,
            OrderService orderService
    ) {
        this.registerOrderRestClient = RestClient.create(orderServiceUrl + CREATE_ORDER_API);
        this.orderService = orderService;
    }

    @GetMapping
    @Validated
    public ResponsePage<OrderInfoDto> getOrdersInfo(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Optional<LocalDateTime> intervalStart,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Optional<LocalDateTime> intervalEnd,
            Optional<String> orderStatus,
            Optional<String> executionStatus,
            Optional<String> paymentStatus,
            @Min(1)
            @RequestParam(required = false, defaultValue = "1")
            Integer page,
            @Min(1)
            @Max(100)
            @RequestParam(required = true)
            int pageSize
    ) {
        return orderService.getOrdersInfo(
                intervalStart,
                intervalEnd,
                orderStatus,
                executionStatus,
                paymentStatus,
                page,
                pageSize
        );
    }

    @PostMapping
    public ResponseEntity<String> registerOrder(@RequestBody RegisterOrderParams registerOrderParams) {
        ResponseEntity<String> responseEntity = registerOrderRestClient.post().body(registerOrderParams).retrieve().onStatus(
                status -> true,
                (request, response) -> {
                    // every response is meant to be resent
                }
        ).toEntity(String.class);

        HttpHeaders newResponseHeaders = new HttpHeaders();
        MediaType originalContentType = responseEntity.getHeaders().getContentType();
        if (originalContentType != null) {
            newResponseHeaders.set(HttpHeaders.CONTENT_TYPE, responseEntity.getHeaders().getContentType().toString());
        }

        return new ResponseEntity<>(
                responseEntity.getBody(),
                newResponseHeaders,
                responseEntity.getStatusCode()
        );
    }
}
