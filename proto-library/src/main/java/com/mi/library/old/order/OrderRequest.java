package com.mi.library.old.order;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private String account_ID;
    private String figi;
    private Long lots;
    private int direction;
    private int type;


}
