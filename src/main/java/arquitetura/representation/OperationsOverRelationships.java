package arquitetura.representation;

import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.MemberEnd;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;

public class OperationsOverRelationships {
	
	static Logger LOGGER = LogManager.getLogger(OperationsOverRelationships.class.getName());
	
	private List<Relationship> relationships;
	private Set<String> allIds;
	private Architecture  architecture;
	
	public OperationsOverRelationships(Architecture architecture) {
		this.architecture = architecture;
		this.relationships = architecture.getAllRelationships();
		this.allIds = architecture.getAllIds();
	}

	public void moveAssociation(AssociationRelationship association, Class class1, Class class2) {
		class1.getRelationships().remove(association);
		class2.getRelationships().remove(association);
		
		association.getParticipants().get(0).setCLSClass(class1);
		association.getParticipants().get(1).setCLSClass(class2);
		
		class1.getRelationships().add(association);
		class2.getRelationships().add(association);
	}
	
	public void moveAssociationClass(AssociationClassRelationship association, Class member1, Class member2) {
		association.getMemebersEnd().clear();
		association.getMemebersEnd().add(new MemberEnd("none", null, "public", member1));
		association.getMemebersEnd().add(new MemberEnd("none", null, "public", member2));
		
		member1.getRelationships().add(association);
		member2.getRelationships().add(association);
	}
	
	public void moveDependency(DependencyRelationship dependency, Class client, Class supplier) {
		dependency.setClient(client);
		dependency.setSupplier(supplier);
		client.getRelationships().add(dependency);
		supplier.getRelationships().add(dependency);
	}


	public void removeAssociationRelationship(AssociationRelationship as) {
		if (!removeRelationship(as))
			LOGGER.info("Cannot remove Association " + as + ".\n");
	}
	
	public void removeDependencyRelationship(DependencyRelationship dp) {
		if (!removeRelationship(dp))
			LOGGER.info("Cannot remove Dependency " + dp + ".\n");
	}

	public void removeAssociationClass(AssociationClassRelationship associationClass){
		if (!removeRelationship(associationClass))
			LOGGER.info("Cannot remove AssociationClass " + associationClass + ".\n");
	}

	public void removeGeneralizationRelationship(GeneralizationRelationship generalization) {
		if (!removeRelationship(generalization))
			LOGGER.info("Cannot remove Generalization " + generalization + ".\n");
	}

	private boolean removeRelationship(Relationship as) {
		if(as == null) return false;
		this.allIds.remove(as.getId());
		return relationships.remove(as);
	}

	public void moveAssociationEnd(AssociationEnd associationEnd, Class idclass8) {
		associationEnd.setCLSClass(idclass8);
	}

	public void moveDependencyClient(DependencyRelationship dependency,	Class newClient) {
		dependency.setClient(newClient);
	}

	public void moveDependencySupplier(DependencyRelationship dependency, Class newSupplier) {
		dependency.setSupplier(newSupplier);
	}

	public void moveMemberEndOf(MemberEnd memberEnd, Class klass) {
		memberEnd.setType(klass);
	}

	/**
	 * Move o client de uma {@link RealizationRelationship} para outro elemento (Classe ou Package).
	 *
	 * @param realization
	 * @param newClient
	 */
	public void moveRealizationClient(RealizationRelationship realization, Element newClient) {
		realization.getSupplier().getRelationships().remove(realization);
		realization.setClient(newClient);
		newClient.getRelationships().add(realization);
	}

	/**
	 * Move o supplier de uma {@link RealizationRelationship} para outro elemento (Classe ou Package).
	 * 
	 * @param realization
	 * @param newSupplier
	 */
	public void moveRealizationSupplier(RealizationRelationship realization, Element newSupplier) {
		realization.getSupplier().getRelationships().remove(realization);
		realization.setSupplier(newSupplier);
		newSupplier.getRelationships().add(realization);
	}
	
	/**
	 * Move uma realizacão inteira.
	 * 
	 * @param realization - Realização a ser movida
	 * @param client - Novo Cliente
	 * @param supplier - Novo Supplier
	 */
	public void moveRealization(RealizationRelationship realization, Element client, Element supplier) {
		
		realization.getClient().getRelationships().remove(realization);
		realization.getSupplier().getRelationships().remove(realization);
		
		realization.setClient(client);
		realization.setSupplier(supplier);
		
		client.getRelationships().add(realization);
		supplier.getRelationships().add(realization);
		
	}

	public void createNewRealization(Element client, Element supplier) {
		String id = UtilResources.getRandonUUID();
		RealizationRelationship realization = new RealizationRelationship(client, supplier, "", id);
		client.getRelationships().add(realization);
		supplier.getRelationships().add(realization);
		this.architecture.getAllRelationships().add(realization);
	}

}
