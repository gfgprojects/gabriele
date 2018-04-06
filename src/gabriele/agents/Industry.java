/*
GABRIELE: the General Agent Based Repast Implemented Extensible Laboratory for Economics
Copyright (C) 2018  Gianfranco Giulioni
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package gabriele.agents;

import java.util.ArrayList;

import gabriele.Context;

public class Industry{
	int absoluteRank,relativeRank,numberOfFirms;
	double production,marketShare,weightedProduction,productAttractivenesIndicator,demand,investments;
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
	public void decreaseNumberOfFirms(){
		numberOfFirms--;
	}
	public void increaseProduction(double fp){
		production=production+fp;
	}
	public void increaseDemand(double dem){
		demand=demand+dem;
	}
	public void allocateDesiredDemand(){
		if(Context.verboseFlag){
			System.out.println("       Industry with Absolute Rank "+absoluteRank+" Relative Rank "+relativeRank+" number of firms "+theIndustryFirmsList.size()+" production "+production+" demand from howsehold "+demand+" demand from firms "+investments);
		}
		for(int i=0;i<theIndustryFirmsList.size();i++){
			aFirm=theIndustryFirmsList.get(i);
			aFirm.setDesiredDemand(production,demand);
		}
	}

	public void allocateDemand(){
		if(Context.verboseFlag){
			System.out.println("       Industry with Absolute Rank "+absoluteRank+" Relative Rank "+relativeRank+" number of firms "+theIndustryFirmsList.size()+" production "+production+" demand from howsehold "+demand+" demand from firms "+investments);
//			System.out.println("       Industry with Absolute Rank "+absoluteRank+" Relative Rank "+relativeRank+" number of firms "+theIndustryFirmsList.size()+" production "+production+" demand "+demand);
		}
		for(int i=0;i<theIndustryFirmsList.size();i++){
			aFirm=theIndustryFirmsList.get(i);
			aFirm.setDemand(production,demand);
		}
	}

	public void allocateInvestments(){
		if(Context.verboseFlag){
			System.out.println("       Industry with Absolute Rank "+absoluteRank+" Relative Rank "+relativeRank+" number of firms "+theIndustryFirmsList.size()+" production "+production+" demand from household "+demand+" demand from firms "+investments);
		}
		for(int i=0;i<theIndustryFirmsList.size();i++){
			aFirm=theIndustryFirmsList.get(i);
			aFirm.setOrdersOfProductsForInvestmentPurpose(production,investments);
		}
	}







	public void addFirm(Firm af){
		af.setIndustry(this);
		theIndustryFirmsList.add(af);
		if(Context.verboseFlag){
			if(relativeRank<1){
			System.out.println("       Added Firm in Industry with Absolute Rank "+absoluteRank+" Relative Rank not yet computed  number of firms "+theIndustryFirmsList.size());
			}
			else{
			System.out.println("       Added Firm in Industry with Absolute Rank "+absoluteRank+" Relative Rank "+relativeRank+" number of firms "+theIndustryFirmsList.size());
			}
		}
	}

	public void removeFirm(Firm rf){
		theIndustryFirmsList.remove(rf);
		if(theIndustryFirmsList.size()<1){
			production=0;
			demand=0;
		}
		if(Context.verboseFlag){
			System.out.println("       Removed Firm in Industry with Absolute Rank "+absoluteRank+" Relative Rank "+relativeRank+" number of firms "+theIndustryFirmsList.size());
		}
	}

	public void setRelativeRank(int mar){
		relativeRank=absoluteRank-mar+1;
	}
	public void setMarketShare(double ap){
		marketShare=demand/ap;
		weightedProduction=relativeRank*demand;
	}
	public void setProductDiffusionIndicator(double twp){
//		productAttractivenesIndicator=production*relativeRank/twp;
		productAttractivenesIndicator=demand*relativeRank/twp;
		if(Context.verboseFlag){
			System.out.println("     Industry with AR "+absoluteRank+" RR "+relativeRank+" market share "+marketShare+" product attractiveness indicator "+productAttractivenesIndicator);
		}
	}
	public double getProduction(){
		return production;
	}
	public double getDemand(){
		return demand;
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

	public void resetDemand(){
		demand=0;
	}
	public void setInvestments(double invest){
		investments=invest;
	}
	public double getInvestments(){
		return investments;
	}



}
