package com.xly.codemind.utils;

import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @author X-LY。
 * @version 1.0
 * @className IdWorker
 * @description 全局ID生成器(雪花算法)
 **/
@Component
public class IdWorker {

    //private static Logger logger = LoggerFactory.getLogger(IdWorker.class);

    private static volatile IdWorker instance;

    //时间起始标记点，作为基准，一般取系统的最近时间（一旦确定不能变动）
    private final static long twepoch = 1288834974657L;

    //机器标识位数
    private final static long workerIdBits = 5L;

    //数据中心标识位数
    private final static long datacenterIdBits = 5L;

    //机器ID最大值
    private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);

    //数据中心ID最大值
    private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    //毫秒内自增位
    private final static long sequenceBits = 12L;

    //机器ID偏左移12位
    private final static long workerIdShift = sequenceBits;

    //数据中心ID左移17位
    private final static long datacenterIdShift = sequenceBits + workerIdBits;

    //时间毫秒左移22位
    private final static long timestampleftShift = sequenceBits + workerIdBits + datacenterIdBits;

    private final static long sequenceMask = -1L ^ (-1L << sequenceBits);

    //上次生产ID时间戳
    private static long lastTimestamp = -1L;

    //同毫秒并发控制
    private long sequence = 0L;

    //机器ID
    private final long workerId;

    //机房ID
    private final long datacenterId;

    private IdWorker(){
        this.datacenterId = getDatacenterId(maxDatacenterId);
        this.workerId = getMaxWorkerId(datacenterId,maxWorkerId);
    }

    /**
     * 单例，获取初始化类
     * @return
     */
    public static IdWorker getInstance(){
        if(instance == null){
            synchronized (IdWorker.class){
                instance = new IdWorker();
            }
        }
        return instance;
    }

    /**
     * workerId 工作机器ID
     * datacenterId 序列号
     * @param workerId
     * @param datacenterId
     */
    public IdWorker(long workerId,long datacenterId){
        if(workerId > maxWorkerId || workerId < 0){
            //throw new Exception(String.format("worker ID can't be greater than %d or less than 0",maxWorkerId));
            throw new RuntimeException("worker Id can't be greater than maxWorkerId and less than 0");
        }
        if(datacenterId > maxDatacenterId || datacenterId < 0){
            //throw new Exception(String.format("datacenterId can't be greater than %d or less than 0"));
            throw new RuntimeException("datacenterId can't be greater than %d or less than 0");
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获取下一个ID
     * @return
     */
    public synchronized long nextId(){
        long timestamp = timeGen();
        if(timestamp < lastTimestamp){
            throw new RuntimeException(String.format("clock moved backwards"));
        }

        if(lastTimestamp == timestamp){
            //当前毫秒内，则+1
            sequence = (sequence + 1) & sequenceMask;
            if(sequence == 0){
                //当前毫秒没计数满了，则等待下一秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        }else{
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        //ID偏移组合生成最终的ID,并返回ID
        long nextId = ((timestamp - twepoch) << timestampleftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;

        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp){
        long timestamp = this.timeGen();
        while(timestamp <= lastTimestamp){
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen(){
        return System.currentTimeMillis();
    }

    protected static long getMaxWorkerId(long datacenterId,long maxWorkerId){
        StringBuffer mpid = new StringBuffer();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if(!name.isEmpty()){
            /**
             * get jvmPid
             */
            mpid.append(name.split("@")[0]);
        }
        /**
         * MAC + PID 的hashcode,获取16个低位
         */
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    protected static long getDatacenterId(long maxDatacenterId){
        long id = 0L;
        try{
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if(network == null){
                id = 1L;
            }else{
                byte[] mac = network.getHardwareAddress();
                id = ((0x000000FF & (long) mac[mac.length - 1])
                        | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                id = id % (maxDatacenterId + 1);
            }
        }catch (Exception e){
            //logger.error("getDatacenterId:" + e.getMessage());
        }
        return id;
    }

}
