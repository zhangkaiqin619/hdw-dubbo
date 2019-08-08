package com.hdw.test;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Maps;
import com.hdw.common.result.PageParams;
import com.hdw.sms.service.ISysSmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

	@Reference
	private ISysSmsService smsService;

	@Test
	public void testSmsPage(){
		Map<String,Object> params= Maps.newHashMap();
		params.put("page","1");
		params.put("limit","10");
		params.put("typeName","MQ消息");
		PageParams pageParams=smsService.selectDataGrid(new PageParams(params));
		if(ObjectUtils.isNotEmpty(pageParams)){
			System.out.println(pageParams.getTotalCount());
			System.out.println(pageParams.getList());
		}
	}


}
