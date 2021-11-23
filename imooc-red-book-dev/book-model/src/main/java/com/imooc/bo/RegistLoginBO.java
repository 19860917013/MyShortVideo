package com.imooc.bo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author 包建丰
 * @date 2021/11/23 21 :04
 * @description
 **/

@Data
@ToString
public class RegistLoginBO {

    @NotBlank(message = "手机号不能为空")
    private String mobile;
    @NotBlank(message = "短信验证码不能为空")
    private String verfyCode;

}
