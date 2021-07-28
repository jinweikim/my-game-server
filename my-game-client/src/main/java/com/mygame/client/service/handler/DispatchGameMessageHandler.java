package com.mygame.client.service.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mygame.game.common.IGameMessage;
import com.mygame.game.messagedispatcher.DispatchGameMessageService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
/**
 * 
 * @ClassName: DispatchGameMessageHandler 
 * @Description: 接收服务器响应的消息，并将消息分发到业务处理方法中。
 * @author: wgs
 * @date: 2019年4月19日 下午8:42:57
 */
public class DispatchGameMessageHandler extends ChannelInboundHandlerAdapter {
    private DispatchGameMessageService dispatchGameMessageService;
    private static Logger logger = LoggerFactory.getLogger(DispatchGameMessageHandler.class);
    public DispatchGameMessageHandler(DispatchGameMessageService dispatchGameMessageService) {
        this.dispatchGameMessageService = dispatchGameMessageService;
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("连接断开，channelId:{}",ctx.channel().id().asShortText());
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        IGameMessage gameMessage = (IGameMessage)msg;
        GameClientChannelContext gameClientChannelContext = new GameClientChannelContext(ctx.channel(),gameMessage);//构造消息处理的上下文信息
        dispatchGameMessageService.callMethod(gameMessage, gameClientChannelContext);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       logger.error("服务异常",cause);
    }
}
