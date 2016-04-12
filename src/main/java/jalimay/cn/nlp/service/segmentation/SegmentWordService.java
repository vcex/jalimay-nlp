package jalimay.cn.nlp.service.segmentation;

import java.util.List;

/**
 * 分词服务
 * 
 * @author xiewei
 *
 */
public interface SegmentWordService {
	/**
	 * 分词
	 * 
	 * @param sentence
	 * @return
	 */
	public List<String> segment(String sentence);
}
