package bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhuang.ma
 * @date 2022/1/27
 */
@Slf4j
public class MySocketServer {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(9999);

        while (true) {
            Socket accept = serverSocket.accept();
            InputStream inputStream = accept.getInputStream();
            while (true){
                byte[] request = new byte[1024];
                int read = inputStream.read(request);
                if (read == -1){
                    break;
                }
                log.info("{}",new String(request));

            }
        }
    }
}
