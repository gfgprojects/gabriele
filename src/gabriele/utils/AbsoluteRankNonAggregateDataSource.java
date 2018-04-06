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

package gabriele.utils;

import gabriele.agents.Firm;

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
