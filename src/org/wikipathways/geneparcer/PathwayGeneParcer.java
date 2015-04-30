package org.wikipathways.geneparcer;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.bridgedb.BridgeDb;
import org.bridgedb.DataSource;
import org.bridgedb.Xref;
import org.pathvisio.core.data.XrefWithSymbol;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.util.PathwayParser;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.wikipathways.client.WikiPathwaysClient;

/**
 * 
 * @author Bram
 *
 */
public class PathwayGeneParcer {

	public static void main(String[] args) throws MalformedURLException, RemoteException, ConverterException, FileNotFoundException, IOException {
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
	 	//System.out.println(pathway.);
	 	List<Xref> list= pathway.getDataNodeXrefs();
	 	
	 	PrintWriter writer = new PrintWriter("output.txt","UTF-8");
	 	for(int i=0; i<list.size();i++){
		 	String name=p.getName();
		 	String id=p.getId();
	 		String nodeId= list.get(i).getId();
	 		
	 		String nodeName = "";
	 		//cant find anything that works in getting the name, i think i need an xref with symbol 
	 		//and then display the symbol but i couldnt get it to work
	 		
	 		writer.println(name+"\t"+ id+"\t"+ nodeId+"\t"+ nodeName);
	 		
	 	}
	 	writer.close();
	 }
}
