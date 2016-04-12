package jalimay.cn.nlp.domain;

public class WordEntry {
	String word;
	WordClass firstClass;
	double firstClassProbability;

	public WordEntry() {
	}

	public WordEntry(String w, WordClass c, double p) {
		this.word = w;
		this.firstClass = c;
		this.firstClassProbability = p;
	}

	@Override
	public String toString() {
		if (firstClass == null)
			return word + ":[]";
		return word + ":[" + firstClass.name() + "]";
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public WordClass getFirstClass() {
		return firstClass;
	}

	public void setFirstClass(WordClass firstClass) {
		this.firstClass = firstClass;
	}

	public double getFirstClassProbability() {
		return firstClassProbability;
	}

	public void setFirstClassProbability(double firstClassProbability) {
		this.firstClassProbability = firstClassProbability;
	}

	/**
	 * 词性
	 * 
	 * @author xiewei
	 *
	 */
	public static enum WordClass {
		// 材料
		material(0),
		// 尺寸
		size(1),
		// 风格
		style(2),
		// 功能
		feature(3),
		// 花纹
		pattern(4),
		// 机芯
		mechanism(5),
		// 季节
		season(6),
		// 性别
		gender(7),
		// 颜色
		color(8);

		final int index;

		public int getIndex() {
			return index;
		}

		WordClass(int index) {
			this.index = index;
		}

		public static WordClass get(int index) {
			for (WordClass wc : WordClass.values()) {
				if (wc.index == index) {
					return wc;
				}
			}
			return null;
		}
	}
}
