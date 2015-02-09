package in.gore.sample;

public class AverageCompute {
	
	static float computeAverage(int[][] inputArray) throws Exception {
		if (inputArray == null)
			throw new Exception("Input array is null");
		int numRows = inputArray.length;
		if (numRows <= 0)
			throw new Exception("Input array is empty");
		float maxAvg = 0;
		for (int i = 0; i<numRows; i++) {
			int arr[] = inputArray[i];
			if (arr.length <= 0)
				throw new Exception ("Array Contains empty row");
			float sum = 0;
			for (int j = 0; j < arr.length; j++) {
				sum += arr[j];
			}
			float avg = sum/(float)arr.length;
			if (i == 0)
				maxAvg = avg;
			else {
				if (avg > maxAvg)
					maxAvg = avg;
			}
		}
		return maxAvg;
	}

}
