package com.hll.command;

import io.netty.buffer.ByteBuf;

/**
 * Created by hll on 2016/5/3.
 */
public class IntegerReply extends CommonRedisReply {

  public IntegerReply(byte[] value) {
    this();
    this.value=value;
  }

  public IntegerReply() {
    super(Type.INTEGER);
  }

  @Override
  public void doEncode(ByteBuf buffer) {
    buffer.writeBytes(value);
    writeCRLF(buffer);
  }
}
