package mestrado.arquitetura.representation.relationship;

import mestrado.arquitetura.representation.Element;
import mestrado.arquitetura.representation.Package;

/**
 * 
 * @author edipofederle
 *
 */
public class AbstractionRelationship extends Relationship{
	
	private Element client;
	private Element supplier;

	public AbstractionRelationship(Element client, Element supplier) {
		setClient(client);
		setSupplier(supplier);
		if(client instanceof Package)
			((Package) client).addImplementedInterface(client);
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
	public void setClient(Element client) {
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
	public void setSupplier(Element supplier) {
		this.supplier = supplier;
	}



}