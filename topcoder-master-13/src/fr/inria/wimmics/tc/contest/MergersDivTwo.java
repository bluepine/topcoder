package fr.inria.wimmics.tc.contest;
//srm536.div2
//not correct solution
import java.util.Arrays;
import java.util.Collections;

public class MergersDivTwo {
	/*public double findMaximum(int[] revenues, int k) {
		
		Arrays.sort(revenues);
		
		double dp[][] = new double[revenues.length+1][revenues.length+1];
		
		dp[0][0] = 0;
		for(int i=1;i<=revenues.length;i++) {
			dp[i][0] = (dp[i-1][0]*(i-1) + (double)revenues[i-1])/(double)i;
		}
		
		for(int i=1;i<=revenues.length;i++) {
			for(int j=1;j<=revenues.length;j++) {
				if(i+j-1<revenues.length)
					dp[i][j] = (dp[i][j-1]*j + revenues[i+j-1])/(j+1);
				else
					dp[i][j] = dp[i][j-1];
			}
		}
		double max = dp[k][revenues.length];
		for(int i=k;i<=revenues.length;i++) {
			if(max<dp[i][revenues.length]) {
				max  = dp[i][revenues.length];
			}
		}
		
		
		return max;
	}*/
	
	double getSume(int[] revenues, int i, int j) {
		double sum = 0.0;
		for(int ii=i;ii<=j;ii++) {
			sum += revenues[ii];
		}
		return sum;
	}
	
	public double findMaximum(int[] revenues, int k) {
		
		int n = revenues.length;
		
		for(int i=k-1;i<n;i++) {
			double part1 = getSume(revenues, 0, i);
			//part2 = 0.0;
			for(int j=k;j<n && i+1<n;j++) {
				double part2 = getSume(revenues,i+1,k);
			}
		}
		
		return 0.0;
	}
}
