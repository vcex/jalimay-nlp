package jalimay.cn.nlp.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jalimay.cn.nlp.domain.WordEntry;
import jalimay.cn.nlp.service.classification.ClassifyWordService;
import jalimay.cn.nlp.service.segmentation.SegmentWordService;

@Controller
public class WordController {
	@Resource(name = "segmentWordServiceMmseg")
	SegmentWordService segmentWordService;
	@Resource(name = "classifyWordServiceByRBM")
	ClassifyWordService classifyWordService;

	@RequestMapping("word/segment.show")
	public String segment(Model view) {
		return "word/segment";
	}

	@RequestMapping("word/segment.do")
	public String segment(Model view, String sentence) throws UnsupportedEncodingException {
		// String str = new String(sentence.getBytes("ISO-8859-1"), "utf-8");
		view.addAttribute("sentence", sentence);
		List<String> words = segmentWordService.segment(sentence);
		List<WordEntry> wordEntries = new ArrayList<WordEntry>();
		for (String word : words) {
			WordEntry we = classifyWordService.matchClass(word);
			wordEntries.add(we);
		}
		view.addAttribute("wordEntries", wordEntries);
		return "word/segment";
	}

	@RequestMapping("word/classify.do")
	public String classify(String word, Model view) {
		view.addAttribute("word", word);
		view.addAttribute("classMap", classifyWordService.classify(word));
		return "word/classify";
	}
}
