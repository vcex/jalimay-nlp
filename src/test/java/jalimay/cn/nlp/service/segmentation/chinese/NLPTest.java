package jalimay.cn.nlp.service.segmentation.chinese;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Test;

import edu.stanford.nlp.dcoref.CoNLL2011DocumentReader.NamedEntityAnnotation;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.ChineseLexiconAndWordSegmenter;
import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class NLPTest {
	Logger log = Logger.getLogger(getClass());

	@Test
	public void pipeline() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		log.info("---------------------------------------");
		String text = "FENDI/芬迪女士钱包8M002400A05 F0WN1";
		log.info("---------------------------------------");
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			log.info("sentence:" + sentence);
			log.info("---------------------------------------");
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class); // text
				log.info(word);
				token.get(PartOfSpeechAnnotation.class);// POS tag
				token.get(NamedEntityAnnotation.class); // NER label
			}
			// Tree tree = sentence.get(TreeAnnotation.class);
			sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
		}
		document.get(CorefChainAnnotation.class);
	}

	@Test
	public void split() {
		Options op = new Options();
		String parserFileOrUrl = getClass().getResource("/data/ctb.gz").getPath();
		ChineseLexiconAndWordSegmenter clas = ChineseLexiconAndWordSegmenter.getSegmenterDataFromFile(parserFileOrUrl,
				op);
		List<HasWord> hws = clas.segment("天天向上");
		for (HasWord hw : hws) {
			log.info(hw.word());
		}
	}
}
