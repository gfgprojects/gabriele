package sfcabm;

import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.engine.schedule.DefaultActionFactory;
import repast.simphony.engine.schedule.IAction;
//import repast.simphony.engine.environment.RunEnvironment;
//import repast.simphony.random.RandomHelper;
//import sfcabm.LaborMkt;
import sfcabm.Consumer;
import sfcabm.Firm;
import sfcabm.LaborMarket;
import sfcabm.OfficeForStatistics;
import sfcabm.Bank;

public class Context implements ContextBuilder<Object> {
	public static boolean verboseFlag=false;
		public static int NumConsumers = 10;
		public static int NumFirms = 3;
		public static int NumBanks = 1;
		public static int consumerExitAge=50;
		public static int parameterOfProductivityInProductionFuncion=100;
		public static int parameterOfnumberOfWorkersToDetermineProductionCapitalInProductionFuncion=50;
		public static double percentageOfDemandMissedBecauseOfGoodsMarketsInperfections=0.0;
		public static double percentageOfCapitalDepreciation=0.05;
		public static double laborMarketStateToSetWage=0.5;
		public static double interestRateOnDeposits=0.01;
		public static double interestRateOnLoans=0.05;
		public static double interestRateOnSubsidizedLoans=0.01;
		public static int unemploymentDole=10;
		public static int costEdu=10;
		// set the wageSettingRule variable to
		//	0 if you want the wage depend on the worker productivity
		//	1 if you want the wage depend on the average productivity of workers having the worker degree in the employer's firm
		//	2 if you want the wage depend on the average productivity of workers having the worker degree in the economy
		public static int wageSettingRule=2;


		//set the firmsWorkersMatching variable to
		//	0 do the following: 	unemployed send applications to firms
		//				firms employ by looking at the received applications
		//	1 do the following:	unemployed send applications to firms and to the office for labor
		//				firms employ by looking at the received applications. If they need additional workers ask to the office for labor
		//	2 do the following:	unemployed send applications to the office for labor only
		//				firms post their vacancies to the office for labor only
		//				the office for labor match vacancies an unemployed
		
		public static int firmsWorkersMatching=0;
	
		public static int numberOfJobApplicationAnUneployedSends=2;
		public static int numberOfBanksAConsumerCanBeCustumerOf=1;
		public static int numberOfBanksAFirmCanBeCustumerOf=1;
		public static int consumersProgressiveIdentificationNumber=0; 
		public static int firmsProgressiveIdentificationNumber=0; 
		public static int banksProgressiveIdentificationNumber=0; 

		/*
		double initialProbabilityToBeEmployed=0.7;
		VARIABILI USATE IN LAB MKT
		double reservationWageWorker;
		double workerWage;
		int numApplic;
		EMPLOYMENT STATUS: DECIDERE SE TENERE O RIMUOVERE IN BASE A LAB MKT
		int status;
		 */
		
		
		 //propensioni rivedere calibration
		public static double alpha = 0.4;
		public static double beta = 0.4;
		//taxes on consumption, tax rate evolves??...T = \phi c
		public static double taxRate = 0.1;


		Consumer aConsumer;
		Firm aFirm;

		IndexedIterable consumersList,firmsList,banksList;
		OfficeForStatistics officeForStatistics;

	DefaultActionFactory contextActionFactory= new DefaultActionFactory();
	IAction contextAction;
	
