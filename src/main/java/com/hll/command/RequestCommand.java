package com.hll.command;

import com.hll.client.RedisConnectionCallBack;
import com.hll.protocal.ProtoUtil;
import com.hll.protocal.RedisRequestDecoder;
import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * Created by hll on 2016/4/29.
 */
public class RequestCommand implements RedisRequest {
  private int argCount;
  private List<byte[]> args;

  private RedisConnectionCallBack redisConnectionCallBack;

  public int getArgCount() {
    return argCount;
  }

  public void setArgCount(int argCount) {
    this.argCount = argCount;
  }

  public List<byte[]> getArgs() {
    return args;
  }

  public void setArgs(List<byte[]> args) {
    this.args = args;
  }

  public void setCallback(RedisConnectionCallBack callback) {
    this.redisConnectionCallBack = callback;
  }

  public RedisConnectionCallBack getRedisConnectionCallBack() {
    return redisConnectionCallBack;
  }

  @Override
  public void encode(ByteBuf byteBuf) {
    byteBuf.writeByte((byte) '*');
    byteBuf.writeBytes(ProtoUtil.convertIntToByteArray(args.size()));
    writeCRLF(byteBuf);
    for (byte[] arg : args) {
      byteBuf.writeByte((byte) '$');
      byteBuf.writeBytes(ProtoUtil.convertIntToByteArray(arg.length));
      writeCRLF(byteBuf);
      byteBuf.writeBytes(arg);
      writeCRLF(byteBuf);
    }
  }

  private void writeCRLF(ByteBuf byteBuf) {
    byteBuf.writeByte(RedisRequestDecoder.CR_BYTE);
    byteBuf.writeByte(RedisRequestDecoder.LF_BYTE);
  }
}
