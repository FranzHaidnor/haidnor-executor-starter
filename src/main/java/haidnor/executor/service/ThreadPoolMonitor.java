package haidnor.executor.service;

import lombok.SneakyThrows;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolMonitor implements Runnable {

    /**
     * 记录上一次的线程池任务完成总数
     */
    private long lastTaskCount;

    @SneakyThrows
    @Override
    public void run() {
        ThreadPoolExecutor executor = ThreadPoolService.getExecutor();
        while (true) {
            long taskCount = executor.getTaskCount();
            ThreadPoolService.setThroughput(taskCount - lastTaskCount);
            lastTaskCount = taskCount;
            TimeUnit.SECONDS.sleep(1);
        }
    }

}
