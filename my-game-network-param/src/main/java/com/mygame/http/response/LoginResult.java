package com.mygame.http.response;

import lombok.Data;

@Data
public class LoginResult {
    private long userId;
    private String token;
}
