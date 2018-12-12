package reactor;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
/**
 * Created by Anur IjuoKaruKas on 2018/12/12
 */
public class Handler implements Runnable {

    private ArrayBlockingQueue<Request> requestQueue;

    private ArrayBlockingQueue<Response> responseQueue;

    public Handler(ArrayBlockingQueue<Request> requestQueue, ArrayBlockingQueue<Response> responseQueue) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Request request = requestQueue.poll(500, TimeUnit.MILLISECONDS);
                if (request != null) {
                    handler(request);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handler(Request request) throws InterruptedException {
        Thread.sleep(1000);        // 模拟业务处理
        ByteBuffer byteBuffer = request.getByteBuffer();
        byte[] bytes = byteBuffer.array();

        System.out.println("收到了请求：" + bytes);

        ByteBuffer response = ByteBuffer.allocate(1024);
        response.put("This is response".getBytes());
        response.put(bytes);
        responseQueue.put(new Response(request.getSelectionKey(), response));
    }
}