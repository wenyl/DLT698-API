package cn.com.wenyl.bs.dlt698.net4g.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NettyTcpServer implements ApplicationRunner {
    private static final int port = 30055; // GPRS/4G 设备连接的端口
    @Autowired
    private ObjectProvider<cn.com.wenyl.bs.dlt698.net4g.tcp.TcpServerHandler> handlerProvider;
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(handlerProvider.getObject());
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("TCP 服务器已启动，监听端口:{}", port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("TCP 服务器启动失败", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(this::start).start();
    }
}
