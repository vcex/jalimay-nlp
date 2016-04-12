package jalimay.cn.nlp.service.classification.word2vec;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import jalimay.cn.nlp.domain.WordEntry;
import jalimay.cn.nlp.domain.WordEntry.WordClass;
import jalimay.cn.nlp.service.classification.ClassifyWordService;

public class ClassifyWordServiceByRBM implements ClassifyWordService {
	private double probabilityThreshold = 0.3;
	private VectorWord vectorWord;
	private MultiLayerNetwork model;
	private TrainRBM trainRBM;

	public void setVectorWord(VectorWord vectorWord) {
		this.vectorWord = vectorWord;
	}

	public void setTrainRBM(TrainRBM trainRBM) {
		this.trainRBM = trainRBM;
	}

	public void load() throws FileNotFoundException, IOException, ClassNotFoundException {
		this.model = trainRBM.load();
	}

	@Override
	public WordEntry matchClass(String word) {
		Map<WordClass, Double> w = classify(word);
		double maxOut = 0;
		WordClass maxWc = null;
		for (Entry<WordClass, Double> out : w.entrySet()) {
			if (Double.compare(maxOut, out.getValue()) < 0) {
				maxOut = out.getValue();
				maxWc = out.getKey();
			}
		}
		if (maxOut > probabilityThreshold) {
			return new WordEntry(word, maxWc, maxOut);
		} else
			return new WordEntry(word, null, 0);
	}

	@Override
	public Map<WordClass, Double> classify(String word) {
		double[] vec = this.vectorWord.vec(word);
		INDArray ps = this.model.output(Nd4j.create(vec));
		Map<WordClass, Double> m = new HashMap<WordClass, Double>();
		for (int i = 0; i < ps.length(); i++) {
			m.put(WordClass.get(i), ps.getDouble(i));
		}
		return m;
	}

}
