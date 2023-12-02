package com.example.cooing.domain.auth.res;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class PostLoginRes {
    private String accessToken;


}
