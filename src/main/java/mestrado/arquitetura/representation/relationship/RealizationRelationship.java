package mestrado.arquitetura.representation.relationship;

import mestrado.arquitetura.representation.Element;

/**
 * 
 * @author edipofederle
 *
 */
public class RealizationRelationship extends Relationship {
	
	private String name;
	private Element client;
	private Element supplier;
	
	
	public RealizationRelationship(Element client, Element supplier, String name, String id){
		setClient(client);
		setSupplier(supplier);
		setName(name);
		setId(id);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	private void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the client
	 */
	public Element getClient() {
		return client;
	}


	/**
	 * @param client the client to set
	 */
	private void setClient(Element client) {
		this.client = client;
	}


	/**
	 * @return the supplier
	 */
	public Element getSupplier() {
		return supplier;
	}


	/**
	 * @param supplier the supplier to set
	 */
	private void setSupplier(Element supplier) {
		this.supplier = supplier;
	}


	
}