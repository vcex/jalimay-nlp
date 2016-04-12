package jalimay.cn.nlp.service.segmentation.chinese;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import jalimay.cn.nlp.service.segmentation.chinese.SegmentWordServiceByMmseg4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-config.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
public class SegmentWordServiceByMmseg4jTest {
	Logger log = Logger.getLogger(getClass());
	@Resource
	SegmentWordServiceByMmseg4j segmentServiceMmseg;

	@Test
	public void test() {
		List<String> words = segmentServiceMmseg.segment("Michael Kors/迈克·科尔斯 纯皮女款笑脸耳朵包大号");
		log.info(words);
	}

}
