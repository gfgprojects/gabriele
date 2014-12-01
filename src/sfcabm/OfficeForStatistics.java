package sfcabm;

import java.util.ArrayList;
import java.util.Iterator;
import repast.simphony.data2.AggregateDSCreator;
import repast.simphony.data2.AggregateDataSource;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.engine.schedule.DefaultActionFactory;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.util.ContextUtils;
import repast.simphony.random.RandomHelper;

import sfcabm.Firm;

public class OfficeForStatistics{
	public repast.simphony.context.Context<Object> myContext;
	LaborMarket myLaborMarket;
	AggregateDataSource maximumAbsoluteRankDataSource,minimumAbsoluteRankDataSource;
	public static ArrayList<Industry> industriesList = new ArrayList<Industry>();

	double maximumAbsoluteRank,minimumAbsoluteRank,aggregateProduction,totalWeightedProduction,aggregateDemand;
	public IndexedIterable<Object> firmsList,consumersList;

	Firm aFirm;
	Consumer aConsumer;
	Object anObj;
	Industry anIndustry;
	Iterator<Industry> industriesListIterator;
	Iterator contextIterator;

	DefaultActionFactory statActionFactory;
	IAction statAction;
	ArrayList anOrderList;
	AProductDemand anOrder;

	int[] numberOfWokersInADegree;
	double[] totalProductivityOfWorkersInADegree;
	public static double[] averageProductivityOfWorkersInADegree;
	int numberOfWorkers=0;
	double totalProductivity=0;
	public static double averageProductivity=0;

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
		industriesList = new ArrayList<Industry>();
		try{
			maximumAbsoluteRank=(double)maximumAbsoluteRankDataSource.get(myContext.getObjects(Class.forName("sfcabm.Firm")),0);
			minimumAbsoluteRank=(double)minimumAbsoluteRankDataSource.get(myContext.getObjects(Class.forName("sfcabm.Firm")),0);
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}
		if(Context.verboseFlag){
			System.out.println("     maximum absolute rank "+maximumAbsoluteRank+" minimum Ansolute Rank "+minimumAbsoluteRank);
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
//remove firms with 0 production

		contextIterator=myContext.iterator();
		while(contextIterator.hasNext()){
			anObj=contextIterator.next();
			if(anObj instanceof Firm){
				aFirm=(Firm)anObj;
				if(aFirm.getProduction()>0){
				}
				else{
					System.out.println("     firm removed because producing "+aFirm.getProduction());
					contextIterator.remove();
				}
			}
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
		if(Context.verboseFlag){
			//			System.out.println("STATS OFFICE: NEW DATA FROM INDUSTRIES ARE AVAILABLE");
			industriesListIterator=industriesList.iterator();
			while(industriesListIterator.hasNext()){
				anIndustry=industriesListIterator.next();
				if(Context.verboseFlag){
					System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction());
				}
			}

			if(Context.verboseFlag){
				System.out.println("     aggregate production "+aggregateProduction);
			}
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
		if(Context.verboseFlag){
			for(int z=0;z<7;z++){
				System.out.println("     system level degree "+z+" workers "+numberOfWokersInADegree[z]+" total Productivity "+totalProductivityOfWorkersInADegree[z]+" average Prod "+averageProductivityOfWorkersInADegree[z]);
			}
			System.out.println("     system number of workers "+numberOfWorkers+" total Productivity "+totalProductivity+" average productivity "+averageProductivity);
		}

	}

/**
 * The difference with the computeDemand method is that this method accounts for imperfection in the goods market 
 */
	public void computeDesiredDemand(){
		aggregateDemand=0;
		statAction=statActionFactory.createActionForIterable(industriesList,"resetDemand",false);
		statAction.execute();


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
				double tmpDemand=anOrder.getDemand()*(1-Context.percentageOfDemandMissedBecauseOfGoodsMarketsInperfections);
				anIndustry.increaseDemand(tmpDemand);
				aggregateDemand=aggregateDemand+tmpDemand;
			}

		}

		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: COMPUTE DESIRED DEMAND ");
		}
			industriesListIterator=industriesList.iterator();
			while(industriesListIterator.hasNext()){
				anIndustry=industriesListIterator.next();
		if(Context.verboseFlag){
				System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction()+" demand "+anIndustry.getDemand());
		}
			}

		if(Context.verboseFlag){
			System.out.println("     Aggregate demand "+aggregateDemand+" aggregate production "+aggregateProduction);
		}
	}

	public void allocateDesiredDemand(){
		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: ALLOCATE DESIRED DEMAND ");
		}
		statAction=statActionFactory.createActionForIterable(industriesList,"allocateDesiredDemand",false);
		statAction.execute();
	}

	public void computeDemand(){
		aggregateDemand=0;
		statAction=statActionFactory.createActionForIterable(industriesList,"resetDemand",false);
		statAction.execute();


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

		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: COMPUTE DEMAND ");
		}
			industriesListIterator=industriesList.iterator();
			while(industriesListIterator.hasNext()){
				anIndustry=industriesListIterator.next();
		if(Context.verboseFlag){
				System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction()+" demand "+anIndustry.getDemand());
		}
			}

		if(Context.verboseFlag){
			System.out.println("     Aggregate demand "+aggregateDemand+" aggregate production "+aggregateProduction);
		}
	}

	public void allocateDemand(){
		if(Context.verboseFlag){
			System.out.println("OFFICE FOR STATISTICS: ALLOCATE DEMAND ");
		}
		statAction=statActionFactory.createActionForIterable(industriesList,"allocateDemand",false);
		statAction.execute();
	}

