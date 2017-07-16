package com.app;

import com.app.model.Item;
import com.app.repository.ItemRepository;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class RestApplication implements CommandLineRunner {

    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Autowired
    private ItemRepository itemRepository;

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public ResponseEntity<?> info() {
        return ResponseEntity.ok("I am the rest app!");
    }

    @Override
    public void run(String... strings) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        Item item1 = new Item();
        item1.setNumber(1L);
        item1.setCreatedDate(formattedDateTime);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setNumber(2L);
        item2.setCreatedDate(formattedDateTime);
        itemRepository.save(item2);
    }
}
