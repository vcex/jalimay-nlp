package jalimay.cn.nlp.service.classification.word2vec;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import jalimay.cn.nlp.service.classification.ClassifyWordService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-config.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
public class ClassifyWordServiceByRBMTest {
	@Resource(name = "classifyWordServiceByRBM")
	ClassifyWordService classifyWordService;
	Logger log = Logger.getLogger(getClass());

	@Test
	public void test() {
		String[] words = { "黑色", "黑", "红", "红色", "牛皮", "时尚", "红牛", "黑钻石", "色狼", "橘黄", "桔黄", "橘黄色", "黄", "黄色", "帆船",
				"藏青", "蓝", "经典", "连衣裙" };
		for (String word : words)
			log.info(classifyWordService.matchClass(word));
	}

}
