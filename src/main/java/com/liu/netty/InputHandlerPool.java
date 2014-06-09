package com.liu.netty;

import io.netty.channel.Channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.liu.helper.QueueHelper;
import com.liu.message.Response;

public class InputHandlerPool {
    private static Logger logger = Logger.getLogger(InputHandlerPool.class);

    private static ExecutorService handlerExecutor;

    public static boolean init() {

        handlerExecutor = Executors.newFixedThreadPool(500);
        if (handlerExecutor == null) return false;

        logger.info("Initializing input queue ...");
        if (!QueueHelper.init()) {
            logger.error("Error occurred during initializing input request queue");
            return false;
        } else {
            return true;
        }
    }

    public static void submit(Channel channel, String inputJson, boolean isKeepAlive) {
        handlerExecutor.execute(new InputHandlerTask(channel, inputJson, isKeepAlive));
    }

    private static class InputHandlerTask implements Runnable {
        private String inputJson;
        private Channel channel;
        private boolean isKeepAlive;

        public InputHandlerTask(Channel channel, String inputJson,
                                boolean isKeepAlive) {
            this.channel = channel;
            this.inputJson = inputJson;
            this.isKeepAlive = isKeepAlive;
        }

        @Override
        public void run() {
            Response res = Dispatcher.dispatch(this.inputJson);
            NettyResponse.write(this.channel, res, this.isKeepAlive);
        }
    }
}
