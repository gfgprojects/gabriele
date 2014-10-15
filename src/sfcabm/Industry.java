package sfcabm;

public class Industry{
	int absoluteRank,numberOfFirms;
	double production;

	public Industry(int ar){
		absoluteRank=ar;
		numberOfFirms=0;
		production=0;
	}
	public void increaseNumberOfFirms(){
		numberOfFirms++;
	}
	public void increaseProduction(double fp){
		production=production+fp;
	}
	public double getProduction(){
		return production;
	}
	public int getAbsoluteRank(){
		return absoluteRank;
	}
	public int getNumberOfFirms(){
		return numberOfFirms;
	}



}
