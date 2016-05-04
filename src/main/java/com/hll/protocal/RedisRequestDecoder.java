package com.hll.protocal;

import com.hll.command.RequestCommand;
import com.hll.command.ShutdownCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hll on 2016/4/29.
 */
public class RedisRequestDecoder extends ReplayingDecoder<RedisRequestDecoder.State> {

  public static final char DOLLAR_BYTE = '$';
  public static final char ASTERISK_BYTE = '*';
  public static final char CR_BYTE = '\r';
  public static final char LF_BYTE = '\n';

  protected enum State {
    READ_SKIP,//健壮性考虑，如果第一个字符不是*则skip直到遇到*
    READ_INIT,
    READ_ARGC,//参数数量
    READ_ARG, //参数
    READ_END
  }

  private RequestCommand requestCommand;

  public RedisRequestDecoder() {
    super(State.READ_SKIP);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {

    switch (state()) {
      case READ_SKIP: {
        try {
          skipChar(buffer);
          checkpoint(State.READ_INIT);
        } finally {
          checkpoint();
        }
      }
      case READ_INIT: {
        char ch = (char) buffer.readByte();
        if (ch == ASTERISK_BYTE) {
          ch = (char) buffer.readByte();
          if (ch == CR_BYTE) {//shutdown命令
            buffer.readByte();
            checkpoint(State.READ_SKIP);
            out.add(new ShutdownCommand());
          } else {
            buffer.readerIndex(buffer.readerIndex() - 1);
            requestCommand = new RequestCommand();
            checkpoint(State.READ_ARGC);
          }
        }
      }
      case READ_ARGC: {
        requestCommand.setArgCount(readInt(buffer));
        checkpoint(State.READ_ARG);
      }
      case READ_ARG: {
        List<byte[]> args = new ArrayList<>(requestCommand.getArgCount());
        while (args.size() < requestCommand.getArgCount()) {
          buffer.readByte();//读出$
          int length = readInt(buffer);
          byte[] argByte = new byte[length];
          buffer.readBytes(argByte);
          buffer.skipBytes(2);//skip \r\n
          args.add(argByte);
        }
        requestCommand.setArgs(args);
        checkpoint(State.READ_END);
      }
      case READ_END: {
        RequestCommand command = this.requestCommand;
        this.requestCommand = null;
        checkpoint(State.READ_INIT);
        out.add(command);
        return;
      }
      default:
        throw new Error("can't reach here!");
    }
  }

  private int readInt(ByteBuf buffer) {
    StringBuilder sb = new StringBuilder();
    char ch = (char) buffer.readByte();
    while (ch != CR_BYTE) {
      sb.append(ch);
      ch = (char) buffer.readByte();
    }
    buffer.readByte();
    return Integer.parseInt(sb.toString());
  }


  private void skipChar(ByteBuf buffer) {
    for (; ; ) {
      char ch = (char) buffer.readByte();
      if (ch == ASTERISK_BYTE) {
        buffer.readerIndex(buffer.readerIndex() - 1);
        break;
      }
    }
  }

}
