package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author zhuang.ma
 * @date 2022/1/27
 */
@Slf4j
public class XDecoder extends ByteToMessageDecoder {
    static final  int PACKET_SIZE = 220;

    ByteBuf tempMsg = Unpooled.buffer();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
      log.info("收到的数据长度{}",in.readableBytes());

        int tempSize = tempMsg.readableBytes();
        ByteBuf message = null;
        if (tempSize > 0 ){
            message = Unpooled.buffer();
            message.writeBytes(tempMsg);
            message.writeBytes(in);
            log.info("合并后的message{}",message);
        }else {
            message = in;
        }
        int size = message.readableBytes();
        int count = size / PACKET_SIZE;
        ByteBuf finalMessage = message;
        IntStream.range(0,count).forEach(
                i->{
                    byte[] bytes = new byte[PACKET_SIZE];
                    finalMessage.readBytes(bytes);
                    out.add(Unpooled.copiedBuffer(bytes));
                }


        );
        size = message.readableBytes();
        if (size != 0){
            tempMsg.clear();
            tempMsg.writeBytes(message.readBytes(size));
        }

        log.info("{}",message);


    }
}
