package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

/**
 * 请求方向
 */
@Getter
public enum RequestType {
    CLIENT_RESPONSE_SERVER(0,0), //客户机对服务器上报的响应
    CLIENT_REQUEST(0,1), //客户机发起的请求
    SERVER_REQUEST(1,0), // 服务器发起的请求
    SERVER_RESPONSE_CLIENT(1,1); // 服务器对客户机请求的响应
    private final int dir;
    private final int prm;
    private RequestType(int dir, int prm) {
        this.dir = dir;
        this.prm = prm;
    }
    public static RequestType getRequestType(int dir, int prm) {
        for(RequestType dirAndPRM : RequestType.values()) {
            if(dirAndPRM.dir == dir && prm == dirAndPRM.prm) {
                return dirAndPRM;
            }
        }
        return null;
    }
}
