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

public class Context implements ContextBuilder<Object> {
	public static boolean verboseFlag=true;
		public static int NumConsumers = 20;
		public static int NumFirms = 3;
		public static int consumerExitAge=50;
		public static int parameterOfProductivityInProductionFuncion=100;
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
		public static int consumersProgressiveIdentificationNumber=0; 
		public static int firmsProgressiveIdentificationNumber=0; 

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

		IndexedIterable consumersList,firmsList;
		OfficeForStatistics officeForStatistics;

	DefaultActionFactory contextActionFactory= new DefaultActionFactory();
	IAction contextAction;
	
		//public static boolean verbouseFlag=true;
		public repast.simphony.context.Context<Object> build(
				repast.simphony.context.Context<Object> context) {

			//int maxIter=30;

			for (int i = 0; i< NumConsumers; i++){
				aConsumer=new Consumer(consumersProgressiveIdentificationNumber,context);
				consumersProgressiveIdentificationNumber++;
				aConsumer.initialize();
				context.add(aConsumer);
			}


			for (int f = 0; f<NumFirms; f++){
				context.add(new Firm(firmsProgressiveIdentificationNumber,context));
				firmsProgressiveIdentificationNumber++;
			}

			LaborMarket theLaborMarket=new LaborMarket();
			context.add(theLaborMarket);

			try{
				consumersList=context.getObjects(Class.forName("sfcabm.Consumer"));
				firmsList=context.getObjects(Class.forName("sfcabm.Firm"));
			}
			catch(ClassNotFoundException e){
				System.out.println("Class not found");
			}
			for(int i=0;i<consumersList.size();i++){
				aConsumer=(Consumer)consumersList.get(i);
				aConsumer.sendInitialJobApplication();
			}
			for(int i=0;i<firmsList.size();i++){
				aFirm=(Firm)firmsList.get(i);
				aFirm.setInitialWorkers();
			}

			officeForStatistics=new OfficeForStatistics(context);
			officeForStatistics.computeVariables();

// trial cycle to be replaced with scheduled actions
	
			contextAction=contextActionFactory.createActionForIterable(firmsList,"setWorkersWage",false);
			contextAction.execute();

			contextAction=contextActionFactory.createActionForIterable(consumersList,"stepConsumption",false);
			contextAction.execute();

			officeForStatistics.computeDemand();

			contextAction=contextActionFactory.createActionForIterable(firmsList,"jettisoningCurricula",false);
			contextAction.execute();

			theLaborMarket.jettisoningCurricula();

			contextAction=contextActionFactory.createActionForIterable(firmsList,"laborForceDownwardAdjustment",false);
			contextAction.execute();

			contextAction=contextActionFactory.createActionForIterable(consumersList,"sendJobApplications",false);
			contextAction.execute();
		if(verboseFlag){
System.out.println("DIRECT HIRING");
		}
			contextAction=contextActionFactory.createActionForIterable(firmsList,"laborForceUpwardAdjustment",false);
			contextAction.execute();

			officeForStatistics.activateLaborMarket();
	
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
