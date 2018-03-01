package com.lc.nlp4han.constituent.maxent;

import java.io.File;
import java.io.IOException;

import com.lc.nlp4han.constituent.HeadTreeNode;
import com.lc.nlp4han.ml.util.CrossValidationPartitioner;
import com.lc.nlp4han.ml.util.FileInputStreamFactory;
import com.lc.nlp4han.ml.util.ModelWrapper;
import com.lc.nlp4han.ml.util.ObjectStream;
import com.lc.nlp4han.ml.util.PlainTextByLineStream;
import com.lc.nlp4han.ml.util.TrainingParameters;
import com.lc.nlp4han.pos.word.POSTaggerWordME;

/**
 * 英文句法分析交叉验证运行类
 * @author 王馨苇
 *
 */
public class SyntacticAnalysisCVTool {

	private final String languageCode;

    private final TrainingParameters params;

    private SyntacticAnalysisEvaluateMonitor[] listeners;
    
    public SyntacticAnalysisCVTool(String languageCode,TrainingParameters trainParam,SyntacticAnalysisEvaluateMonitor... listeners){
    	this.languageCode = languageCode;
        this.params = trainParam;
        this.listeners = listeners;
    }
    
    /**
     * 十折交叉验证进行评估
     * @param samples 样本流
     * @param nFolds 几折交叉验证
     * @param contextGen 特征生成
     * @param aghw 生成头结点
     * @throws IOException
     */
    public void evaluate(ObjectStream<SyntacticAnalysisSample<HeadTreeNode>> samples, int nFolds, SyntacticAnalysisContextGenerator<HeadTreeNode> contextGen, AbstractGenerateHeadWords aghw) throws IOException{
    	CrossValidationPartitioner<SyntacticAnalysisSample<HeadTreeNode>> partitioner = new CrossValidationPartitioner<SyntacticAnalysisSample<HeadTreeNode>>(samples, nFolds);
		int run = 1;
		//小于折数的时候
		while(partitioner.hasNext()){
			System.out.println("Run"+run+"...");
			CrossValidationPartitioner.TrainingSampleStream<SyntacticAnalysisSample<HeadTreeNode>> trainingSampleStream = partitioner.next();
			ModelWrapper posmodel = new ModelWrapper(new File("data\\model\\pos\\en-pos-maxent.bin"));	
			ModelWrapper chunkmodel= SyntacticAnalysisMEForChunk.train(languageCode, trainingSampleStream, params, contextGen);
			trainingSampleStream.reset();
			ModelWrapper buildmodel = SyntacticAnalysisMEForBuildAndCheck.trainForBuild(languageCode, trainingSampleStream, params, contextGen);

			trainingSampleStream.reset();
			ModelWrapper checkmodel = SyntacticAnalysisMEForBuildAndCheck.trainForCheck(languageCode, trainingSampleStream, params, contextGen);

			POSTaggerWordME postagger = new POSTaggerWordME(posmodel);	
	        SyntacticAnalysisMEForChunk chunktagger = new SyntacticAnalysisMEForChunk(chunkmodel,contextGen, aghw);
	        SyntacticAnalysisMEForBuildAndCheck buildandchecktagger = new SyntacticAnalysisMEForBuildAndCheck(buildmodel,checkmodel,contextGen, aghw);
	        
			SyntacticAnalysisEvaluatorForByStep evaluator = new SyntacticAnalysisEvaluatorForByStep(postagger,chunktagger,buildandchecktagger, aghw, listeners);
			SyntacticAnalysisMeasure measure = new SyntacticAnalysisMeasure();
			
			evaluator.setMeasure(measure);
	        //设置测试集（在测试集上进行评价）
	        evaluator.evaluate(trainingSampleStream.getTestSampleStream());
	        
	        System.out.println(measure);
	        run++;
		}
    }
    
    private static void usage(){
    	System.out.println(SyntacticAnalysisCVTool.class.getName() + " -data <corpusFile> -encoding <encoding> -type <algorithm>" + "[-cutoff <num>] [-iters <num>] [-folds <nFolds>] ");
    }
    
    public static void main(String[] args) throws IOException {
    	if (args.length < 1)
        {
            usage();
            return;
        }

        int cutoff = 3;
        int iters = 100;
        int folds = 10;
        File corpusFile = null;
        String encoding = "UTF-8";
        String type = "MAXENT";
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-data"))
            {
                corpusFile = new File(args[i + 1]);
                i++;
            }
            else if (args[i].equals("-encoding"))
            {
                encoding = args[i + 1];
                i++;
            }
            else if (args[i].equals("-type"))
            {
                type = args[i + 1];
                i++;
            }
            else if (args[i].equals("-cutoff"))
            {
                cutoff = Integer.parseInt(args[i + 1]);
                i++;
            }
            else if (args[i].equals("-iters"))
            {
                iters = Integer.parseInt(args[i + 1]);
                i++;
            }
            else if (args[i].equals("-folds"))
            {
                folds = Integer.parseInt(args[i + 1]);
                i++;
            }
        }

        TrainingParameters params = TrainingParameters.defaultParams();
        params.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(cutoff));
        params.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(iters));
        params.put(TrainingParameters.ALGORITHM_PARAM, type.toUpperCase());
        
        SyntacticAnalysisContextGenerator<HeadTreeNode> contextGen = new SyntacticAnalysisContextGeneratorConf();
        AbstractGenerateHeadWords aghw = new ConcreteGenerateHeadWords();
        System.out.println(contextGen);
        ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStreamFactory(corpusFile), encoding);       
        ObjectStream<SyntacticAnalysisSample<HeadTreeNode>> sampleStream = new SyntacticAnalysisSampleStream(lineStream, aghw);
        SyntacticAnalysisCVTool run = new SyntacticAnalysisCVTool("zh",params);
        run.evaluate(sampleStream,folds,contextGen, aghw);
	}
}
