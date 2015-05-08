package org.wikipathways.geneparcer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bridgedb.BridgeDb;
import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.Xref;
import org.bridgedb.bio.Organism;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.model.PathwayElement;
import org.pathvisio.wikipathways.webservice.WSCurationTag;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;
import org.wikipathways.client.WikiPathwaysClient;

/**
 * 
 * @author Bram
 *
 */
public class PathwayGeneParcer {

	public static void main(String[] args) throws MalformedURLException, RemoteException, ConverterException, FileNotFoundException, IOException, IDMapperException, ClassNotFoundException {
		WikiPathwaysClient client=new WikiPathwaysClient(new URL("http://webservice.wikipathways.org"));
		/* 
	 WSPathwayInfo [] info = client.listPathways(Organism.HomoSapiens);
	 for(WSPathwayInfo i : info) {
		 //for testing 

		 */

		//WSPathway p = client.getPathway(i.getId());

		WSPathway p = client.getPathway("WP58");
		WSCurationTag[] c= client.getCurationTags(p.getId());

		List<String> tags= new ArrayList<String>();

		for(WSCurationTag tag : c){
			tags.add(tag.getDisplayName());
		}

		if(tags.contains("Curated collection")){
			Pathway pathway = WikiPathwaysClient.toPathway(p);
			System.out.println(pathway.getMappInfo().getMapInfoName());

			PrintWriter writer = new PrintWriter("output.txt","UTF-8");	 	
			List<PathwayElement> list= pathway.getDataObjects();


			//setting up bridgedb

			File bridgedb = new File("Hs_Derby_20130701.bridge");
			Class.forName("org.bridgedb.rdb.IDMapperRdb");  
			IDMapper mapper = BridgeDb.connect("idmapper-pgdb:" + bridgedb.getAbsolutePath());


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

						// Mapping to En Id and writing in text file 
						Set<Xref> result = mapper.mapID(element.getXref(), DataSource.getExistingBySystemCode("En"));
						for(Xref xref : result ){
							writer.println(name+"\t"+ id+"\t"+ xref.getId() +"\t"+ nodeName);
						}
					}
				}
			} 	
			writer.close();	
		}	
		//	}
	}
}




