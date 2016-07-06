package gabriele.bargaining;

import gabriele.Context;

public class AProductDemand{
	int absoluteRank,relativeRank;
	double demand;
	public AProductDemand(int ar,int rr,double dem){
		absoluteRank=ar;
		relativeRank=rr;
		demand=dem;
	}

	public void inform(int id){
		if(Context.verboseFlag){
			System.out.println("        consumer "+id+" new order of product with absolute rank "+absoluteRank+" and relative rank "+relativeRank+" demanded quantity "+demand);
		}
	}

	public void adjustDemand(int id,double factor){
		demand=demand*factor;
		if(Context.verboseFlag){
			System.out.println("        consumer "+id+" revised demand of product with absolute rank "+absoluteRank+" and relative rank "+relativeRank+" demanded quantity "+demand+" previously demanded "+(demand/factor));
		}
	}

	public double getDemand(){
		return demand;
	}

}
