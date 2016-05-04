package com.hll;

import com.hll.conf.Configuration;
import com.hll.server.HedisServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hll on 2016/4/29.
 */
public class Bootstrap {

  private static Logger logger= LoggerFactory.getLogger(Bootstrap.class);

  public static void main(String[] args) {
    try {
      Configuration conf = new Configuration();
      HedisServer hedisServer = new HedisServer(conf);
      hedisServer.init();
      hedisServer.start();
      logger.info("hedis启动成功,host:{},port:{}",conf.getHedisHost(),conf.getHedisPort());
    }catch (Exception e){
      logger.error("hedis启动失败:",e);
      System.exit(1);
    }
  }
}
