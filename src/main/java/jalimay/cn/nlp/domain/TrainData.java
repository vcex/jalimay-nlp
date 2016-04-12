package jalimay.cn.nlp.domain;

import org.apache.commons.lang3.StringUtils;

/**
 * 训练数据
 * 
 * @author xiewei
 *
 */
public class TrainData {
	public static final int FEATURES = 9;
	String word;
	int count;
	double[] features;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double[] getFeatures() {
		return features;
	}

	public void setFeatures(double[] features) {
		this.features = features;
	}

	public static TrainData make(String data) {
		String[] items = data.split("\t");
		TrainData d = new TrainData();
		d.word = items[0];
		if (StringUtils.isNotEmpty(items[1]))
			d.count = Integer.valueOf(items[1]);
		d.features = new double[9];
		for (int i = 2; i <= 10; i++) {
			d.features[i - 2] = Double.valueOf(items[i]);
		}
		return d;
	}
}
