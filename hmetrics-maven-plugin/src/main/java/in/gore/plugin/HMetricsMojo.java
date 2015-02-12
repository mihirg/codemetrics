package in.gore.plugin;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.analysis.IPackageCoverage;
import org.jacoco.core.analysis.ISourceFileCoverage;
import org.jacoco.core.tools.ExecFileLoader;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

/**
 * Goal which parses the jacoco .exec file and dumps statistics in an xml.
 *
 */
@Mojo( name = "hmetrics" )
public class HMetricsMojo extends AbstractMojo
{
    @Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = false )
    private String outputDirectory;

    @Parameter( defaultValue = "metrics.xml", property = "outputFileName", required = true)
    private String outputFile;

    @Parameter( defaultValue = "jacoco-unit.exec", property = "inputFile", required = true)
    private String inputFile;

    
    public void execute() throws MojoExecutionException
    { 
    	try {
        	
    		ExecFileLoader execFile = new ExecFileLoader();
    		try {
    			execFile.load(new FileInputStream(inputFile));
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		CoverageBuilder coverageBuilder = new CoverageBuilder();
    		Analyzer analyzer = new Analyzer(execFile.getExecutionDataStore(), coverageBuilder);
    		analyzer.analyzeAll(new File(outputDirectory));
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
    	catch (Exception exp) {
    		throw new MojoExecutionException(exp.getMessage());    		
    	}
    }
}
