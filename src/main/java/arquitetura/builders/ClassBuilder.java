package arquitetura.builders;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;

import arquitetura.helpers.ModelHelper;
import arquitetura.helpers.ModelHelperFactory;
import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Method;

/**
 * Builder resposável por criar element do tipo Classe.
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class ClassBuilder extends ElementBuilder<arquitetura.representation.Class> {
	
	private AttributeBuilder attributeBuilder;
	private MethodBuilder methodBuilder;
	private ModelHelper modelHelper;

	/**
	 * Recebe como paramêtro {@link Architecture}. <br/>
	 * Initializa helper {@link ModelHelper}
	 * 
	 * @param architecture
	 */
	public ClassBuilder(Architecture architecture) {
		super(architecture);
		attributeBuilder = new AttributeBuilder(architecture);
		methodBuilder = new MethodBuilder(architecture);
		
		modelHelper = ModelHelperFactory.getModelHelper();
	}
	
	/**
	 * Constrói um elemento do tipo {@link Class}.
	 */
	@Override
	protected arquitetura.representation.Class buildElement(NamedElement modelElement) {
		arquitetura.representation.Class klass = null; // TODO VER ISTO. 
		
		boolean isAbstract = false;
		
		if(modelElement instanceof ClassImpl)
			isAbstract = ((org.eclipse.uml2.uml.Classifier)modelElement).isAbstract();
		
		String packageName = ((NamedElement)modelElement).getNamespace().getQualifiedName();
		packageName = packageName !=null ? packageName : "";
		
		
		
		klass = new Class(architecture, name, variantType, isAbstract, packageName, XmiHelper.getXmiId(modelElement));
		klass.getAllAttributes().addAll(getAttributes(modelElement));
		klass.getAllMethods().addAll(getMethods(modelElement, klass));
		return klass;
	}
	

	/**
	 * Retorna todos atributos de uma Class.
	 * @param modelElement
	 * @return List
	 */
	private List<Attribute> getAttributes(NamedElement modelElement) {
		List<Attribute> attrs = new ArrayList<Attribute>();
		
			List<Property> attributes = modelHelper.getAllAttributesForAClass(modelElement);
			for (Property property : attributes){
				attrs.add(attributeBuilder.create(property));
			}
		
		return attrs;
	}
	
	/**
	 * Retorna todos os método de uma classe
	 * 
	 * @param modelElement
	 * @return List
	 */
	private List<Method> getMethods(NamedElement modelElement, Class parent) {
		List<Method> methods = new ArrayList<Method>();
		List<Operation> elements = modelHelper.getAllMethods(modelElement);
		
		for (Operation classifier : elements)
			methods.add(methodBuilder.create(classifier));
			
		return methods;
	}

}