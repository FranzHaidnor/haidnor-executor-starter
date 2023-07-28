package haidnor.executor.controller;

import haidnor.executor.modle.vo.ThreadPoolVO;
import haidnor.executor.service.ThreadPoolService;
import haidnor.executor.util.ElasticityLinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wang xiang
 */
@RestController
@RequestMapping("/threadPool")
@Slf4j
public class ThreadPoolController {

    /**
     * 获取当前线程池信息
     */
    @RequestMapping("/getCurrentThreadPoolInfo")
    public ThreadPoolVO getThreadPoolInfo() {
        ThreadPoolExecutor executor = ThreadPoolService.getExecutor();

        ThreadPoolVO threadPoolVO = new ThreadPoolVO();
        // 线程信息
        threadPoolVO.setPoolSize(executor.getPoolSize());
        threadPoolVO.setMaximumPoolSize(executor.getMaximumPoolSize());
        threadPoolVO.setCorePoolSize(executor.getCorePoolSize());
        threadPoolVO.setActiveCount(executor.getActiveCount());
        threadPoolVO.setLargestPoolSize(executor.getLargestPoolSize());
        threadPoolVO.setKeepAliveTime(executor.getKeepAliveTime(TimeUnit.SECONDS));
        // 任务数信息
        threadPoolVO.setTaskCount(executor.getTaskCount());
        threadPoolVO.setCompletedTaskCount(executor.getCompletedTaskCount());

        // 任务队列信息
        ElasticityLinkedBlockingQueue<Runnable> queue = (ElasticityLinkedBlockingQueue<Runnable>) executor.getQueue();
        threadPoolVO.setQueueSize(queue.size());
        threadPoolVO.setCapacity(queue.getCapacity());

        // 其他信息
        threadPoolVO.setTakeStatus(ThreadPoolService.getExecutorStatus());
        threadPoolVO.setPoolInfo(executor.toString());
        return threadPoolVO;
    }

    /**
     * 设置核心线程数
     */
    @RequestMapping("/setCorePoolSize")
    public void setCorePoolSize(int corePoolSize) {
        ThreadPoolService.setCorePoolSize(corePoolSize);
        log.info("设置线程池核心线程数 {}", corePoolSize);
    }

    /**
     * 设置最大线程数
     */
    @PostMapping("/setMaximumPoolSize")
    public void setMaximumPoolSize(int maximumPoolSize) {
        ThreadPoolService.setMaximumPoolSize(maximumPoolSize);
        log.info("设置线程池最大线程数 {}", maximumPoolSize);
    }

    /**
     * 设置线程存活时间
     *
     * @param keepAliveTime 时长(秒)
     */
    @PostMapping("setKeepAliveTime")
    public void setKeepAliveTime(long keepAliveTime) {
        ThreadPoolService.setKeepAliveTime(keepAliveTime);
    }

    /**
     * 设置任务队列最大容量
     */
    @PostMapping("/setCapacity")
    public void setCapacity(int capacity) {
        ThreadPoolService.setCapacity(capacity);
        log.info("设置线程池任务队列最大容量 {}", capacity);
    }

    /**
     * 设置当前队列调用 take() 方法的状态, 用于控制线程池暂停与运行
     */
    @PostMapping("/setTakeStatus")
    public void setTakeStatus(boolean takeStatus) {
        ThreadPoolService.setExecutorStatus(takeStatus);
        log.info("设置线程池获取任务状态 {}", takeStatus);
    }

    /**
     * 测试接口
     * 添加 10000 个任务到线程池中,每个任务执行时间随机为 1 - 2000 毫秒
     */
    @RequestMapping("/addTask")
    public String addTask() {
        ThreadPoolExecutor executor = ThreadPoolService.getExecutor();
        try {
            for (int i = 0; i < 10000; i++) {
                executor.execute(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(new Random().nextInt(2000) + 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (RejectedExecutionException rejectedException) {
            log.info("添加任务失败");
            rejectedException.printStackTrace();
        }
        return "添加成功";
    }

}
