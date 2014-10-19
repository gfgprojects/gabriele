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

	double maximumAbsoluteRank,minimumAbsoluteRank,aggregateProduction,totalWeightedProduction,aggregateDemand;
	IndexedIterable<Object> firmsList,consumersList;

	Firm aFirm;
	Consumer aConsumer;
	Industry anIndustry;
	Iterator<Industry> industriesListIterator;

	DefaultActionFactory statActionFactory;
	IAction statAction;
	ArrayList anOrderList;
	AProductDemand anOrder;

	int[] numberOfWokersInADegree;
	double[] totalProductivityOfWorkersInADegree;
	double[] averageProductivityOfWorkersInADegree;
	int numberOfWorkers=0;
	double totalProductivity=0;
	double averageProductivity=0;

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
//compute 1) aggregate production 2) number of firms in each industry and 3) production for each industry
		aggregateProduction=0;
		for(int i=0;i<firmsList.size();i++){
			aFirm=(Firm)firmsList.get(i);
			aggregateProduction=aggregateProduction+aFirm.getProduction();
			int position=(int)(aFirm.getProductAbsoluteRank()-minimumAbsoluteRank);
			anIndustry=industriesList.get(position);
			anIndustry.increaseNumberOfFirms();
			anIndustry.increaseProduction(aFirm.getProduction());
			anIndustry.addFirm(aFirm);
		}
//delete from the industries list those with zero production
		industriesListIterator=industriesList.iterator();
		while(industriesListIterator.hasNext()){
			anIndustry=industriesListIterator.next();
			if(anIndustry.getProduction()==0){
				industriesListIterator.remove();
			}
		}
	
//print to terminal information on industries
		if(Context.verbousFlag){
			System.out.println("STATS OFFICE: NEW DATA FROM INDUSTRIES ARE AVAILABLE");
			industriesListIterator=industriesList.iterator();
			while(industriesListIterator.hasNext()){
				anIndustry=industriesListIterator.next();
				System.out.println("absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction());
			}
			System.out.println("aggregate production "+aggregateProduction);
		}

//set relative rank for each firm
		
		int minimumAbsoluteRankOfFirmsWithPositiveProduction=industriesList.get(0).getAbsoluteRank();
		for(int i=0;i<industriesList.size();i++){
			anIndustry=industriesList.get(i);
			anIndustry.setRelativeRank(minimumAbsoluteRankOfFirmsWithPositiveProduction);
			anIndustry.setMarketShare(aggregateProduction);
		}
	
//compute totalWeightedProduction
		totalWeightedProduction=0;
		for(int i=0;i<industriesList.size();i++){
			anIndustry=industriesList.get(i);
			totalWeightedProduction=totalWeightedProduction+anIndustry.getWeightedProduction();
		}
//set product diffusion indicator for each industry
		for(int i=0;i<industriesList.size();i++){
			anIndustry=industriesList.get(i);
			anIndustry.setProductDiffusionIndicator(totalWeightedProduction);
		}
//let firms computeAverageProductivityForEachDegreeOfEducation		
		statAction=statActionFactory.createActionForIterable(firmsList,"computeAverageProductivityForEachDegreeOfEducation",false);
		statAction.execute();
// computeAverageProductivityForEachDegreeOfEducation for the economy		
		numberOfWokersInADegree=new int[7];
		totalProductivityOfWorkersInADegree=new double[7];
		averageProductivityOfWorkersInADegree=new double[7];
		numberOfWorkers=0;
		totalProductivity=0;

		for(int i=0;i<firmsList.size();i++){
			aFirm=(Firm)firmsList.get(i);
			int[] aFirmNumberOfWokersInADegree=aFirm.getnumberOfWokersInADegree();
			double[] aFirmTotalProductivityOfWorkersInADegree=aFirm.getTotalProductivityOfWorkersInADegree();
			for(int j=0;j<7;j++){
				numberOfWokersInADegree[j]=numberOfWokersInADegree[j]+aFirmNumberOfWokersInADegree[j];
				totalProductivityOfWorkersInADegree[j]=totalProductivityOfWorkersInADegree[j]+aFirmTotalProductivityOfWorkersInADegree[j];
				numberOfWorkers=numberOfWorkers+aFirmNumberOfWokersInADegree[j];
				totalProductivity=totalProductivity+aFirmTotalProductivityOfWorkersInADegree[j];


				if(numberOfWokersInADegree[j]>0){
					averageProductivityOfWorkersInADegree[j]=totalProductivityOfWorkersInADegree[j]/numberOfWokersInADegree[j];
				}
				else{
					averageProductivityOfWorkersInADegree[j]=0;
				}
			}

		}
		averageProductivity=totalProductivity/numberOfWorkers;
			for(int z=0;z<7;z++){
				System.out.println("system level degree "+z+" workers "+numberOfWokersInADegree[z]+" total Productivity "+totalProductivityOfWorkersInADegree[z]+" average Prod "+averageProductivityOfWorkersInADegree[z]);
			}
			System.out.println("system number of workers "+numberOfWorkers+" total Productivity "+totalProductivity+" average productivity "+averageProductivity);


	}


	public void computeDemand(){
		aggregateDemand=0;
		try{
			consumersList=myContext.getObjects(Class.forName("sfcabm.Consumer"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}


		for(int i=0;i<consumersList.size();i++){
			aConsumer=(Consumer)consumersList.get(i);
			anOrderList=aConsumer.getOrders();

			for(int j=0;j<anOrderList.size();j++){
				anOrder=(AProductDemand)anOrderList.get(j);
				anIndustry=industriesList.get(j);
				anIndustry.increaseDemand(anOrder.getDemand());
				aggregateDemand=aggregateDemand+anOrder.getDemand();
			}

		}
		System.out.println("AGGREGATE DEMAND "+aggregateDemand);
		statAction=statActionFactory.createActionForIterable(industriesList,"allocateDemand",false);
		statAction.execute();
	}


	public void publishIndustriesStats(){
		try{
			consumersList=myContext.getObjects(Class.forName("sfcabm.Consumer"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}


//		statAction=statActionFactory.createActionForIterable(consumersList,"showInfoOnIndustries",false);
		statAction=statActionFactory.createActionForIterable(consumersList,"stepConsumption",false);
		statAction.execute();
	}
}
