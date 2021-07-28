package com.mygame.client.service.handler.codec;

import com.mygame.game.GameMessageService;
import com.mygame.game.common.GameMessagePackage;
import com.mygame.game.common.IGameMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ResponseHandler extends ChannelInboundHandlerAdapter {
    private GameMessageService gameMessageService;

    public ResponseHandler(GameMessageService gameMessageService) {
        this.gameMessageService = gameMessageService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        GameMessagePackage messagePackage = (GameMessagePackage) msg;
        int messageId = messagePackage.getHeader().getMessageId();
        IGameMessage gameMessage = gameMessageService.getResponseInstanceByMessageId(messageId);
        gameMessage.setHeader(messagePackage.getHeader());
        gameMessage.read(messagePackage.getBody());
        ctx.fireChannelRead(gameMessage);
    }
}
