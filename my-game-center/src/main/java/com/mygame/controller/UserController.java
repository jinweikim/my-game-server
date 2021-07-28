package com.mygame.controller;

import com.mygame.common.error.GameErrorException;
import com.mygame.common.error.IServerError;
import com.mygame.common.error.TokenException;
import com.mygame.common.utils.RSAUtils;
import com.mygame.dataconfig.GameGatewayInfo;
import com.mygame.db.entity.Player;
import com.mygame.db.entity.UserAccount;
import com.mygame.db.entity.UserAccount.ZonePlayerInfo;
import com.mygame.error.GameCenterError;
import com.mygame.http.request.CreatePlayerParam;
import com.mygame.http.request.LoginParam;
import com.mygame.http.MessageCode;
import com.mygame.http.request.SelectGameGatewayParam;
import com.mygame.http.response.GameGatewayInfoMsg;
import com.mygame.http.response.LoginResult;
import com.mygame.http.response.ResponseEntity;
import com.mygame.service.GameGatewayService;
import com.mygame.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mygame.service.UserLoginService;
import com.mygame.common.utils.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/request")
public class UserController {
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private GameGatewayService gameGatewayService;
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping(MessageCode.USER_LOGIN)
    public ResponseEntity<LoginResult> login (@RequestBody LoginParam loginParam) {
        loginParam.checkParam(); // 检测请求参数的合法性
        IServerError serverError = userLoginService.verifySdkToken(loginParam.getOpenId(), loginParam.getToken());
        if (serverError != null) { // 请求第三方，验证登录信息的正确性
            throw GameErrorException.newBuilder(serverError).build();
        }
        UserAccount userAccount = userLoginService.login(loginParam);
        LoginResult loginResult = new LoginResult();
        loginResult.setUserId(userAccount.getUserId());
        String token = JWTUtil.getUserToken(userAccount.getOpenId(), userAccount.getUserId());
        loginResult.setToken(token); // 使用 JWT 生成 token
        logger.info("user {} 登录成功", loginParam.getOpenId());
//        System.out.println("登录成功");
        return new ResponseEntity<LoginResult>(loginResult);
    }

    @PostMapping(MessageCode.CREATE_PLAYER)
    public ResponseEntity<ZonePlayerInfo> createPlayer(@RequestBody CreatePlayerParam param, HttpServletRequest request) {
        param.checkParam();
        String token = request.getHeader("token");
        logger.info(token);
        if (token == null)  {
            throw GameErrorException.newBuilder(GameCenterError.TOKEN_FAILED).build();
        }
        JWTUtil.TokenBody tokenBody;
        try{
            tokenBody = JWTUtil.getTokenBody(token);
        } catch (TokenException e) {
            throw GameErrorException.newBuilder(GameCenterError.TOKEN_FAILED).build();
        }
        String openId = tokenBody.getOpenId();
        UserAccount userAccount = userLoginService.getUserAccountByOpenId(openId).get();
        String zoneId = param.getZoneId();
        ZonePlayerInfo zonePlayerInfo = userAccount.getZonePlayerInfo().get(zoneId);
        System.out.println(zonePlayerInfo);
        // 一个区只能有一个 player
        logger.info("区号", zoneId);
        if (zonePlayerInfo == null) {
            logger.info("该账号在该大区 {} 没有角色", zoneId);
            Player player = playerService.createPlayer(param.getZoneId(), param.getNickName());
            zonePlayerInfo = new ZonePlayerInfo(player.getPlayerId(), System.currentTimeMillis());
            userAccount.getZonePlayerInfo().put(zoneId, zonePlayerInfo);
            userLoginService.updateUserAccount(userAccount);
        }
        ResponseEntity<ZonePlayerInfo> response = new ResponseEntity<ZonePlayerInfo>(zonePlayerInfo);
        return response;
    }

    @PostMapping(MessageCode.SELECT_GAME_GATEWAY)
    public Object selectGameGateway(@RequestBody SelectGameGatewayParam param) throws Exception {
        param.checkParam();
        long playerId = param.getPlayerId();
        GameGatewayInfo gameGatewayInfo = gameGatewayService.getGameGatewayInfo(playerId);
        GameGatewayInfoMsg gameGatewayInfoMsg = new GameGatewayInfoMsg(gameGatewayInfo.getId(), gameGatewayInfo.getIp(), gameGatewayInfo.getPort());
        Map<String, Object> keyPair = RSAUtils.genKeyPair();// 生成rsa的公钥和私钥
        byte[] publickKeyBytes = RSAUtils.getPublicKey(keyPair);// 获取公钥
        String publickKey = Base64Utils.encodeToString(publickKeyBytes);// 为了方便传输，对bytes数组进行一下base64编码
        String token = playerService.createToken(param, gameGatewayInfo.getIp(), publickKey);// 根据这些参数生成token
        gameGatewayInfoMsg.setToken(token);
        byte[] privateKeyBytes = RSAUtils.getPrivateKey(keyPair);
        String privateKey = Base64Utils.encodeToString(privateKeyBytes);
        gameGatewayInfoMsg.setRsaPrivateKey(privateKey);// 给客户端返回私钥
        logger.info("player {} 获取游戏网关信息成功：{}", playerId, gameGatewayInfoMsg);
        ResponseEntity<GameGatewayInfoMsg> responseEntity = new ResponseEntity<>(gameGatewayInfoMsg);
        return responseEntity;
    }
}
