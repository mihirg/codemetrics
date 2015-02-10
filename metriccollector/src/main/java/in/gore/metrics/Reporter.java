package in.gore.metrics;



import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.analysis.IPackageCoverage;
import org.jacoco.core.analysis.ISourceFileCoverage;
import org.jacoco.core.tools.ExecFileLoader;
import org.objectweb.asm.ClassReader;
public class Reporter {

	private static Options  getOptions() {
		Options options = new Options();
		
		Option help = new Option("h", "help",false,"Help");
		options.addOption(help);
		Option classes = new Option("c", "classes", true, "Classes Directory");
		options.addOption(classes);
		Option srcFile = new Option ("s", "source", true, "Source file location");
		options.addOption(srcFile);
		Option outputFile = new Option("o", "output", true, "Output file location");
		options.addOption(outputFile);
		return options;		
	}
	
	private static void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Main", getOptions());
		System.exit(0);
	}
	
	public static void main(String[] args) throws IOException {
		String srcFile = null;
		String outputFile = null;
		String classes = null;
		try {
			CommandLineParser parser = new BasicParser();
			CommandLine cmd = parser.parse(getOptions(), args);
			
			if (cmd.hasOption("h"))
				printHelp();
			if (cmd.hasOption("c"))
				classes = cmd.getOptionValue("c");
			if (cmd.hasOption("s"))
				srcFile = cmd.getOptionValue("s");
			if (cmd.hasOption("o"))
				outputFile = cmd.getOptionValue("o");
			if (classes == null || srcFile == null || outputFile == null)
				printHelp();
			
		} catch (Exception exp) {
			
		}
		
		ExecFileLoader execFile = new ExecFileLoader();
		try {
			execFile.load(new FileInputStream(srcFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CoverageBuilder coverageBuilder = new CoverageBuilder();
		Analyzer analyzer = new Analyzer(execFile.getExecutionDataStore(), coverageBuilder);
		analyzer.analyzeAll(new File(classes));
		IBundleCoverage bundle = coverageBuilder.getBundle("test");
		Collection<IPackageCoverage> packageCollection = bundle.getPackages();
		Iterator<IPackageCoverage> iter = packageCollection.iterator();
		CombinedMetrics combinedMetrics = new CombinedMetrics();
		while (iter.hasNext()) {
			IPackageCoverage pack = iter.next();
			Collection<ISourceFileCoverage> srcFiles = pack.getSourceFiles();
			Iterator<ISourceFileCoverage> srcIter = srcFiles.iterator();
			while (srcIter.hasNext()) {
				ISourceFileCoverage srcCov = srcIter.next();
				ClassMetrics classMetrics = new ClassMetrics(srcCov.getName(), srcCov.getPackageName());
				classMetrics.getMetrics().add(new Metric("Method Coverage", srcCov.getMethodCounter().getCoveredCount(),srcCov.getMethodCounter().getMissedCount()));
				classMetrics.getMetrics().add(new Metric("Branch Coverage", srcCov.getBranchCounter().getCoveredCount(),srcCov.getBranchCounter().getMissedCount()));
				classMetrics.getMetrics().add(new Metric("Instruction Counter", srcCov.getInstructionCounter().getCoveredCount(),srcCov.getInstructionCounter().getMissedCount()));
				classMetrics.getMetrics().add(new Metric("Complexity Counter", srcCov.getComplexityCounter().getCoveredCount(),srcCov.getComplexityCounter().getMissedCount()));
				classMetrics.getMetrics().add(new Metric("Line Counter", srcCov.getLineCounter().getCoveredCount(),srcCov.getLineCounter().getMissedCount()));
				combinedMetrics.getClassMetrics().add(classMetrics);
				
			}
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(CombinedMetrics.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			marshaller.marshal(new JAXBElement(new QName(CombinedMetrics.class.getSimpleName()), CombinedMetrics.class, combinedMetrics), new File(outputFile));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
