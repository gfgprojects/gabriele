package sfcabm;

public class Industry{
	int absoluteRank,relativeRank,numberOfFirms;
	double production,marketShare,weightedProduction,productAttractivenesIndicator;

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
	public void setRelativeRank(int mar){
		relativeRank=absoluteRank-mar+1;
	}
	public void setMarketShare(double ap){
		marketShare=production/ap;
		weightedProduction=relativeRank*production;
	}
	public void setProductDiffusionIndicator(double twp){
		productAttractivenesIndicator=production*relativeRank/twp;
		System.out.println("AR "+absoluteRank+" RR "+relativeRank+" mark share "+marketShare+" product attractiveness indicator "+productAttractivenesIndicator);
	}
	public double getProduction(){
		return production;
	}
	public int getAbsoluteRank(){
		return absoluteRank;
	}
	public int getRelativeRank(){
		return relativeRank;
	}
	public int getNumberOfFirms(){
		return numberOfFirms;
	}
	public double getWeightedProduction(){
		return weightedProduction;
	}
	public double getProductAttractiveness(){
		return productAttractivenesIndicator;
	}



}
