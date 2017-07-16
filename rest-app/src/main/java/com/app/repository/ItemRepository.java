package com.app.repository;

import com.app.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, Long> {

    Item findFirstByOrderByNumberAsc();

    Item findFirstByOrderByNumberDesc();
}
