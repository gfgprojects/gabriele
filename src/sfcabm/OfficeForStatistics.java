package sfcabm;

import java.util.ArrayList;
import java.util.Iterator;
import repast.simphony.data2.AggregateDSCreator;
import repast.simphony.data2.AggregateDataSource;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.engine.schedule.DefaultActionFactory;
import repast.simphony.engine.schedule.IAction;

import sfcabm.Firm;

public class OfficeForStatistics{
	repast.simphony.context.Context<Object> myContext;
	AggregateDataSource maximumAbsoluteRankDataSource,minimumAbsoluteRankDataSource;
	public static ArrayList<Industry> industriesList = new ArrayList<Industry>();

	double maximumAbsoluteRank,minimumAbsoluteRank;
	IndexedIterable<Object> firmsList,consumersList;

	Firm aFirm;
	Industry anIndustry;
	Iterator<Industry> industriesListIterator;

	DefaultActionFactory statActionFactory;
	IAction statAction;

	public OfficeForStatistics(repast.simphony.context.Context<Object> con){
		myContext=con;

		AggregateDSCreator absoluteRankDScreator = new AggregateDSCreator(new AbsoluteRankNonAggregateDataSource());
		maximumAbsoluteRankDataSource= absoluteRankDScreator.createMaxSource("maximum absolute rank");
		minimumAbsoluteRankDataSource= absoluteRankDScreator.createMinSource("minimum absolute rank");

		statActionFactory = new DefaultActionFactory();
	}

	public void computeVariables(){
		maximumAbsoluteRankDataSource.reset();
		minimumAbsoluteRankDataSource.reset();
		try{
			maximumAbsoluteRank=(double)maximumAbsoluteRankDataSource.get(myContext.getObjects(Class.forName("sfcabm.Firm")),0);
			minimumAbsoluteRank=(double)minimumAbsoluteRankDataSource.get(myContext.getObjects(Class.forName("sfcabm.Firm")),0);
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}
		if(Context.verbousFlag){
			System.out.println("maximum absolute rank "+maximumAbsoluteRank+" minimum Ansolute Rank "+minimumAbsoluteRank);
		}
		

		
		for(int i=0;i<(int)(maximumAbsoluteRank-minimumAbsoluteRank+1);i++){
			industriesList.add(new Industry((int)(i+minimumAbsoluteRank)));
		}
		
		try{
			firmsList=myContext.getObjects(Class.forName("sfcabm.Firm"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}

		for(int i=0;i<firmsList.size();i++){
			aFirm=(Firm)firmsList.get(i);
			int position=(int)(aFirm.getProductAbsoluteRank()-minimumAbsoluteRank);
			anIndustry=industriesList.get(position);
			anIndustry.increaseNumberOfFirms();
			anIndustry.increaseProduction(aFirm.getProduction());
		}
		industriesListIterator=industriesList.iterator();
		while(industriesListIterator.hasNext()){
			anIndustry=industriesListIterator.next();
			if(anIndustry.getProduction()==0){
				industriesListIterator.remove();
			}
		}

		if(Context.verbousFlag){
			System.out.println("STATS OFFICE: NEW DATA FROM INDUSTRIES ARE AVAILABLE");
			industriesListIterator=industriesList.iterator();
			while(industriesListIterator.hasNext()){
				anIndustry=industriesListIterator.next();
				System.out.println("absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction());
			}
		}



	}


	public void publishIndustriesStats(){
		try{
			consumersList=myContext.getObjects(Class.forName("sfcabm.Consumer"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}


		statAction=statActionFactory.createActionForIterable(consumersList,"showInfoOnIndustries",false);
		statAction.execute();
	}
}
