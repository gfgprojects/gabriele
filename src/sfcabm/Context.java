package sfcabm;

import repast.simphony.dataLoader.ContextBuilder;
//import sfcabm.LaborMkt;
import sfcabm.Workers;
import sfcabm.Firm;

public class Context implements ContextBuilder<Object> {
		private static final int NumWorkers = 10;
		private static final int NumFirm = 10;

		//public static boolean verbouseFlag=true;
		public repast.simphony.context.Context<Object> build(
				repast.simphony.context.Context<Object> context) {
	
			for (int i = 0; i< NumWorkers; i++){
				context.add(new Workers(i));
				
				//non sicura che vada bene, ma il eclipse consiglia cosi
			for (int m = 0; m<NumFirm; m++){
					context.add(new Firm(m, m, m, m));
				}
				
			//LaborMkt theLaborMkt=new LaborMkt();
			//context.add(theLaborMkt);
			
			
			}
			return context;
			
			
		}


	}
