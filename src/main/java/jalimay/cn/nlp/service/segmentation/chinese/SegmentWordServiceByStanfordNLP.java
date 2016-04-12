package jalimay.cn.nlp.service.segmentation.chinese;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifierFloat;
import edu.stanford.nlp.ie.crf.CRFClassifierNoisyLabel;
import edu.stanford.nlp.ie.crf.CRFClassifierNonlinear;
import edu.stanford.nlp.ie.crf.CRFClassifierWithDropout;
import edu.stanford.nlp.ie.crf.CRFClassifierWithLOP;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.sequences.SeqClassifierFlags;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.international.pennchinese.ChineseTreebankLanguagePack;
import edu.stanford.nlp.util.StringUtils;
import jalimay.cn.nlp.service.segmentation.SegmentWordService;

/**
 * 用Stanford-core-nlp分词
 * 
 * @author xiewei
 *
 */
public class SegmentWordServiceByStanfordNLP implements SegmentWordService {
	private CRFClassifier<CoreLabel> crf;
	private LexicalizedParser lp;

	private SegmentWordServiceByStanfordNLP() {
	}

	public static SegmentWordServiceByStanfordNLP make(List<String> args)
			throws ClassCastException, ClassNotFoundException, IOException {
		return make(args.toArray(new String[] {}));
	}

	public static SegmentWordServiceByStanfordNLP make(String[] args)
			throws ClassCastException, ClassNotFoundException, IOException {
		StringUtils.printErrInvocationString("CRFClassifier", args);
		Properties props = StringUtils.argsToProperties(args);
		SeqClassifierFlags flags = new SeqClassifierFlags(props);
		return make(props, flags);
	}

	public static SegmentWordServiceByStanfordNLP make(Properties props, SeqClassifierFlags flags)
			throws ClassCastException, ClassNotFoundException, IOException {
		SegmentWordServiceByStanfordNLP ss = new SegmentWordServiceByStanfordNLP();
		if (flags.useFloat) {
			ss.crf = new CRFClassifierFloat<CoreLabel>(flags);
		} else {
			if (flags.nonLinearCRF) {
				ss.crf = new CRFClassifierNonlinear<CoreLabel>(flags);
			} else {
				if (flags.numLopExpert > 1) {
					ss.crf = new CRFClassifierWithLOP<CoreLabel>(flags);
				} else {
					if (flags.priorType.equals("DROPOUT")) {
						ss.crf = new CRFClassifierWithDropout<CoreLabel>(flags);
					} else {
						if (flags.useNoisyLabel) {
							ss.crf = new CRFClassifierNoisyLabel<CoreLabel>(flags);
						} else
							ss.crf = new CRFClassifier<CoreLabel>(flags);
					}
				}
			}
		}
		String strLoadPath = flags.loadClassifier;
		ss.crf.loadClassifier(strLoadPath, props);

		ss.lp = LexicalizedParser.getParserFromSerializedFile(props.getProperty("serDictionary"));
		return ss;
	}

	public List<String> segment(String sentence) {
		String documents = this.crf.classifyToString(sentence, "tabbedEntities", false);
		List<String> words = new ArrayList<String>();
		CollectionUtils.addAll(words, documents.trim().split(" "));
		return words;
	}

	public void parse(List<String> tokens) {
		if (lp != null) {
			List<Word> words = new ArrayList<Word>();
			for (String word : tokens) {
				words.add(new Word(word));
			}
			Tree parse = (Tree) lp.apply(words);
			// TreebankLanguagePack tlp = new PennTreebankLanguagePack();
			// GrammaticalStructureFactory gsf =
			// tlp.grammaticalStructureFactory();
			// GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
			// Collection<TypedDependency> tdl =
			// gs.typedDependenciesCollapsed();
			// System.out.println(tdl);
			TreePrint tp = new TreePrint("wordsAndTags,penn,typedDependenciesCollapsed",
					new ChineseTreebankLanguagePack());
			tp.printTree(parse);
		}
	}

}

class Word implements HasWord {
	private static final long serialVersionUID = 1L;
	private String w;

	public Word(String word) {
		this.w = word;
	}

	public String word() {
		return w;
	}

	public void setWord(String word) {
		this.w = word;
	}

}