package nio;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * @author zhuang.ma
 * @date 2022/1/28
 */
@Slf4j
public class ByteBufferDemo {
    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        log.info("初始化{}",byteBuffer);

        byteBuffer.put((byte)1);
        byteBuffer.put((byte)2);
        byteBuffer.put((byte)3);
        log.info("写入三个字节{}",byteBuffer);

        log.info("开始读取=======");

        //不调用flip 也是可以读取，但是position 是从4开始读，不对，需要重置下position
        log.info("读取一个{},byteBuffer:{}",byteBuffer.get(),byteBuffer);
        log.info("调用flip=======");

        byteBuffer.flip();

        log.info("读取一个{},byteBuffer:{}",byteBuffer.get(),byteBuffer);
        log.info("读取一个{},byteBuffer:{}",byteBuffer.get(),byteBuffer);

        byteBuffer.compact(); //仅清除已阅读的数据，转换为写模式
        log.info("compact调用后,byteBuffer:{}",byteBuffer);

        byteBuffer.put((byte)3);
        byteBuffer.put((byte)4);
        log.info("写入三个，byteBuffer:{}",byteBuffer);



    }
}
