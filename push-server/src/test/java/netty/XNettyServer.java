package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhuang.ma
 * @date 2022/1/27
 */
@Slf4j
public class XNettyServer {
    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup mainGroup = new NioEventLoopGroup();
        NioEventLoopGroup subGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(mainGroup,subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new XDecoder());
                        pipeline.addLast(new XHandler());

                    }
                });
        try {

            ChannelFuture channelFuture = b.bind(9999).sync();
            log.info("服务启动成功");
            Channel channel = channelFuture.channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }


    }
}
