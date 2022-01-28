package nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author zhuang.ma
 * @date 2022/1/28
 */
@Slf4j
public class NioSocketClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));

        while (!socketChannel.finishConnect()) {
            Thread.yield();
        }

        Scanner scanner = new Scanner(System.in);
        log.info("请输入：");
        String line = scanner.nextLine();
        ByteBuffer buffer = ByteBuffer.wrap(line.getBytes());
        while (buffer.hasRemaining()){
            socketChannel.write(buffer);
        }

        //读取服务端响应

        ByteBuffer requestBuffer = ByteBuffer.allocate(64);
        while (socketChannel.isOpen() && socketChannel.read(requestBuffer)!= -1){
            if (requestBuffer.position() > 0) break;
        }
        requestBuffer.flip();

        byte[] content = new byte[requestBuffer.limit()];
        requestBuffer.get(content);

        log.info("读取到服务端响应{}",new String(content));



        scanner.close();

    }
}
