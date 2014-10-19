package sfcabm;

public class AProductDemand{
	int absoluteRank,relativeRank,demand;
	public AProductDemand(int ar,int rr,int dem){
		absoluteRank=ar;
		relativeRank=rr;
		demand=dem;
	}

	public void inform(int id){
		if(Context.verbousFlag){
			System.out.println("   consumer "+id+" new order of product with absolute rank "+absoluteRank+" and relative rank "+relativeRank+" demanded quantity "+demand);
		}
	}

	public int getDemand(){
		return demand;
	}

}
