package com.hll.command;

import io.netty.buffer.ByteBuf;

/**
 * Created by hll on 2016/5/3.
 */
public interface RedisReply {
  enum Type {

    ERROR((byte) '-'),
    STATUS((byte) '+'),
    BULK((byte) '$'),
    INTEGER((byte) ':'),
    ARRAY((byte) '*'),;

    private byte code;

    Type(byte code) {
      this.code = code;
    }

    public byte getCode() {
      return this.code;
    }
  }

  Type getType();

  void setType(Type type);

  void encode(ByteBuf out);
}
