package jalimay.cn.nlp.service.segmentation.chinese;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.core.io.Resource;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;

import jalimay.cn.nlp.service.segmentation.SegmentWordService;

public class SegmentWordServiceByMmseg4j implements SegmentWordService {
	Resource dic;
	Analyzer ana;

	public void setDic(Resource dic) {
		this.dic = dic;
	}

	public void init() throws IOException {
		ana = new ComplexAnalyzer(dic.getURI().getPath());
	}

	@Override
	public List<String> segment(String sentence) {
		try {
			TokenStream stream = ana.tokenStream("text", sentence);
			stream.reset();

			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			List<String> words = new ArrayList<String>();
			while (stream.incrementToken()) {
				String token = cta.toString();
				words.add(token);
			}
			stream.close();
			return words;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
		}
	}

}
