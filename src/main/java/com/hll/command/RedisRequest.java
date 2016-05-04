package com.hll.command;

import io.netty.buffer.ByteBuf;

/**
 * Created by hll on 2016/5/3.
 */
public interface RedisRequest {

  void encode(ByteBuf byteBuf);
}
