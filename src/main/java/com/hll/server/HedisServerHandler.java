package com.hll.server;

import com.hll.client.DefaultRedisConnectionCallback;
import com.hll.client.RedisClient;
import com.hll.client.RedisConnectionCallBack;
import com.hll.command.ErrorReply;
import com.hll.command.RequestCommand;
import com.hll.protocal.ProtoUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by hll on 2016/4/29.
 */
public class HedisServerHandler extends SimpleChannelInboundHandler<RequestCommand> {

  private Logger logger = LoggerFactory.getLogger(HedisServerHandler.class);

  private RedisClient redisClient;

  public HedisServerHandler(RedisClient redisClient) {
    this.redisClient = redisClient;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RequestCommand request) throws Exception {
    Channel channel = ctx.channel();
    RedisConnectionCallBack callback = new DefaultRedisConnectionCallback(channel);
    request.setCallback(callback);
    redisClient.getRedisConnection().write(request);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    logger.info(ctx.channel() + " had closed!!!");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    if (cause instanceof IOException){
      String message = cause.getMessage();
      if (message!=null && "Connection reset by peer".equals(message)){
        logger.warn("Client closed!",cause);
      }else {
        logger.error("出错，关闭连接",cause);
      }
      ctx.channel().close();
    }else {
      logger.error("出错，关闭连接",cause);
      ctx.channel().write(new ErrorReply(ProtoUtil.buildErrorReplyBytes("closed by upstream")));
    }
  }
}
