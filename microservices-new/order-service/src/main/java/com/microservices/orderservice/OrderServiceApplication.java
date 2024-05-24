package com.microservices.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
