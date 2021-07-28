package com.mygame.client.service;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import com.mygame.common.utils.CommonField;
import com.mygame.common.utils.GameHttpClient;
import com.mygame.game.messagedispatcher.DispatchGameMessageService;
import com.mygame.http.MessageCode;
import com.mygame.http.request.SelectGameGatewayParam;
import com.mygame.http.response.GameGatewayInfoMsg;
import com.mygame.http.response.ResponseEntity;

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GameClientInitService {
    private Logger logger = LoggerFactory.getLogger(GameClientInitService.class);
    @Autowired
    private GameClientConfig gameClientConfig;
    @Autowired
    private ApplicationContext applicationContext;
    @PostConstruct
    public void init() {
        DispatchGameMessageService.scanGameMessages(applicationContext, 0, "com.mygame");// 扫描加载要处理的消息类型
        this.selectGateway();
    }
    private void selectGateway() {
        if (gameClientConfig.isUseGameCenter()) {
            // 因为是测试环境，这里使用一些默认参数
            SelectGameGatewayParam param = new SelectGameGatewayParam();
            param.setOpenId("test_openId");
            param.setPlayerId(1);
            param.setUserId(1);
            param.setZoneId("1");
            GameGatewayInfoMsg gateGatewayMsg = this.selectGatewayInfoFromGameCenter(param);
            // 替换默认的游戏网关信息
            if (gateGatewayMsg != null) {
                gameClientConfig.setDefaultGameGatewayHost(gateGatewayMsg.getIp());
                gameClientConfig.setDefaultGameGatewayPort(gateGatewayMsg.getPort());
                gameClientConfig.setGatewayToken(gateGatewayMsg.getToken());
                gameClientConfig.setRsaPrivateKey(gateGatewayMsg.getRsaPrivateKey());
            } else {
                throw new IllegalArgumentException("从服务中心获取游戏网关信息失败，没有可使用的游戏网关信息");
            }
        }
    }


    /**
     * Description: 从游戏服务中心获取游戏网关信息
     */
    public GameGatewayInfoMsg selectGatewayInfoFromGameCenter(SelectGameGatewayParam selectGameGatewayParam) {
        String uri = gameClientConfig.getGameCenterUrl() + CommonField.GAME_CENTER_PATH + MessageCode.SELECT_GAME_GATEWAY;
        String response = GameHttpClient.post(uri, selectGameGatewayParam);
        if (response == null) {
            logger.warn("从游戏服务中心[{}]获取游戏网关信息失败", uri);
            return null;
        }
        ResponseEntity<GameGatewayInfoMsg> responseEntity = ResponseEntity.parseObject(response, GameGatewayInfoMsg.class);
        GameGatewayInfoMsg gateGatewayMsg = responseEntity.getData();
        return gateGatewayMsg;
    }

}
