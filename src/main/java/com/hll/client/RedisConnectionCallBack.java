package com.hll.client;

import com.hll.command.RedisReply;

/**
 * Created by hll on 2016/5/3.
 */
public interface RedisConnectionCallBack {
  void handleReply(RedisReply reply);
}
