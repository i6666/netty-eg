package bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * @author zhuang.ma
 * @date 2022/1/27
 */
@Slf4j
public class MySocketClient {
    public static void main(String[] args) throws Exception {

        byte[] request = new byte[220];
        byte[] wid = "5555555555".getBytes();
        byte[] content = "余额统计分析".getBytes();

        System.arraycopy(wid, 0, request, 0, 10);
        System.arraycopy(content, 0, request, 10, content.length);

        Socket socket = new Socket("127.0.0.1", 9999);
        OutputStream outputStream = socket.getOutputStream();

        log.info("{}", request);

        CountDownLatch countDownLatch = new CountDownLatch(10);
        IntStream.range(0, 10).forEach(
                i -> new Thread(() -> {
                    try {
                        outputStream.write(request);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }

                }).start()
        );

        countDownLatch.await();
        Thread.sleep(100000L);
        socket.close();

    }
}
