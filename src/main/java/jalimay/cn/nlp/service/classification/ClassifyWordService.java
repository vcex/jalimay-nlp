package jalimay.cn.nlp.service.classification;

import java.util.Map;

import jalimay.cn.nlp.domain.WordEntry;
import jalimay.cn.nlp.domain.WordEntry.WordClass;

/**
 * 词性分类
 * 
 * @author xiewei
 *
 */
public interface ClassifyWordService {
	/**
	 * 反馈该词具有指定词性
	 * 
	 * @param word
	 * @return 词性 {@link WordClass}: maybe null
	 */
	public WordEntry matchClass(String word);

	/**
	 * 反馈该词在各个词性维度上的概率
	 * 
	 * @param word
	 * @return
	 */
	public Map<WordClass, Double> classify(String word);

}
