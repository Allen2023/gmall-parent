package com.atguigu.gmall.model.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/6/1 19:54
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserIdTo {
    private Long userId;
    private String userTempId;
}
