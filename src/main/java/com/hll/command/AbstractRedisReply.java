package com.hll.command;

import com.hll.protocal.RedisReplyDecoder;
import io.netty.buffer.ByteBuf;

/**
 * Created by hll on 2016/5/3.
 */
abstract public class AbstractRedisReply implements RedisReply {

  private Type type;

  public AbstractRedisReply() {
  }

  public AbstractRedisReply(Type type) {
    this.type = type;
  }

  @Override
  public Type getType() {
    return this.type;
  }

  @Override
  public void setType(Type type) {
    this.type = type;
  }

  public void writeCRLF(ByteBuf byteBuf){
    byteBuf.writeByte(RedisReplyDecoder.CR_BYTE);
    byteBuf.writeByte(RedisReplyDecoder.LF_BYTE);
  }

  public void writeStart(ByteBuf byteBuf){
    byteBuf.writeByte(type.getCode());
  }

  @Override
  public void encode(ByteBuf buffer) {
    writeStart(buffer);
    doEncode(buffer);
  }

  public abstract void doEncode(ByteBuf out);
}
