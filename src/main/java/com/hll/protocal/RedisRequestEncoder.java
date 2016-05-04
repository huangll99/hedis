package com.hll.protocal;

import com.hll.command.RequestCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by hll on 2016/5/3.
 */
public class RedisRequestEncoder extends MessageToByteEncoder<RequestCommand> {
  @Override
  protected void encode(ChannelHandlerContext ctx, RequestCommand msg, ByteBuf out) throws Exception {
    msg.encode(out);
    ctx.writeAndFlush(out);//必须要有这一步吗----holly shit,必须要有
    out.release();
  }
}
