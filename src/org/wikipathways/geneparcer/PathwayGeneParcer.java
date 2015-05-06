package org.wikipathways.geneparcer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.model.PathwayElement;
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
	 	
	 	//List<Xref> list= pathway.getDataNodeXrefs();
	 	List<PathwayElement> list= pathway.getDataObjects();
	 	System.out.println(list);
	 	
	 	PrintWriter writer = new PrintWriter("output.txt","UTF-8");
	 	
	 	for(PathwayElement element : list) {
		 	String name=p.getName();
		 	String id=p.getId();
	 		String nodeId= element.getElementID();
	 		String nodeName= element.getTextLabel();
	 		
	 		// make sure nodeId and nodeName are not empty
	 		if(nodeId != null && !nodeId.equals("") && 
	 				nodeName != null && !nodeName.equals("")) {
	 		
		 		// since we only want to provide genes and proteins 
		 		// we need to filter the nodes based on data node type
		 		if(element.getDataNodeType().equals("GeneProduct") ||
		 				element.getDataNodeType().equals("Protein")) {
		 			
		 			// TODO: identifier mapping (map everything to Ensembl)
		 			// system code of Ensembl = En
		 			
		 			writer.println(name+"\t"+ id+"\t"+ nodeId+"\t"+ nodeName);
		 		}
	 		}
	 	} 	
	 	writer.close();
	}
}
