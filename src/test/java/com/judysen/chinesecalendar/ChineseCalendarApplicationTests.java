package com.judysen.chinesecalendar;

import com.alibaba.fastjson.JSON;
import com.judysen.chinesecalendar.config.CustomConfig;
import com.judysen.chinesecalendar.core.ChineseWorkDayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
//@TestPropertySource(locations="classpath:application.properties")
@SpringBootTest
@PropertySource("classpath:application.properties")
public class ChineseCalendarApplicationTests {
	@Autowired
	private ChineseWorkDayUtils chineseWorkDayUtils;
	@Autowired
	private CustomConfig config;
	@Test
	public void contextLoads() {
	}
	@Test
	public void getAllHolidays() throws Exception{
		chineseWorkDayUtils.init();
		List<String> holidays=chineseWorkDayUtils.holidaysInYear(2018);
		System.out.println(JSON.toJSONString(holidays));
		System.out.println(holidays.size());
	}
}
