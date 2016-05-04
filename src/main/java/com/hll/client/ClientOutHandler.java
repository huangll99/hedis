package com.hll.client;

import com.hll.command.RequestCommand;
import io.netty.channel.*;
import io.netty.util.AttributeKey;

/**
 * Created by hll on 2016/5/3.
 */
public class ClientOutHandler extends ChannelOutboundHandlerAdapter {

  public static final AttributeKey<RedisConnectionCallBack> CALLBACK_KEY = AttributeKey.newInstance("CALLBACK_KEY");

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    if (msg instanceof RequestCommand) {
      ctx.channel().attr(CALLBACK_KEY).set(((RequestCommand) msg).getRedisConnectionCallBack());
    }
    super.write(ctx, msg, promise);
  }


}
