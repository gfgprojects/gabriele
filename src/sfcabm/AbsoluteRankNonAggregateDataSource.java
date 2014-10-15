package sfcabm;

import sfcabm.Firm;

import repast.simphony.data2.NonAggregateDataSource;

public class AbsoluteRankNonAggregateDataSource implements NonAggregateDataSource{
@Override
	public Object get(Object firm){
		Firm aFirm=(Firm)firm;
		return new Double(aFirm.getProductAbsoluteRank());
	}
	public Class getSourceType(){
		return (Firm.class);
	}
	public Class getDataType(){
		return Integer.class;
	}
	public String getId(){
		return "AbsoluteRankNonAggregateDataSource";
	}

}
