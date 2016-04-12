package jalimay.cn.nlp.service.classification.word2vec;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.deeplearning4j.datasets.fetchers.BaseDataFetcher;
import org.deeplearning4j.datasets.iterator.BaseDatasetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.core.io.Resource;

import jalimay.cn.nlp.domain.TrainData;
import jalimay.cn.nlp.service.classification.Train;

public class TrainRBM implements Train {
	Resource data;
	Resource trainedModel;
	private VectorWord vectorWord;

	public void setVectorWord(VectorWord vectorWord) {
		this.vectorWord = vectorWord;
	}

	public void setTrainedModel(Resource trainedModel) {
		this.trainedModel = trainedModel;
	}

	public void setData(Resource data) {
		this.data = data;
	}

	DataSet getDataSet() throws IOException {
		List<String> lines = IOUtils.readLines(data.getInputStream());
		int size = lines.size();
		double[][] features = new double[size][vectorWord.size()];
		double[][] labels = new double[size][TrainData.FEATURES];
		for (int i = 0; i < size; i++) {
			try {
				TrainData td = TrainData.make(lines.get(i));
				features[i] = vectorWord.vec(td.getWord());
				labels[i] = td.getFeatures();
			} catch (Exception e) {
				log.error("transform train data", e);
			}
		}
		DataSet ds = new DataSet(Nd4j.create(features), Nd4j.create(labels));
		return ds;
	}

	final Logger log = Logger.getLogger(TrainRBM.class);

	public MultiLayerNetwork newModel() {
		int inputNum = vectorWord.size();
		int outputNum = TrainData.FEATURES;
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(12345).iterations(1)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).learningRate(0.05)
				.regularization(true).l2(0.0001).list(3)
				.layer(0,
						new DenseLayer.Builder().nIn(inputNum).nOut(100).weightInit(WeightInit.XAVIER)
								.updater(Updater.ADAGRAD).activation("relu").build())
				.layer(1,
						new DenseLayer.Builder().nIn(100).nOut(10).weightInit(WeightInit.XAVIER)
								.updater(Updater.ADAGRAD).activation("relu").build())
				.layer(2,
						new OutputLayer.Builder().nIn(10).nOut(outputNum).weightInit(WeightInit.XAVIER)
								.updater(Updater.ADAGRAD).activation("relu")
								.lossFunction(LossFunctions.LossFunction.MSE).build())
				.pretrain(false).backprop(true).build();
		return new MultiLayerNetwork(conf);
	}

	public MultiLayerNetwork load() throws IOException, ClassNotFoundException {
		MultiLayerNetwork model = newModel();
		// Load the updater:
		org.deeplearning4j.nn.api.Updater updater;
		ObjectInputStream ois = new ObjectInputStream(trainedModel.getInputStream());
		updater = (org.deeplearning4j.nn.api.Updater) ois.readObject();
		model.setUpdater(updater);
		ois.close();
		return model;
	}

	@Override
	public void train() throws IOException {
		// Customizing params
		int iterations = 100;
		int listenerFreq = iterations / 2;
		log.info("Load data....");
		// Loads data into generator and format consumable for NN
		log.info("Build model....");
		MultiLayerNetwork model = newModel();
		model.init();
		model.setListeners(new ScoreIterationListener(listenerFreq));

		for (int i = 0; i < 3; i++)
			model.fit(getDataSet());
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.trainedModel.getFile()));
		oos.writeObject(model.getUpdater());
		oos.close();
	}

	@Deprecated
	public static class WordIterator extends BaseDatasetIterator {
		private static final long serialVersionUID = 1L;

		public WordIterator(BaseDataFetcher fetcher) {
			super(150, 150, fetcher);
		}
	}

	@Deprecated
	public static class WordFetcher extends BaseDataFetcher {
		private static final long serialVersionUID = 1L;
		Logger log = Logger.getLogger(getClass());
		Resource data;
		List<String> lines;
		private VectorWord vectorWord;

		public void setVectorWord(VectorWord vectorWord) {
			this.vectorWord = vectorWord;
		}

		public void setData(Resource data) throws IOException {
			this.data = data;
			this.lines = IOUtils.readLines(data.getInputStream());
			this.totalExamples = lines.size();
		}

		@Override
		public void fetch(int numExamples) {
			int from = cursor;
			int to = cursor + numExamples;
			if (to > totalExamples)
				to = totalExamples;
			initializeCurrFromList(to(this.lines.subList(from, to)));
			cursor += numExamples;
		}

		List<DataSet> to(List<String> lines) {
			List<DataSet> ds = new ArrayList<DataSet>();
			for (String line : lines) {
				try {
					TrainData td = TrainData.make(line);
					ds.add(new DataSet(Nd4j.create(vectorWord.vec(td.getWord())), Nd4j.create(td.getFeatures())));
				} catch (Exception e) {
					log.error("transform train data", e);
				}
			}
			return ds;
		}

	}
}
