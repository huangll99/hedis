package com.hll.protocal;

import com.hll.command.RedisReply;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by hll on 2016/5/3.
 */
public class RedisReplyEncoder extends MessageToByteEncoder<RedisReply> {

  @Override
  protected void encode(ChannelHandlerContext ctx, RedisReply msg, ByteBuf out) throws Exception {
    msg.encode(out);
    ctx.writeAndFlush(out);
    out.release();
  }
}
