package fr.inria.wimmics.tc.contest;
//srm536.div2
public class BinaryPolynomialDivTwo {

	int computePoly(int[] a, int x) {
		
		int sum = 0;
		for(int i=0;i<a.length;i++) {
			int xVal = (int) Math.pow(x,i);
			int temp = xVal * a[i];
			sum += temp;
		}
		
		return sum%2;
	}
	
	public int countRoots(int[] a) {
		
		int count = 0;
		
		int resX0 = computePoly(a, 0);
		int resX1 = computePoly(a, 1);
		if(resX0==0) count++;
		if(resX1==0) count++;
		return count;
	}
}
