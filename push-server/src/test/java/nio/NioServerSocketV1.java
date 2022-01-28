package nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhuang.ma
 * @date 2022/1/28
 */
@Slf4j
public class NioServerSocketV1 {
    volatile static List<SocketChannel> acceptChannels = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        log.info("启动成功");
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel!= null){
                log.info("收到连接{}",socketChannel);
                socketChannel.configureBlocking(false);
                //添加进来
                acceptChannels.add(socketChannel);
            }else {

                Iterator<SocketChannel> iterator = acceptChannels.iterator();
                while (iterator.hasNext()){
                    SocketChannel   acceptChannel = iterator.next();
                    ByteBuffer readBuf = ByteBuffer.allocate(1024);

                    //等于0 没有读到数据
                    if (acceptChannel.read(readBuf)==0){
                        continue;
                    }

                    while (acceptChannel.isOpen() && acceptChannel.read(readBuf) != -1){
                        //数据读取结束则返回
                        if (readBuf.position() > 0){
                            break;
                        }
                    }
                    // 没有数据
                    if (readBuf.position() == 0 )continue;

                    //转换为读模式
                    readBuf.flip();
                    byte[] content = new byte[readBuf.limit()];
                    readBuf.get(content);

                    log.info("读取收到的数据{},来自{}",new String(content),acceptChannel.getRemoteAddress());

                    String response = "HTTP/1.1 200 OK \r\n"+
                            "Content-Length: 11\r\n\r\n"+
                            "Hello World";

                    ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                    while (buffer.hasRemaining()){
                        acceptChannel.write(buffer);
                    }

                }
            }

        }

    }
}
