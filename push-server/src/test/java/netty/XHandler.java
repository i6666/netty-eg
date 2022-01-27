package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhuang.ma
 * @date 2022/1/27
 */
@Slf4j
public class XHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        if (buf != null){
            String content = buf.toString(CharsetUtil.UTF_8);
            log.info("XHandler read {}",content);
        }


    }
}
