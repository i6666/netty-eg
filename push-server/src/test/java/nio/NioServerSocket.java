package nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author zhuang.ma
 * @date 2022/1/28
 */
@Slf4j
public class NioServerSocket {
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
                ByteBuffer readBuf = ByteBuffer.allocate(1024);

                while (socketChannel.isOpen() && socketChannel.read(readBuf) != -1){
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

                log.info("读取收到的数据{},来自{}",new String(content),socketChannel.getRemoteAddress());

                String response = "HTTP/1.1 200 OK \r\n"+
                        "Content-Length: 11\r\n\r\n"+
                        "Hello World";

                ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                while (buffer.hasRemaining()){
                    socketChannel.write(buffer);
                }


            }

        }

    }
}
