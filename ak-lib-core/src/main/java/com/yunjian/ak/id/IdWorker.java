package com.yunjian.ak.id;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 12:14
 * @Version 1.0
 */
class IdWorker {
    private final long workerId;
    private long sequence = 0L;
    private static final long twepoch = 1361753741828L;
    private static final long workerIdBits = 4L;
    public static final long maxWorkerId = 15L;
    private static final long sequenceBits = 10L;
    private static final long workerIdShift = 10L;
    private static final long timestampLeftShift = 14L;
    public static final long sequenceMask = 1023L;
    private long lastTimestamp = -1L;

    public IdWorker(long workerId) {
        if (workerId <= 15L && workerId >= 0L) {
            this.workerId = workerId;
        } else {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", 15L));
        }
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = this.sequence + 1L & 1023L;
            if (this.sequence == 0L) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0L;
        }

        if (timestamp < this.lastTimestamp) {
            try {
                throw new Exception(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.lastTimestamp = timestamp;
        long nextId = timestamp - 1361753741828L << 14 | this.workerId << 10 | this.sequence;
        return nextId;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for(timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }

        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
