package netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Created by Anur IjuoKaruKas on 2019/1/15
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 【2-1】
     * 它仍需要传入消息返回给发送者，而 write 操作是异步的直到 channelRead 方法返回后可能仍然没有完成（2-1）。
     * 为此，EchoServerHandler拓展了ChannelInboundHandlerAdapter，其在这个时间点上不会释放消息。
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)// 读完后好像是返回一个空的buffer，然后关闭channel？？？
           .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();// 关闭这个Channel
    }
}
