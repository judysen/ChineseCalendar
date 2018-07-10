/* ========================================
 * System Name　　：化交线上平台
 * SubSystem Name ：化交站点核心工具集
 * File Name: Constants
 * ----------------------------------------
 * Create Date/Change History
 * ----------------------------------------
 * 2017/4/24 　lizhihua   Create
 *
 *
 * ----------------------------------------
 * Copyright (c) SCEM . All rights reserved.
 */
package com.judysen.chinesecalendar.core;

import com.alibaba.fastjson.JSON;
import com.judysen.chinesecalendar.config.CustomConfig;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lizhihua
 * @version 1.0
 */
@Component
public class ChineseWorkDayUtils {
    @Autowired
    private CustomConfig config;

    private Logger logger= LoggerFactory.getLogger("ChineseWorkDayUtils");
    private ConcurrentHashMap<Integer,List<String>> cacheHolidays=new ConcurrentHashMap<>();
    String fileContext=null;
    List<Workday> workdays=null;
    public ChineseWorkDayUtils(){

    }
    public void init(){
        try{
            this.getConfigFile(config.getWorkfileDir());
        }
        catch (Exception e){
            logger.error("获取工作日配置文件出错",e);
        }
    }
    /**
     * 获取配置文件Json文件的内容
     * @param filename
     * @return
     * @throws Exception
     */
    private void getConfigFile(String filename) throws Exception{
        File file=new File(filename);
        if(!file.exists())
        {
            file= ResourceUtils.getFile(filename);
            if(file==null){
                throw new Exception("找不到指定的日期定义文件："+filename);
            }
        }

        fileContext= FileUtils.readFileToString(file,"UTF-8");
        workdays=JSON.parseArray(fileContext,Workday.class);
    }

    /**
     * 判断是否是中国式工作日
     * @param date
     * @return
     */
    public boolean isChineseWorkDay(Date date) throws Exception{
        if(StringUtils.isEmpty(fileContext))
            return  false;

        boolean flag=false;
        Calendar yearCalendar=Calendar.getInstance();
        yearCalendar.setTime(date);
        Workday workday=this.workdays.stream().filter(f->f.getYear()==yearCalendar.get(Calendar.YEAR)).findFirst().orElse(null);
        if(workday==null){
            throw new Exception("找不到对应的日期数据，请检查日期配置文件");
        }
        if(!isWorkDay(date)){
            flag= workday.getWorkdays().contains(DateUtils.FormatDate(date,"yyyy-MM-dd"));
        }else {
            flag=!workday.getWeekends().contains(DateUtils.FormatDate(date,"yyyy-MM-dd"));
        }
        return flag;
    }

    /**
     *
     * @param currentDay
     * @param next
     * @return
     */
    public Date nextChineseWorkDay(Date currentDay,int next) throws Exception{
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(currentDay);
        int step=1;
        if(next<0){
            step=-1;
            next=-next;
        }
        int index=0;
        int tempNext=0;
        while(tempNext<next){
            if(index>200+next){
                throw new Exception("workjobday configuration is wrong");
            }
            calendar.add(calendar.DATE,step);
            Date date=calendar.getTime();
            if(isChineseWorkDay(date)){
                tempNext++;
            }

            index++;
        }
        return calendar.getTime();
    }

    /**
     *
     * @param date
     * @return
     */
    private boolean isWorkDay(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int d=calendar.get(Calendar.DAY_OF_WEEK);
        if(d==1||d==7){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 返回一年所有的假期
     * @param year
     * @return
     */
    public synchronized List<String> holidaysInYear(int year) throws Exception{
        List<String> allHolidays=new ArrayList<>();
        if(cacheHolidays.containsKey(year)){
            allHolidays=cacheHolidays.get(year);
        }else {
            Calendar startDate=Calendar.getInstance();
            startDate.set(year,0,1);
            Calendar endDate=Calendar.getInstance();
            endDate.set(year,11,31);

            while(startDate.before(endDate)){
                if(!this.isChineseWorkDay(startDate.getTime())){
                    allHolidays.add(DateUtils.FormatDate(startDate.getTime(),"yyyy-MM-dd"));
                }
                startDate.add(Calendar.DATE,1);
            }
        }

        return allHolidays;
    }
}
