package in.gore.sample;

import static org.junit.Assert.*;

import org.junit.Test;

public class AverageComputeTest {

	@Test
	public void basic() {
		
		int [][] input = new int[2][];
		for (int i = 0; i< 2; i++)
			input[i] = new int[2];
		input[0][0] = 1;
		input[0][1] = 2;
		input[1][0] = 3;
		input[1][1] = 4;
		try {
			float avg = AverageCompute.computeAverage(input);
			assertTrue("Max average should be 3.5", avg == 3.5);
		} catch (Exception exp) {
			fail("Simple test case fails");
		}
		
	}
	
}
