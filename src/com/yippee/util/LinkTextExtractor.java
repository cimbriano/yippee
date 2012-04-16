package com.yippee.util;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LinkTextExtractor {
	ArrayList<String> links;
	ArrayList<String> text;
	
	public LinkTextExtractor() {
		links = new ArrayList<String>();
		text = new ArrayList<String>();
	}
	
	/**
	 * Recursively extracts all <a> tags in an HTML document and returns them as a list to be added to the queue to be crawled
	 * 
	 * @param url URL of the document, to evaluate relative links
	 * @param parent the parent node of this node
	 * @return All links found in this branch
	 */
	protected void extract (String url, Node parent) {
		
		NodeList nodes = parent.getChildNodes();
//		ArrayList<String> links = new ArrayList<String>(); 
		
		for (int z = 0; z < nodes.getLength(); z++) {
			Node child = nodes.item(z);
			
			// Links
			if (child.getNodeName().equals("a")) {
				NamedNodeMap attr = child.getAttributes();
				
				if (attr.getNamedItem("href") != null) {
					// Found a link
					String link =  attr.getNamedItem("href").getNodeValue();
					
					
					try{
						// Resolve url if relative
						if (!link.startsWith("http://") && !link.startsWith("https://")) {
							link = resolve(url, link);
						}
						
//						System.out.println("queuing link: " + link);
						links.add(link);
					}catch(MalformedURLException e){
						//Do nothing with this link, absolute url could not be formed
						//TODO Add logging here
					}
					

				}				
			} else if (child.getNodeType() == Node.TEXT_NODE) {
				text.add(child.getNodeValue());
			}
			
			// Recursively find more links
			if (child.hasChildNodes()) {
				extract(url, child);
			}
		}
	}
	

	public ArrayList<String> getLinks() {
		return links;
	}
	
	public ArrayList<String> getText() {
		return text;
	}
	
	public String resolve(String baseURL, String relativeURL) throws MalformedURLException {
		//Does baseURL end in slash?
		if(baseURL.endsWith("/")){
			if(relativeURL.startsWith("/")){
				//Case 1A
				//Use URL class to get the root of baseURL (i.e., up to the end of the host)
				return relativeStartsWithSlash(baseURL, relativeURL);
				
			} else if(relativeURL.startsWith("?") || relativeURL.startsWith("#")){
				return baseURL + relativeURL;
			} else{
				//RelativeURL doesn't start with slash
				//Case 1 B, C & D
				return baseURL + relativeURL;
			}
		}else {
			//baseURL Doesn't End With Slash
			if(relativeURL.startsWith("/")){
				//Case 2A (Same as case 1A
				return relativeStartsWithSlash(baseURL, relativeURL);
				
			} else if(relativeURL.startsWith("?") || relativeURL.startsWith("#")){
				return baseURL + relativeURL;
			}else{
				//Cases 2 B, C & D
				//base Does NOT end in slash
				URL url = new URL(baseURL);
				//System.out.println("Stripped baseURL: " + baseURL.substring(url.getProtocol().length() + 3));
				
				if( baseURL.substring(url.getProtocol().length() + 3).contains("/") ){
					
					//Has slash other than protocol
					//Append relative URL to last slash in baseURL
					return baseURL.substring(0, baseURL.lastIndexOf('/') + 1) + relativeURL;
					
				}else {
					//No other / besides protocol
					return baseURL + "/" + relativeURL;
				}
				
			}
		}
		
	}

	private String relativeStartsWithSlash(String baseURL, String relativeURL)
			throws MalformedURLException {
		URL url = new URL(baseURL);
		int port = url.getPort();
		return url.getProtocol() + "://" + url.getHost() + ((port == 80 || port == -1) ? "" : ":" + url.getPort()) + relativeURL;
	}
	
}
