package haidnor.executor.service;

import haidnor.executor.util.ElasticityLinkedBlockingQueue;

import java.util.concurrent.*;

/**
 * @author wang xiang
 */
public class ThreadPoolService {

    /**
     * 线程池
     */
    private static final ThreadPoolExecutor executor;

    /**
     * 任务队列
     */
    private static final ElasticityLinkedBlockingQueue<Runnable> workQueue;

    /**
     * 线程池处理任务每秒的吞吐量
     */
    private static long throughput;

    static {
        // 核心线程数
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        // 最大线程数
        int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        // 空闲等待时间
        long keepAliveTime = 60L;
        // 时间单位
        TimeUnit unit = TimeUnit.SECONDS;
        // 任务队列
        workQueue = new ElasticityLinkedBlockingQueue<>(Integer.MAX_VALUE);
        // 线程池工厂
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        // 拒绝策略
        RejectedExecutionHandler abortPolicy = new ThreadPoolExecutor.AbortPolicy();

        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, abortPolicy);

        // 弹性线程自动优化收获线程
        Thread thread = new Thread(new ThreadPoolMonitor());
        thread.setDaemon(true);
        thread.start();
    }

    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public static long getThroughput() {
        return throughput;
    }

    public static void setThroughput(long throughput) {
        ThreadPoolService.throughput = throughput;
    }

    /**
     * 设置核心线程数,大于零并小于等于最大线程数
     *
     * @param corePoolSize 核心线程数
     */
    public static void setCorePoolSize(int corePoolSize) {
        if (corePoolSize < 0) {
            throw new IllegalArgumentException();
        }
        if (corePoolSize > executor.getMaximumPoolSize()) {
            throw new IllegalArgumentException("核心线程数不能大于最大线程数");
        }
        executor.setCorePoolSize(corePoolSize);
    }

    /**
     * 设置最大线程数,大于零并大于等于核心线程数
     *
     * @param maximumPoolSize 最大线程数
     */
    public static void setMaximumPoolSize(int maximumPoolSize) {
        if (executor.getCorePoolSize() > maximumPoolSize) {
            throw new IllegalArgumentException("最大线程数不可小于核心线程数");
        }
        executor.setMaximumPoolSize(maximumPoolSize);
    }

    /**
     * 设置任务队列最大容量
     *
     * @param capacity 任务队列最大容量
     */
    public static void setCapacity(int capacity) {
        if (executor.getQueue() instanceof ElasticityLinkedBlockingQueue) {
            ElasticityLinkedBlockingQueue<Runnable> queue = (ElasticityLinkedBlockingQueue<Runnable>) executor.getQueue();
            try {
                queue.setCapacity(capacity);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置线程存活时间
     *
     * @param keepAliveTime 存活时间
     */
    public static void setKeepAliveTime(long keepAliveTime) {
        if (keepAliveTime <= 0) {
            throw new IllegalArgumentException("线程存活时间不能小于等于 0 秒");
        }
        executor.setKeepAliveTime(keepAliveTime, TimeUnit.SECONDS);
    }

    /**
     * 返回线程池运行暂停状态
     *
     * @return true 正常运行, false 暂停从任务队列中获取任务
     * @author wang xiang
     */
    public static boolean getExecutorStatus() {
        return workQueue.getTakeStatus();
    }

    /**
     * 设置当前队列调用 take() 方法的状态, 用于控制线程池暂停与运行
     *
     * @param status true 正常运行, false 暂停从任务队列中获取任务
     */
    public static void setExecutorStatus(boolean status) {
        workQueue.setTakeStatus(status);
    }

}
