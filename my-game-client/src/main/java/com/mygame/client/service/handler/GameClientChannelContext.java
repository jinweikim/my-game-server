package com.mygame.client.service.handler;

import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mygame.game.common.IGameMessage;
import com.mygame.game.messagedispatcher.IGameChannelContext;
import io.netty.channel.Channel;

public class GameClientChannelContext implements IGameChannelContext{
    private Channel channel;
    private IGameMessage request;
    private static Logger logger = LoggerFactory.getLogger(GameClientChannelContext.class);
    
    
    public GameClientChannelContext(Channel channel, IGameMessage request) {
        super();
        this.channel = channel;
        this.request = request;
    }

    @Override
    public void sendMessage(IGameMessage gameMessage) {
        if(channel.isActive() && channel.isOpen()) {
            channel.writeAndFlush(gameMessage);
        } else {
            logger.trace("channel {} 已失效，发消息失败",channel.id().asShortText());
        }
    }

    @Override
    public String getRemoteHost() {
        InetSocketAddress ipSocket = (InetSocketAddress)channel.remoteAddress();
        String remoteHost = ipSocket.getAddress().getHostAddress();
        return remoteHost;
    }

    @Override
    public long getPlayerId() {
        return 0;
    }
  
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getRequest() {
        return (T)request;
    }

    public Channel getChannel() {
        return channel;
    }
    

}
