package com.app.service;

import com.app.exception.ItemNotFoundException;
import com.app.exception.NumberAlreadyExistException;
import com.app.model.Item;
import com.app.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody Item item) {
        log.info("save is requested with number : " + item.getNumber());
        if (itemRepository.exists(item.getNumber())) {
            log.error(item.getNumber() + " already exist!");
            throw new NumberAlreadyExistException(item.getNumber());
        }
        saveInternal(item);
        return ResponseEntity.ok(item);
    }

    @RequestMapping(value = "retrieve/{number}", method = RequestMethod.GET)
    public Item retrieveItemByNumber(@PathVariable Long number) {
        log.info("retrieveItemByNumber is requested with number : " + number);
        final Optional<Item> item = Optional.ofNullable(itemRepository.findOne(number));
        return item.orElseThrow(() -> new ItemNotFoundException(number));
    }

    @RequestMapping(value = "retrieveMaxNumber", method = RequestMethod.GET)
    public Item retrieveMaxNumber() {
        log.info("retrieveItemByNumber is requested");
        final Optional<Item> item = Optional.ofNullable(itemRepository.findFirstByOrderByNumberDesc());
        return item.orElseThrow(() -> new ItemNotFoundException());
    }

    @RequestMapping(value = "retrieveMinNumber", method = RequestMethod.GET)
    public Item retrieveMinNumber() {
        log.info("retrieveItemByNumber is requested");
        final Optional<Item> item = Optional.ofNullable(itemRepository.findFirstByOrderByNumberAsc());
        return item.orElseThrow(() -> new ItemNotFoundException());
    }

    @RequestMapping(value = "deleteOrRetrieve", method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<?> deleteOrRetrieve(@RequestParam(required = false) Long number,
                                              @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        log.info("deleteOrRetrieve is requested with number : " + number + ", direction : " + direction);
        boolean shouldDelete = !Objects.isNull(number);
        if (shouldDelete) {
            deleteBy(number);
            return ResponseEntity.ok().build();
        } else {
            List<Item> items = itemRepository.findAll(new Sort(direction, "number"));
            return ResponseEntity.ok(items);
        }
    }

    private void saveInternal(Item item) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        item.setCreatedDate(formattedDateTime);
        itemRepository.save(item);
    }

    private void deleteBy(Long number) {
        if (!itemRepository.exists(number)) {
            log.error(number + " is not found!");
            throw new ItemNotFoundException();
        }
        itemRepository.delete(number);
    }
}
