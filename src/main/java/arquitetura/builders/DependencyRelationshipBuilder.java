package arquitetura.builders;


import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;

import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.relationship.DependencyRelationship;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class DependencyRelationshipBuilder  extends ArchitectureHelper{

	private Architecture architecture;

	public DependencyRelationshipBuilder( Architecture architecture) {
		this.architecture = architecture;
	}
	public DependencyRelationship create(Dependency element) {

		EList<NamedElement> suppliers = element.getSuppliers();
		EList<NamedElement> clieents = element.getClients();
		
//		EList<org.eclipse.uml2.uml.Element> relatedElements = element.getRelatedElements();
//		
//		for (org.eclipse.uml2.uml.Element elm : relatedElements) {
//			arquitetura.representation.Element e = architecture.getElementByXMIID(getModelHelper().getXmiId(elm));
//			e.getIdsRelationships().add((getModelHelper().getXmiId(element)));
//		}
		
		Element client = architecture.findElementById(getModelHelper().getXmiId(clieents.get(0)));
		Element supplier = architecture.findElementById(getModelHelper().getXmiId(suppliers.get(0)));
		
		if((client instanceof Class) && (supplier instanceof Interface))
			((Class) client).getRequiredInterfaces().add((Interface) supplier);
		
		DependencyRelationship dependency =  new DependencyRelationship(supplier, client, element.getName(), architecture, getModelHelper().getXmiId(element));
		
		dependency.getClient().addRelationship(dependency);
		dependency.getSupplier().addRelationship(dependency);
		
		return dependency;
	}

}

