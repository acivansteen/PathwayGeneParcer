package org.wikipathways.geneparcer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	public static void main(String[] args) throws MalformedURLException,
			RemoteException, ConverterException, FileNotFoundException,
			IOException, IDMapperException, ClassNotFoundException {
		WikiPathwaysClient client = new WikiPathwaysClient(new URL(
				"http://webservice.wikipathways.org"));

		PrintWriter writerE = new PrintWriter("edges.txt", "UTF-8");
		PrintWriter writerN = new PrintWriter("nodes.txt","UTF-8");
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> pathwayset = new HashMap<String,String>();
		
		

		System.out.println("[INFO]\t Get all pathways.");
		WSPathwayInfo[] info = client.listPathways(Organism.HomoSapiens);
		System.out.println("[INFO]\t Number of pathways: " + info.length);

		System.out.println("[INFO]\t Filter curated collection.");
		// check if pathway is in curated collection
		List<WSPathwayInfo> pwyList = new ArrayList<WSPathwayInfo>();
		for (WSPathwayInfo i : info) {
			for (WSCurationTag tag : client.getCurationTags(i.getId())) {
				if (tag.getName().equals("Curation:AnalysisCollection")) {
					pwyList.add(i);
				}
			}
		}
		System.out.println("[INFO]\t Number of curated pathways: "
				+ pwyList.size());

		System.out.println("[INFO]\t Get all datanodes.");
		for (WSPathwayInfo i : pwyList) {
			WSPathway p = client.getPathway(i.getId());

			// get general information
			String name = p.getName();
			String id = p.getId();
			if (!pathwayset.containsKey(id)){
				pathwayset.put(id, name);
			}
			Pathway pathway = WikiPathwaysClient.toPathway(p);

			// setting up bridgedb
			File bridgedb = new File("Hs_Derby_20130701.bridge");
			Class.forName("org.bridgedb.rdb.IDMapperRdb");
			IDMapper mapper = BridgeDb.connect("idmapper-pgdb:"
					+ bridgedb.getAbsolutePath());

			for (PathwayElement element : pathway.getDataObjects()) {
				String nodeId = element.getElementID();
				String nodeName = element.getTextLabel();

				// make sure nodeId and nodeName are not empty
				if (nodeId != null && !nodeId.equals("") && nodeName != null
						&& !nodeName.equals("")) {

					// since we only want to provide genes and proteins
					// we need to filter the nodes based on data node type
					if (element.getDataNodeType().equals("GeneProduct")
							|| element.getDataNodeType().equals("Protein")) {

						// Mapping to Ensembl Id and writing in text file
						Set<Xref> result = mapper.mapID(element.getXref(),
								DataSource.getExistingBySystemCode("En"));
						for (Xref xref : result) {					
							if (!map.containsKey(xref.getId()))
								map.put(xref.getId(), nodeName);
							for(String key:map.keySet()){
								writerE.println( id+"\t"+ key);
							}
						}
					}
				}
			}
			// writing all genes by ensemble Id into the nodes.txt file
			for (String key : map.keySet()) {
				writerN.println(key	+ "\t" + map.get(key));
			}
			for (String key: pathwayset.keySet()){
				writerN.println(key	+ "\t" + pathwayset.get(key));
			}
		}
			
		 System.out.println("[INFO]\t All pathways done.");
		 writerN.close();
		 writerE.close();	
		
				 
	}
}
