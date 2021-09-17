package com.linzongwei.netty.http;

/**
 * Description:
 *
 * @author linli
 * @since 2021/4/27 14:38
 */
public class StartUp {

    public static void main(String[] args) throws Exception{
        HttpServer httpServer = new HttpServer();
        httpServer.run();
    }
}
