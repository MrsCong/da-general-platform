package com.dgp.core.http.request;

import lombok.Data;

@Data
public class IdRequest<T> {

    //主键
    T id;

}