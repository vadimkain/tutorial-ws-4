package com.example.tutorialws4.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {
    @Id
    private String nickName;
    private String fullName;
    private Status status;
}
