package org.wikipathways.geneparcer;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.bridgedb.DataSource;
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
	 	Pathway pathway = WikiPathwaysClient.toPathway(p);
	 	System.out.println(pathway.getMappInfo().getMapInfoName());

	 }
//	}

}
