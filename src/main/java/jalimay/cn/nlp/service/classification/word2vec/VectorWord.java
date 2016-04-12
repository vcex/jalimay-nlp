package jalimay.cn.nlp.service.classification.word2vec;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

public class VectorWord {
	private final Logger log = Logger.getLogger(getClass());
	private Map<Character, Integer> charIndex;
	private int charIndexSize = 0;
	private Resource dic;

	public void setDic(Resource dic) {
		this.dic = dic;
	}

	public void init() throws IOException {
		// 初始化字向量索引
		log.info("开始初始化字向量索引");
		Map<Character, Integer> charIndex = new HashMap<Character, Integer>();
		List<String> lines = IOUtils.readLines(dic.getInputStream());
		int i = 0;
		for (String line : lines) {
			for (char c : line.toCharArray())
				if (!charIndex.containsKey(c))
					charIndex.put(c, i++);
		}
		log.info("字向量索引大小:" + i);
		this.charIndex = charIndex;
		this.charIndexSize = charIndex.size();

	}

	/**
	 * 将词转为字向量，词中出现的字对应向量位置的值为1，其余位置为0
	 * 
	 * @param word
	 * @return
	 */
	public double[] vec(String word) {
		double[] vec = new double[charIndexSize];
		int i = 1;
		for (char c : word.toCharArray()) {
			if (charIndex.containsKey(c)) {
				int index = charIndex.get(c);
				log.debug(c + ":" + index);
				vec[index] = i++;
			}
		}
		return vec;
	}

	/**
	 * 得到向量长度
	 * 
	 * @return
	 */
	public int size() {
		return this.charIndexSize;
	}

	int index(char c) {
		return this.charIndex.get(c);
	}
}
