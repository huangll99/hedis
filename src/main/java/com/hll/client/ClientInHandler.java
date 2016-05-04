package com.hll.client;

import com.hll.command.RedisReply;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by hll on 2016/5/3.
 */
public class ClientInHandler extends SimpleChannelInboundHandler<RedisReply>{

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RedisReply msg) throws Exception {
    RedisConnectionCallBack callBack = ctx.channel().attr(ClientOutHandler.CALLBACK_KEY).get();
    callBack.handleReply(msg);
    ctx.channel().attr(RedisClient.REDIS_CONNECTION_ATTRIBUTE).get().release();
  }
}
