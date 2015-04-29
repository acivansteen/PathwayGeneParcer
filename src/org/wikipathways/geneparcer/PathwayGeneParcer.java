package org.wikipathways.geneparcer;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import org.bridgedb.DataSource;
import org.bridgedb.Xref;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.wikipathways.client.WikiPathwaysClient;

/**
 * 
 * @author Bram
 *
 */
public class PathwayGeneParcer {

	public static void main(String[] args) throws MalformedURLException, RemoteException, ConverterException {
		WikiPathwaysClient client=new WikiPathwaysClient(new URL("http://webservice.wikipathways.org"));
	 
//	 WSPathwayInfo [] info = client.listPathways(Organism.HomoSapiens);
//	 
//	 for(WSPathwayInfo i : info) {
//		 WSPathway p = client.getPathway(i.getId());
		
		// TODO: 
		// parse pathway
	 	WSPathway p = client.getPathway("WP1");
	 	//makes p the pathway WP1
	 	Pathway pathway = WikiPathwaysClient.toPathway(p);
	 	System.out.println(pathway.getMappInfo().getMapInfoName());
	 	List<Xref> list= pathway.getDataNodeXrefs();
	 	
	 	for(int i=0; i<list.size();i++){
	 		System.out.println(list.get(i).getId());
	 		System.out.println(list.get(i).getDataSource());
	 	}
		
	 	
	 	
	 	/*System.out.println(list);
	 	System.out.println(list.size());
	 	System.out.println(list.get(2)); */
	 }
//	}

}
