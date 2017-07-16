package com.app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Item {

    @Id
    private Long number;
    private String createdDate;
}