		//public static boolean verbouseFlag=true;
		public repast.simphony.context.Context<Object> build(
				repast.simphony.context.Context<Object> context) {

			//int maxIter=30;

if(verboseFlag){
	System.out.println("");
	System.out.println("");
	System.out.println("BEGINNING OF INITIAL SETUP");
	System.out.println("==========================================");
	System.out.println("");
	System.out.println("");
}


if(verboseFlag){
System.out.println("CREATING CONSUMERS");
		}
			for (int i = 0; i< NumConsumers; i++){
				aConsumer=new Consumer(consumersProgressiveIdentificationNumber,context);
				consumersProgressiveIdentificationNumber++;
				aConsumer.initialize();
				context.add(aConsumer);
			}

		if(verboseFlag){
System.out.println("CREATING FIRMS");
		}

			for (int f = 0; f<NumFirms; f++){
				context.add(new Firm(firmsProgressiveIdentificationNumber,context));
				firmsProgressiveIdentificationNumber++;
			}

		if(verboseFlag){
System.out.println("CREATING BANKS");
		}

			for (int b = 0; b<NumBanks; b++){
				context.add(new Bank(banksProgressiveIdentificationNumber,context));
				banksProgressiveIdentificationNumber++;
			}



		if(verboseFlag){
System.out.println("CREATING OFFICE FOR LABOR");
		}
			LaborMarket theLaborMarket=new LaborMarket();
			context.add(theLaborMarket);

			try{
				consumersList=context.getObjects(Class.forName("sfcabm.Consumer"));
				firmsList=context.getObjects(Class.forName("sfcabm.Firm"));
				banksList=context.getObjects(Class.forName("sfcabm.Bank"));
			}
			catch(ClassNotFoundException e){
				System.out.println("Class not found");
			}



		if(verboseFlag){
System.out.println("CREATING OFFICE FOR STATISTICS");
		}
			officeForStatistics=new OfficeForStatistics(context);

	
		if(verboseFlag){
System.out.println("CONSUMERS: SEND CVs");
		}

			for(int i=0;i<consumersList.size();i++){
				aConsumer=(Consumer)consumersList.get(i);
				aConsumer.sendInitialJobApplication();
			}


		if(verboseFlag){
System.out.println("FIRMS: HIRE");
		}
			for(int i=0;i<firmsList.size();i++){
				aFirm=(Firm)firmsList.get(i);
				aFirm.setInitialWorkers();
			}

			if(verboseFlag){
System.out.println("CONSUMERS SETUP BANK ACCOUNTS");
		}
			contextAction=contextActionFactory.createActionForIterable(consumersList,"setupBankAccount",false);
			contextAction.execute();

			if(verboseFlag){
System.out.println("FIRMS SETUP BANK ACCOUNTS");
		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"setupBankAccount",false);
			contextAction.execute();

			if(verboseFlag){
System.out.println("BANK SETUP BALANCE");
		}
			contextAction=contextActionFactory.createActionForIterable(banksList,"computeDemandedCredit",false);
			contextAction.execute();
			contextAction=contextActionFactory.createActionForIterable(banksList,"computeDeposits",false);
			contextAction.execute();
			contextAction=contextActionFactory.createActionForIterable(banksList,"setupBalance",false);
			contextAction.execute();
			contextAction=contextActionFactory.createActionForIterable(banksList,"computeBalanceVariables",false);
			contextAction.execute();


			if(verboseFlag){
System.out.println("CHECK BALANCE SHEET CONSISTENCY");
}

			contextAction=contextActionFactory.createActionForIterable(consumersList,"computeWealth",false);
			contextAction.execute();
			contextAction=contextActionFactory.createActionForIterable(firmsList,"computeSumOfBankAccounts",false);
			contextAction.execute();

			
			
			
			
			
			
			// trial cycle to be replaced with scheduled actions




		if(verboseFlag){
System.out.println("END OF INITIAL SETUP");
System.out.println("==========================================");
System.out.println("START OF ITERATION CYCLE");
System.out.println("OFFICE FOR STATISTICS: COMPUTE VARIABLES (PRODUCT DIFFUSION INDICATOR, PRODUCTIVITIES ...");
		}

			officeForStatistics.computeVariables();

//		if(verboseFlag){
System.out.println("FIRMS: MAKE PRODUCTION");
//		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"makeProduction",false);
			contextAction.execute();


	
		if(verboseFlag){
System.out.println("FIRMS: SET WAGE");
		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"setWorkersWage",false);
			contextAction.execute();

//		if(verboseFlag){
System.out.println("BANKS: UPDATE CONSUMERS ACCOUNTS");
//		}

			contextAction=contextActionFactory.createActionForIterable(banksList,"updateConsumersAccounts",false);
			contextAction.execute();

		if(verboseFlag){
System.out.println("CONSUMERS: PAY BACK BANK DEBT");
		}
			contextAction=contextActionFactory.createActionForIterable(consumersList,"payBackBankDebt",false);
			contextAction.execute();



	
		if(verboseFlag){
System.out.println("STUDENTS: STEP STATE");
		}
			contextAction=contextActionFactory.createActionForIterable(consumersList,"stepStudentState",false);
			contextAction.execute();


		if(verboseFlag){
System.out.println("CONSUMERS: STEP CONSUMPTION");
		}
			contextAction=contextActionFactory.createActionForIterable(consumersList,"stepConsumption",false);
			contextAction.execute();

//		if(verboseFlag){
System.out.println("BANKS: EXTEND CONSUMER CREDIT");
//		}

			contextAction=contextActionFactory.createActionForIterable(banksList,"setAllowedConsumersCredit",false);
			contextAction.execute();

	
		if(verboseFlag){
System.out.println("CONSUMERS ADJUST CONSUMPTION ACCORDING TO EXTENDED CREDIT");
		}
			contextAction=contextActionFactory.createActionForIterable(consumersList,"adjustConsumptionAccordingToExtendedCredit",false);
			contextAction.execute();


			officeForStatistics.computeDemand();
			officeForStatistics.allocateDesiredDemand();


			officeForStatistics.matchDemandAndSupply();

			officeForStatistics.computeDemand();
			officeForStatistics.allocateDemand();

	
//			if(verboseFlag){
System.out.println("FIRMS COMPUTE ECONOMIC RESULT");
//		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"computeEconomicResultAndCapitalDepreciation",false);
			contextAction.execute();

//if(verboseFlag){
System.out.println("FIRMS COMPUTE DESIRED CAPITAL");
//		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"setDesiredCredit",false);
			contextAction.execute();

//		if(verboseFlag){
System.out.println("BANKS EXTEND FIRM CREDIT");
//		}

			contextAction=contextActionFactory.createActionForIterable(banksList,"setAllowedFirmsCredit",false);
			contextAction.execute();

//if(verboseFlag){
System.out.println("FIRMS ADJUST PRODUCTION CAPITAL");
//		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"adjustProductionCapitalAndBankAccount",false);
			contextAction.execute();






		if(verboseFlag){
System.out.println("JETTISONING CURRICULA");
		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"jettisoningCurricula",false);
			contextAction.execute();

			theLaborMarket.jettisoningCurricula();

		if(verboseFlag){
System.out.println("FIRMS PERFORM DOWNWARD ADJUSTMENT OF LABOR FORCE");
		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"laborForceDownwardAdjustment",false);
			contextAction.execute();

		if(verboseFlag){
System.out.println("CONSUMERS SEND CVs");
		}
			contextAction=contextActionFactory.createActionForIterable(consumersList,"sendJobApplications",false);
			contextAction.execute();
		if(verboseFlag){
System.out.println("DIRECT HIRING");
		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"laborForceUpwardAdjustment",false);
			contextAction.execute();

			officeForStatistics.activateLaborMarket();

/*
			if(verboseFlag){
System.out.println("SET DESIRED CAPITAL");
		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"setDesiredProductionCapital",false);
			contextAction.execute();
*/

			officeForStatistics.performConsumersTurnover();
			officeForStatistics.performFirmsTurnover();

			/* if (RunEnvironment.getInstance().isBatch())
			   {
			   RunEnvironment.getInstance().endAt(10);
			   }

*/
			return context;
			
			
		}

		

	}
