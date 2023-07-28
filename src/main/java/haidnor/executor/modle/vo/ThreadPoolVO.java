package haidnor.executor.modle.vo;

import lombok.Data;

/**
 * @author wang xiang
 */
@Data
public class ThreadPoolVO {

    /**
     * 当前存活的线程数
     */
    private Integer poolSize;

    /**
     * 核心线程数
     */
    private Integer corePoolSize;

    /**
     * 最大线程数
     */
    private Integer maximumPoolSize;

    /**
     * 池中同时进行的最大线程数
     */
    private Integer largestPoolSize;

    /**
     * 计划执行的任务的大概总数
     */
    private Long taskCount;

    /**
     * 正在执行任务的线程的大概数量
     */
    private Integer activeCount;

    /**
     * 完成执行的任务的大致总数
     */
    private Long completedTaskCount;

    /**
     * 线程存活时间
     */
    private Long keepAliveTime;

    /**
     * 当前队列中的任务数
     */
    private Integer queueSize;

    /**
     * 任务队列的最大容量
     */
    private Integer capacity;

    /**
     * 线程 toString()
     */
    private String poolInfo;

    /**
     * 线程池获取任务状态
     */
    private Boolean takeStatus;

}
