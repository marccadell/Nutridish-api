package com.nutridish.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonRes<T> {
    private boolean success;
    private int status;
    private String message;
    private T data;
}
