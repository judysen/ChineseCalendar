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

import java.util.List;

/**
 * @author lizhihua
 * @version 1.0
 */
public class Workday {
    private List Weekends;       //休息日是工作日的列表
    private List Workdays;       //工作日是休息日的列表
    private int year;

    public List getWeekends() {
        return Weekends;
    }

    public void setWeekends(List weekends) {
        Weekends = weekends;
    }

    public List getWorkdays() {
        return Workdays;
    }

    public void setWorkdays(List workdays) {
        Workdays = workdays;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
