package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;

public class AggregationOperations implements Relationship {

	private DocumentManager doc;
	private String client;
	private String target;
	private String name;
	private String multiplicityClassTarget;
	private String multiplicityClassClient;

	public AggregationOperations(DocumentManager doc, String name,  String multiplicityClassClient, String multiplicityClassTarget) {
		this.doc = doc;
		this.name = name;
		this.multiplicityClassClient = multiplicityClassClient;
		this.multiplicityClassTarget = multiplicityClassTarget;
	}

	public AggregationOperations(DocumentManager doc) {
		this.doc = doc;
	}

	public Relationship between(String idElement) {
		this.client = idElement;
		return this;
	}
	
	public Relationship withMultiplicy(String multiplicity) {
		if(this.target != null)
			this.multiplicityClassTarget = multiplicity;
		else if(this.client != null)
			this.multiplicityClassClient = multiplicity;
		return this;
	}

	public Relationship and(String idElement) {
		this.target = idElement;
		return this;
	}

	public String build() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
		final CompositionNode compositeNode = new CompositionNode(doc);
		
		mestrado.arquitetura.parser.Document.executeTransformation(doc, new Transformation(){
			public void useTransformation() throws NodeNotFound, InvalidMultiplictyForAssociationException {
				compositeNode.createComposition(client, target, multiplicityClassClient, multiplicityClassTarget, "shared");
			}
		});
		
		return ""; //TODO return id;
	}

	public Relationship createRelation(String name) {
		if(("".equals(name) || name == null)) name = "shared";
		return new AggregationOperations(this.doc, name, multiplicityClassClient, multiplicityClassTarget);
	}


}