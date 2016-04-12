package jalimay.cn.nlp.service.classification.word2vec;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-config.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
public class VectorWordTest {
	Logger log = Logger.getLogger(getClass());
	@Resource
	VectorWord vectorWord;

	@Test
	public void testVec() throws IOException {
		double[] vec = vectorWord.vec("黑色");
		log.info(Arrays.toString(vec));
		Assert.assertEquals(1, vec[3], 0.00001);
		Assert.assertEquals(2, vec[4], 0.00001);
	}

	@Test
	public void letters() throws IOException {
		String word = "黑色";
		for (char c : word.toCharArray())
			log.info(c + ":" + vectorWord.index(c));
	}
}
