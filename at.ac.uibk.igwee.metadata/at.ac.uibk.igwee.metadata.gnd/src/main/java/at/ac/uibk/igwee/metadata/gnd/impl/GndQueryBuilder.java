package at.ac.uibk.igwee.metadata.gnd.impl;

import java.util.List;
import java.util.stream.Collectors;

public class GndQueryBuilder {
	
	public static String combineGndQueries(List<GndQueryParameter> params) {
		return params.stream()
				.map(param -> param.getIndex().toString() + "=" + param.getQueryString())
				.collect(Collectors.joining(" and "));
	}

}
