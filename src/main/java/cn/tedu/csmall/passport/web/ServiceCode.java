package cn.tedu.csmall.passport.web;

/**
 * 业务状态码枚举类型
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
public enum ServiceCode {

    OK(20000),
    ERR_BAD_REQUEST(40000),
    ERR_UNAUTHORIZED(40100),
    ERR_UNAUTHORIZED_DISABLED(40110),
    ERR_FORBIDDEN(40300),
    ERR_NOT_FOUND(40400),
    ERR_CONFLICT(40900),
    ERR_INSERT(50000),
    ERR_DELETE(50100),
    ERR_UPDATE(50200),
    ERR_SELECT(50300),
    ERR_JWT_EXPIRED(60000),
    ERR_JWT_MALFORMED(60100),
    ERR_JWT_SIGNATURE(60200),
    ERR_UNKNOWN(99999);

    private Integer value;

    ServiceCode(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}