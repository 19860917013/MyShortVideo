package com.imooc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 包建丰
 * @date 2021/11/25 19 :19
 * @description
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FansVO {
    private String fanId;
    private String nickname;
    private String face;
    private boolean isFriend = false;
}

