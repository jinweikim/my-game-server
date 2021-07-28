package com.mygame.http.response;

import lombok.Data;

@Data
public class GameGatewayInfoMsg {
    private int id;
    private String ip;
    private int port;
    private String token;//连接此网关认证时需要的token.
    private String rsaPrivateKey;
    public GameGatewayInfoMsg() {}

    public GameGatewayInfoMsg(int id, String ip, int port) {
        super();
        this.id = id;
        this.ip = ip;
        this.port = port;
    }
    @Override
    public String toString() {
        return "GameGatewayInfoMsg [id=" + id + ", ip=" + ip + ", port=" + port + ", token=" + token + "]";
    }

}
