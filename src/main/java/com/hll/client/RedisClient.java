package com.hll.client;

import com.google.common.collect.Lists;
import com.hll.command.RequestCommand;
import com.hll.conf.Configuration;
import com.hll.protocal.RedisReplyDecoder;
import com.hll.protocal.RedisReplyEncoder;
import com.hll.protocal.RedisRequestEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hll on 2016/4/26.
 */
public class RedisClient {

  private Logger logger = LoggerFactory.getLogger(RedisClient.class);

  private static final EventLoopGroup workerGroup = new NioEventLoopGroup(1);

  private Configuration conf;

  public final static AttributeKey<RedisConnection> REDIS_CONNECTION_ATTRIBUTE = AttributeKey.valueOf("redisconnection");
  private RedisConnection redisConnection;

  public RedisClient(Configuration conf) {
    this.conf = conf;
  }

  public void connect() {
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(workerGroup)
        .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
        .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
        .channel(NioSocketChannel.class)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast("RedisReplyDecoder", new RedisReplyDecoder());
            ch.pipeline().addLast("RedisRequestEncoder", new RedisRequestEncoder());
            ch.pipeline().addLast("ClientInHandler", new ClientInHandler());
            ch.pipeline().addLast("ClientOutHandler", new ClientOutHandler());
          }
        });
    ChannelFuture channelFuture = bootstrap.connect(conf.getRedisHost(), Integer.parseInt(conf.getRedisPort())).syncUninterruptibly();
    logger.info("连接redis成功-{}:{}",conf.getRedisHost(),conf.getRedisPort());

    Channel channel = channelFuture.channel();
    redisConnection = new RedisConnection(channel, conf.getRedisHost(), conf.getRedisPort());
    channel.attr(REDIS_CONNECTION_ATTRIBUTE).set(redisConnection);
    //关闭连接，记录日志
    channelFuture.channel().closeFuture().addListener(future -> logger.info("与redis(host:{} port:{})的连接关闭", conf.getRedisHost(), conf.getRedisPort()));
  }

  public RedisConnection getRedisConnection() {
    return redisConnection;
  }

  public static void main(String[] args) throws InterruptedException {
    Configuration conf = new Configuration();
    RedisClient redisClient = new RedisClient(conf);
    redisClient.connect();
    RequestCommand command = new RequestCommand();
    command.setArgCount(2);
    byte[] comm=new byte[]{(byte)'I',(byte)'N',(byte)'C',(byte)'R'};
    byte[] arg=new byte[]{(byte)'h',(byte)'e',(byte)'l',(byte)'l',(byte)'o'};
    command.setArgs(Lists.newArrayList(comm,arg));
    redisClient.getRedisConnection().write(command);
    Thread.sleep(1000000000);
  }
}
