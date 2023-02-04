package cn.tedu.csmall.passport.web;

import cn.tedu.csmall.passport.ex.ServiceException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一的响应结果类型
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Data
public class JsonResult<T> implements Serializable {

    /**
     * 操作结果的状态码
     */
    @ApiModelProperty("业务状态码")
    private Integer state;
    /**
     * 操作“失败”时响应的提示文本
     */
    @ApiModelProperty("消息")
    private String message;
    /**
     * 操作“成功”时响应的数据
     */
    @ApiModelProperty("数据")
    private T data; // T=Type, E=Element, K=Key, V=Value

    public static JsonResult<Void> ok() {
        return ok(null);
    }

    public static <T> JsonResult<T> ok(T data) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setState(ServiceCode.OK.getValue());
        jsonResult.setData(data);
        return jsonResult;
    }

    public static JsonResult<Void> fail(ServiceException e) {
        // JsonResult jsonResult = new JsonResult();
        // jsonResult.setState(e.getServiceCode().getValue());
        // jsonResult.setMessage(e.getMessage());
        // return jsonResult;
        return fail(e.getServiceCode(), e.getMessage());
    }

    public static JsonResult<Void> fail(ServiceCode serviceCode, String message) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setState(serviceCode.getValue());
        jsonResult.setMessage(message);
        return jsonResult;
    }

}
