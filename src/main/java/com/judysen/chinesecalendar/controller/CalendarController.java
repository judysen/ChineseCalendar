package com.judysen.chinesecalendar.controller;

import com.judysen.chinesecalendar.core.ChineseWorkDayUtils;
import com.judysen.chinesecalendar.core.DateUtils;
import com.judysen.chinesecalendar.core.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Api(value = "CalendarController",tags="获取中国假期接口")
@RestController
@RequestMapping("calendar")
public class CalendarController {
    @Autowired
    private ChineseWorkDayUtils chineseWorkDayUtils;

    /**
     * 获取1年的所有假期（包括星期六，星期天）
     * @param year
     * @return
     */
    @ApiOperation(value = "获取指定一年所有的假期，包括星期六星期天",notes = "包括星期六星期天")
    @RequestMapping(value = "year",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ResponseMessage year(int year){
        ResponseMessage responseMessage=new ResponseMessage();
        try{
            List<String> holidays=chineseWorkDayUtils.holidaysInYear(year);

            responseMessage.setCode("1");
            responseMessage.setData(holidays);
            responseMessage.setMessage("success");
        }catch (Exception ex){
            responseMessage.setCode("0");
            responseMessage.setData(null);
            responseMessage.setMessage("fail");
        }

        return responseMessage;
    }

    /**
     * 判断给定的日期是否是工作日
     * @param date 日期
     * @return
     */
    @ApiOperation(value = "是否是工作日")
    @RequestMapping(value="workday",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ResponseMessage isWorkday(String date) throws Exception{
        Date checkDate =DateUtils.parseDate(date,"yyyy-MM-dd","yyyyMMdd");
        boolean flag=chineseWorkDayUtils.isChineseWorkDay(checkDate);
        ResponseMessage message=new ResponseMessage();
        message.setMessage("success");
        message.setData(flag);
        message.setCode("1");
        return message;
    }

    /**
     *获取给定起始日期的后cnt天后的工作日
     * @param date 其实日期，默认为当前日期
     * @param cnt 天数,默认1天,可以为负数
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取指定天数后的工作日",notes = "包括星期六星期天")
    @RequestMapping(value = "nextWorkday",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ResponseMessage nextWorkday(String date,Integer cnt) throws Exception{
        Date checkDate=new Date();
        if(!StringUtils.isEmpty(date)){
            checkDate=DateUtils.parseDate(date,"yyyy-MM-dd","yyyyMMdd");
        }

        if(cnt==null){
            cnt=1;
        }

        Date flag=chineseWorkDayUtils.nextChineseWorkDay(checkDate,cnt);
        ResponseMessage message=new ResponseMessage();
        message.setMessage("success");
        message.setData(DateUtils.FormatDate(flag,"yyyy-MM-dd"));
        message.setCode("1");
        return message;
    }
}
