package arquitetura.representation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.exceptions.AttributeNotFoundException;
import arquitetura.exceptions.MethodNotFoundException;
import arquitetura.flyweights.VariantFlyweight;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.AssociationClassRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.touml.Types.Type;
import arquitetura.touml.VisibilityKind;

import com.rits.cloning.Cloner;

/**
 * 
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public class Class extends Element {
	
	private static final long serialVersionUID = -5450511036321846093L;

	static Logger LOGGER = LogManager.getLogger(Class.class.getName());
	
	private boolean isAbstract;
	private final Set<Attribute> attributes = new HashSet<Attribute>();
	private final Set<Method> methods = new HashSet<Method>();
	private Set<Interface> implementedInterfaces = new HashSet<Interface>();
	private Set<Interface> requiredInterfaces = new HashSet<Interface>();
	
	/**
	 * 
	 * @param architecture
	 * @param name
	 * @param isVariationPoint
	 * @param variantType
	 * @param isAbstract
	 * @param parent
	 * @param interfacee
	 * @param packageName
	 */
	public Class(Architecture architecture, String name, Variant variantType, boolean isAbstract, String namespace, String id) {
		super(architecture, name, variantType, "klass", namespace, id);
		setAbstract(isAbstract);
	}

	public Class(Architecture architecture, String name, boolean isAbstract) {
		this(architecture, name,  null, isAbstract,  UtilResources.createNamespace(architecture.getName(), name), UtilResources.getRandonUUID());
		architecture.addExternalClass(this);
	}
	
	public Class(Architecture architecture, String name, boolean isAbstract, String packageName) {
		this(architecture, name,  null, isAbstract,  UtilResources.createNamespace(architecture.getName()+"::"+packageName, name), UtilResources.getRandonUUID());
		architecture.addExternalClass(this);
	}

	public Attribute createAttribute(String name, Type type, VisibilityKind visibility) {
		String id = UtilResources.getRandonUUID();
		Attribute a = new Attribute(getArchitecture(), name, visibility.toString(), type.getName(), getArchitecture().getName()+"::"+this.getName(), id);
		addExternalAttribute(a);
		return a;
	}

	public void setAttribute(Attribute attr){
		this.attributes.add(attr);
	}
	
	public Set<Attribute> getAllAttributes() {
		return Collections.unmodifiableSet(attributes);
	}
	
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isAbstract() {
		return isAbstract;
	}
	
	public Set<Method> getAllMethods() {
		return Collections.unmodifiableSet(methods);
	}


	public Attribute findAttributeByName(String name) throws AttributeNotFoundException {
		String message = "atributo '" + name + "' não encontrado na classe '"+ this.getName() +"'.\n";
		
		for(Attribute att : getAllAttributes())
			if (name.equalsIgnoreCase(att.getName()))
				return att;
		LOGGER.info(message);
		throw new AttributeNotFoundException(message);
	}

	
	public boolean moveAttributeToClass(Attribute attribute, Class destinationKlass) {
		if(!destinationKlass.addExternalAttribute(attribute))
			return false;
		
		if(!removeAttribute(attribute)){
			destinationKlass.removeAttribute(attribute);
			return false;
		}
		attribute.setNamespace(getArchitecture().getName() + "::" + destinationKlass.getName());
		LOGGER.info("Moveu atributo: "+  attribute.getName() + " de "+this.getName() +" para " + destinationKlass.getName());
		return true;
	}
	
	/**
	 * Move um método de uma classe para outra.
	 * 
	 * @param method - Método a ser movido
	 * @param destinationKlass - Classe que irá receber o método
	 * 
	 * @return -false se o método a ser movido não existir na classe.<br/> -true se o método for movido com sucesso.
	 */
	public boolean moveMethodToClass(Method method, arquitetura.representation.Class destinationKlass) {
		if(!destinationKlass.addExternalMethod(method))
			return false;
		
		if(!removeMethod(method)){
			destinationKlass.removeMethod(method);
			return false;
		}
		
		method.setNamespace(getArchitecture().getName() + "::" + destinationKlass.getName());
		LOGGER.info("Moveu método: "+  method.getName() + " de "+this.getName() +" para " + destinationKlass.getName());
		return true;
	}
	
	

	public Method createMethod(String name, String type, boolean isAbstract, List<ParameterMethod> parameters) {
		if (!methodExistsOnClass(name, type)){
			String id = UtilResources.getRandonUUID();
			Method method = new Method(getArchitecture(), name, type, this.getName(), isAbstract, id);
			if(parameters != null)
				method.getParameters().addAll(parameters);
			getAllMethods().add(method);
			return method;
		}
		return null; 
	}

	private boolean methodExistsOnClass(String name, String type) {
		for(Method m : getAllMethods()){
			if((name.equalsIgnoreCase(m.getName())) && (type.equalsIgnoreCase(m.getReturnType()))){
				LOGGER.info("Método '"+ name + ":"+type + "' já existe na classe: '"+this.getName()+"'.\n");
				return true;
			}
		}
		
		return false;
	}

	//TODO verificar metodos com mesmo nome e tipo diferente
	public Method findMethodByName(String name) throws MethodNotFoundException {
		String message = "Método '" + name + "' não encontrado na classe '"+ this.getName() +"'.\n";
		
		for(Method m : getAllMethods())
			if((name.equalsIgnoreCase(m.getName())))
				return m;
		
		LOGGER.info(message);
		throw new MethodNotFoundException(message);
	}

	
	public boolean addExternalMethod(Method method) {
		if(methods.add(method)){
			LOGGER.info("Metodo "+method.getName() + " adicionado na classe "+ this.getName());
			return true;
		}else{
			LOGGER.info("TENTOU remover o Método: "+ method.getName() + " da classe: "+ this.getName() + " porém não consegiu");
			return false;
		}
			
	}
	
	public boolean addExternalAttribute(Attribute a) {
		if(this.attributes.add(a)){
			LOGGER.info("Atributo: "+ a.getName() + " adicionado na classe: "+ this.getName());
			return true;
		}else{
			LOGGER.info("TENTOU remover o Atributo: "+ a.getName() + " da classe: "+ this.getName() + " porém não consegiu");
			return false;
		}
	}

	/**
	 * Remove Método da classe
	 * 
	 * @param method - Método a ser removido.
	 * 
	 * @return -true se o método for removido.<br/>-false se método não existir na classe.
	 * 
	 */
	public boolean removeMethod(Method method) {
		if(this.methods.remove(method)){
			LOGGER.info("Método: "+ method.getName() + " removido da classe: "+ this.getName());
			return true;
		}else{
			LOGGER.info("TENTOU remover o método: "+ method.getName() + " classe: "+ this.getName() + " porém não consegiu");
			return false;
		}
	}
	
	/**
	 * 
	 * @param attribute
	 * @return
	 */
	public boolean removeAttribute(Attribute attribute) {
		if(this.attributes.remove(attribute)){
			LOGGER.info("Atributo: "+ attribute.getName() + " removido da classe: "+ this.getName());
			return true;
		}else{
			LOGGER.info("TENTOU remover o atributo: "+ attribute.getName() + " classe: "+ this.getName() + " porém não consegiu");
			return false;
		}
	}

	public List<Method> getAllAbstractMethods() {
		List<Method> abstractMethods = new ArrayList<Method>();
		
		for(Method m : getAllMethods())
			if (m.isAbstract()) abstractMethods.add(m);
		
		return abstractMethods;
	}
	

	public boolean dontHaveAnyRelationship() {
		
		if(getRelationships().isEmpty()){
			return true;
		}else{
			return false;
		}
	}

	public void updateId(String id) {
		super.id = id;
		
	}

	public Object getAllStereotype() {
		return null;
	}


	/**
	 * Retorna o tipo de variant (ex: alternative_OR).<br/>
	 * 
	 * Retorna null se não existir
	 * 
	 * @return String (ex: alternative_OR).
	 */
	public String getVariantType() {
		for(Variant v : VariantFlyweight.getInstance().getVariants()){
			if(v.getName().equalsIgnoreCase(this.getName()))
				return v.getVariantType();
		}
		
		return null;
	}

  //  getAllConcerns: os interesses anotados na classe, nos atributos da classe, nos métodos da classe
  //  e nas interfaces implementadas pela classe. Lembrando que getAllConcerns da interface deve retornar
  //  os interesses anotados na interface mais os interesses das operações da interface.
	
	/**
	 * Retorna todo os interesses da classe, como "todos", entende-se:<br/>
	 * <ul>
	 * 	<li>Interesses anotados na classe</li>
	 * 	<li>Interesses anotados nos atributos e métodos da classe</li>
	 * 	<li>Interesses anotados nas interesses que a classe implementa</li>
	 * </ul>
	 * 
	 * @return {@link Set} Imutável
	 * 
	 */
	@Override
	public Set<Concern> getAllConcerns() {
		Set<Concern> concerns = new HashSet<Concern>(getOwnConcerns());

		for (Method method : getAllMethods())
			concerns.addAll(method.getAllConcerns());
		for (Attribute attribute : getAllAttributes())
			concerns.addAll(attribute.getAllConcerns());
		for(Interface inte  : this.getImplementedInterfaces())
			concerns.addAll(inte.getAllConcerns());
		
		return Collections.unmodifiableSet(concerns);
	}

	public List<AssociationClassRelationship> getAllAssociationClass() {
		List<AssociationClassRelationship> associationsClasses = new ArrayList<AssociationClassRelationship>();
		
		for(Relationship r : getRelationships()){
			if(r instanceof AssociationClassRelationship)
				associationsClasses.add((AssociationClassRelationship) r);
		}
		
		return associationsClasses;
	}

	public Set<Interface> getImplementedInterfaces() {
		return Collections.unmodifiableSet(implementedInterfaces);
	}

	public Set<Interface> getRequiredInterfaces() {
		return Collections.unmodifiableSet(requiredInterfaces);
	}

	public void removeImplementedInterface(Interface inter) {
		this.implementedInterfaces.remove(inter);
	}

	public boolean removeRequiredInterface(Interface supplier) {
		if (!requiredInterfaces.contains(supplier)) return false;
		requiredInterfaces.remove(supplier);
		return true;
	}

	public void addImplementedInterface(Interface supplierElement) {
		this.implementedInterfaces.add(supplierElement);
	}

	public void addRequiredInterface(Interface supplier) {
		this.requiredInterfaces.add(supplier);
	}

	public Class deepCopy() {
		try {
			return this.deepClone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// private static int count = 1;
	public Class deepClone() throws CloneNotSupportedException {
		Cloner cloner = new Cloner();
		Class klass = (Class) cloner.deepClone(this);
		cloner = null;
		return klass;
	}
	
	
	

}