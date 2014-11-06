package sfcabm;

import java.util.ArrayList;

public class Industry{
	int absoluteRank,relativeRank,numberOfFirms;
	double production,marketShare,weightedProduction,productAttractivenesIndicator,demand;
	ArrayList<Firm> theIndustryFirmsList=new ArrayList<Firm>();
	Firm aFirm;

	public Industry(int ar){
		absoluteRank=ar;
		numberOfFirms=0;
		production=0;
		demand=0;
	}
	public void increaseNumberOfFirms(){
		numberOfFirms++;
	}
	public void increaseProduction(double fp){
		production=production+fp;
	}
	public void increaseDemand(double dem){
		demand=demand+dem;
	}

	public void allocateDemand(){
		if(Context.verboseFlag){
			System.out.println("       Industry with Absolute Rank "+absoluteRank+" Relative Rank "+relativeRank+" number of firms "+theIndustryFirmsList.size()+" production "+production+" demand "+demand);
		}
		for(int i=0;i<theIndustryFirmsList.size();i++){
			aFirm=theIndustryFirmsList.get(i);
			aFirm.setDemand(production,demand);
		}
	}
	public void addFirm(Firm af){
		theIndustryFirmsList.add(af);
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
		if(Context.verboseFlag){
			System.out.println("     Industry with AR "+absoluteRank+" RR "+relativeRank+" market share "+marketShare+" product attractiveness indicator "+productAttractivenesIndicator);
		}
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
