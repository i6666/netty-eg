package nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author zhuang.ma
 * @date 2022/1/28
 */
@Slf4j
public class NioServerSocketV2 {
    volatile static List<SocketChannel> acceptChannels = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        //构建一个时间，并将channel 注册上去
        Selector selector = Selector.open();
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0, serverSocketChannel);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT); //关注建立连接时事件


        serverSocketChannel.socket().bind(new InetSocketAddress(8080));

        log.info("启动成功");
        while (true){

            selector.select(); //阻塞

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                SelectionKey nextKey = keyIterator.next();
                keyIterator.remove();

                if (nextKey.isAcceptable()){
                    ServerSocketChannel server =(ServerSocketChannel) nextKey.attachment();
                    SocketChannel accept = server.accept();
                    accept.configureBlocking(false);
                    accept.register(selector,selectionKey.OP_READ,accept);

                    log.info("收到新连接{}",server);
                }
                if (nextKey.isReadable()){
                    SocketChannel client = (SocketChannel)nextKey.attachment();

                    ByteBuffer readBuf = ByteBuffer.allocate(1024);


                    while (client.isOpen() && client.read(readBuf) != -1){
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

                    log.info("读取收到的数据{},来自{}",new String(content),client.getRemoteAddress());

                    String response = "HTTP/1.1 200 OK \r\n"+
                            "Content-Length: 11\r\n\r\n"+
                            "Hello World";

                    ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                    while (buffer.hasRemaining()){
                        client.write(buffer);
                    }
                    nextKey.cancel();
                }



            }



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


                }
            }

        }

    }
}