/**
 * This method allocates the excess of demand of an industry to the industry that produces the next less advanced product. The cycle starts from the industry that produces the most advanced product.  
 */
	public void matchDemandAndSupply(){
		double multiplier;
		double excessDemandToAllocate=0;
		for(int i=industriesList.size()-1;i>=0;i--){
			anIndustry=(Industry)industriesList.get(i);
			if((excessDemandToAllocate+anIndustry.getDemand())>anIndustry.getProduction()){
				multiplier=(anIndustry.getProduction()/anIndustry.getDemand());
				if(Context.verboseFlag){
					System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction()+" demand "+anIndustry.getDemand()+" multiplier "+multiplier);
				}
				excessDemandToAllocate+= anIndustry.getDemand()-anIndustry.getProduction();
			}
			else{
				multiplier=((anIndustry.getDemand()+excessDemandToAllocate)/anIndustry.getDemand())*(1-Context.percentageOfDemandMissedBecauseOfGoodsMarketsInperfections);
				if(Context.verboseFlag){
					System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction()+" demand "+anIndustry.getDemand()+" multiplier "+multiplier);
				}
				excessDemandToAllocate=0;
			}
			for(int j=0;j<consumersList.size();j++){
				aConsumer=(Consumer)consumersList.get(j);
				aConsumer.adjustConsumtionToMatchDemandAndSupply(i,multiplier);
			}
		}






/*

		if(aggregateDemand>aggregateProduction){
			for(int i=0;i<industriesList.size();i++){
				anIndustry=(Industry)industriesList.get(i);
				multiplier=anIndustry.getProduction()/anIndustry.getDemand();
				System.out.println("     absolute Rank "+anIndustry.getAbsoluteRank()+" number of firms "+anIndustry.getNumberOfFirms()+" production "+anIndustry.getProduction()+" demand "+anIndustry.getDemand()+" multiplier "+multiplier);
				for(int j=0;j<consumersList.size();j++){
					aConsumer=(Consumer)consumersList.get(j);
					aConsumer.adjustConsumtionToMatchDemandAndSupply(i,multiplier);
				}

			}
		}
		else{
			System.out.println("domanda insuff");
		}
*/
	}



	public void activateLaborMarket(){
		if(Context.verboseFlag){
			System.out.println("LABOR AGENCY");
		}
				try{
					myLaborMarket=(LaborMarket)(myContext.getObjects(Class.forName("sfcabm.LaborMarket"))).get(0);
				}
				catch(ClassNotFoundException e){
					System.out.println("Class not found");
				}
			switch(Context.firmsWorkersMatching){
				case 0: if(Context.verboseFlag){
						System.out.println("     matching activity was not asked");
					}
					break;
				case 1:
					myLaborMarket.match();
					break;
				case 2:
					myLaborMarket.match();
					break;
				default: System.out.println("Unknown workers firms matching mechanism");
					 break;
			}
	}

	public void performConsumersTurnover(){
		if(Context.verboseFlag){
			System.out.println("CONSUMERS TURNOVER");
		}

// remove
		ArrayList<Consumer> newConsumersList = new ArrayList<Consumer>();
		Consumer aNewConsumer;
		contextIterator=myContext.iterator();
		while(contextIterator.hasNext()){
			anObj=contextIterator.next();
			//check consumers
			if(anObj instanceof Consumer){
				aConsumer=(Consumer)anObj;
				if(aConsumer.getAge()>Context.consumerExitAge){
					if(Context.verboseFlag){
						System.out.println("     Exit  of consumer "+aConsumer.getIdentity()+" age "+aConsumer.getAge()+" wealth "+aConsumer.getWealth());
					}
					contextIterator.remove();
					aNewConsumer=new Consumer(Context.consumersProgressiveIdentificationNumber,myContext,aConsumer.getWealth());
					Context.consumersProgressiveIdentificationNumber++;
					newConsumersList.add(aNewConsumer);
				}
			}
		}

// add new consumers to context
		for(int i=0;i<newConsumersList.size();i++){
			aConsumer=newConsumersList.get(i);
			myContext.add(aConsumer);
			if(Context.verboseFlag){
				System.out.println("     Entry of consumer "+aConsumer.getIdentity()+" age "+aConsumer.getAge()+" wealth "+aConsumer.getWealth());
			}
		}
	}

	public void performFirmsTurnover(){
		if(Context.verboseFlag){
			System.out.println("FIRMS TURNOVER");
		}

// remove
		ArrayList<Firm> newFirmsList = new ArrayList<Firm>();
		Firm aNewFirm;
		contextIterator=myContext.iterator();
		while(contextIterator.hasNext()){
			anObj=contextIterator.next();
		//check firms
			if(anObj instanceof Firm){
				aFirm=(Firm)anObj;
				if(aFirm.getProduction()<20){
					if(Context.verboseFlag){
						System.out.println("     Exit  of Firm "+aFirm.getID()+" production "+aFirm.getProduction());
					}
					contextIterator.remove();
					aNewFirm=new Firm(Context.firmsProgressiveIdentificationNumber,myContext);
					Context.firmsProgressiveIdentificationNumber++;
					newFirmsList.add(aNewFirm);
				}

			}

		}

// add new Firms to context
		for(int i=0;i<newFirmsList.size();i++){
			aFirm=newFirmsList.get(i);
			aFirm.setProductAbsoluteRank(RandomHelper.nextIntFromTo((int)minimumAbsoluteRank,(int)maximumAbsoluteRank));
			myContext.add(aFirm);
			if(Context.verboseFlag){
				System.out.println("     Entry of Firm "+aFirm.getID()+" absolute Rank "+aFirm.getProductAbsoluteRank());
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


//		statAction=statActionFactory.createActionForIterable(consumersList,"showInfoOnIndustries",false);
		statAction=statActionFactory.createActionForIterable(consumersList,"stepConsumption",false);
		statAction.execute();
	}
}
