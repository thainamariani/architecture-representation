package mestrado.arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.helpers.ModelElementHelper;
import mestrado.arquitetura.helpers.StereotypeHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.VariantType;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;


public abstract class ElementBuilder<T extends mestrado.arquitetura.representation.Element> {

	protected String name;
	protected Boolean isVariationPoint;
	protected VariantType variantType;
	protected List<String> concerns;
	protected final Architecture architecture;
	//private final HashMap<String, T> createdElements = new HashMap<String, T>();

	public ElementBuilder(Architecture architecture) {
		this.architecture = architecture;
	}
	
	protected abstract T buildElement(NamedElement modelElement);
	
	public T create(NamedElement modelElement) {
		initialize();
		inspectStereotypes(modelElement);
		name = modelElement.getName();
		T element = buildElement(modelElement);
		element.addConcerns(concerns); // TODO Ver isto
		//createdElements.put(modelElement.getXMIID(), element); // TODO VER ISTO
		return element;
	}
	
	private void inspectStereotypes(NamedElement modelElement) {
		List<Stereotype> allStereotypes = ModelElementHelper.getAllStereotypes(modelElement);
		for (Stereotype stereotype : allStereotypes) {
			verifyVariationPoint(stereotype);
			verifyVariant(stereotype);	
			//verifyFeature(stereotype); // TODO Verificar
			verifyConcern(stereotype);
		}
	}
	
	private void verifyConcern(Stereotype stereotype) {
		if (StereotypeHelper.hasConcern(stereotype))
			 concerns.add(stereotype.getName());
	}
	
	private void verifyVariant(Stereotype stereotype) {
		VariantType type = VariantType.getByName(stereotype.getName());
		if (type != VariantType.NONE)
			variantType = type;
	}
	private void verifyVariationPoint(Stereotype stereotype) {
		isVariationPoint = StereotypeHelper.isVariationPoint(stereotype);
	}
	

	private void initialize() {
		name = "";
		isVariationPoint = false;
		variantType = VariantType.NONE;
		concerns = new ArrayList<String>();
	}
	
}