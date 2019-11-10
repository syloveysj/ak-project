package com.yunjian.ak.task.service;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.dao.annotation.transaction.AkTransactional;
import com.yunjian.ak.dao.core.DaoFactory;
import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.dao.mybatis.enhance.Pageable;
import com.yunjian.ak.dao.mybatis.enhance.Sortable;
import com.yunjian.ak.dao.utils.SortableUtil;
import com.yunjian.ak.task.entity.JobEntity;
import com.yunjian.ak.task.job.DynamicJob;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/11/10 18:31
 * @Version 1.0
 */
@Service
public class DynamicJobService {

    public DynamicJobService() {
        super();
    }

    //通过Id获取Job
    public JobEntity getJobEntityById(Integer id) {
        return DaoFactory.create(JobEntity.class).selectByID(id);
    }

    //从数据库中加载获取到所有Job
    public List<JobEntity> loadJobs() {
        return DaoFactory.create(JobEntity.class).selectAll();
    }

    //获取JobDataMap.(Job参数对象)
    public JobDataMap getJobDataMap(JobEntity job) {
        JobDataMap map = new JobDataMap();
        map.put("name", job.getName());
        map.put("jobGroup", job.getJobGroup());
        map.put("cronExpression", job.getCron());
        map.put("parameter", job.getParameter());
        map.put("jobDescription", job.getDescription());
        map.put("vmParam", job.getVmParam());
        map.put("jarPath", job.getJarPath());
        map.put("status", job.getStatus());
        return map;
    }

    //获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
    public JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map) {
        return JobBuilder.newJob(DynamicJob.class)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(map)
                .storeDurably()
                .build();
    }

    //获取Trigger (Job的触发器,执行规则)
    public Trigger getTrigger(JobEntity job) {
        return TriggerBuilder.newTrigger()
                .withIdentity(job.getName(), job.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()))
                .build();
    }

    //获取JobKey,包含Name和Group
    public JobKey getJobKey(JobEntity job) {
        return JobKey.jobKey(job.getName(), job.getJobGroup());
    }

    /**
     * 将JobEntity中属性值不为null的数据插入到数据库
     *
     * @param entity
     * @return
     */
    public JobEntity insert(JobEntity entity) {
        DaoFactory.create(JobEntity.class).insert(entity);
        return entity;
    }

    /**
     * 通过JobEntity不为null数据为条件更新JobEntity中属性不为null的数据
     *
     * @param entity
     * @return
     */
    public JobEntity update(JobEntity entity) {
        DaoFactory.create(JobEntity.class).update(entity);
        return entity;
    }

    /**
     * 通过JobEntity的id删除JobEntity
     *
     * @param id
     * @return
     */
    public void delete(Integer id) {
        JobEntity entity = new JobEntity();
        entity.setId(id);
        DaoFactory.create(JobEntity.class).delete(entity);
    }

    /**
     * 通过JobEntity的ids列表批量删除JobEntity
     *
     * @param ids
     * @return
     */
    @AkTransactional(datasourceID = "sys")
    public void deleteBatch(List<Integer> ids) {
        DaoFactory.create(JobEntity.class).deleteBatch(ids);
    }

    /**
     * 获取JobEntity的分页列表数据
     *
     * @param page
     * @param pagesize
     * @param sort
     * @param order
     * @param cond
     * @return
     */
    public Page<JobEntity> getListByPage(int page, int pagesize, String sort, String order, String cond) {
        Pageable pageable = new Pageable(page, pagesize);
        Sortable sortable = SortableUtil.getSortable(sort, order);
        JobEntity entity = StringUtils.isNotBlank(cond) ? JSON.parseObject(cond, JobEntity.class) : new JobEntity();
        return DaoFactory.create(JobEntity.class).selectByPage(entity, pageable, sortable, true);
    }
}
