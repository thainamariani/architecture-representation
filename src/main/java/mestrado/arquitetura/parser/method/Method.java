package mestrado.arquitetura.parser.method;

import java.util.ArrayList;
import java.util.List;

import mestrado.arquitetura.helpers.UtilResources;


public class Method {
	
	private String id;
	private Method method;
	private VisibilityKind visibility;
	private String name;
	private List<Argument> arguments = new ArrayList<Argument>();
	private Types returnMethod;
	private boolean isAbstract = false;

	private Method(){}
	
	public Method withArguments(List<Argument> arguments) {
		this.arguments.addAll(arguments);
		return this;
	}


	public Method withVisibility(VisibilityKind visibility) {
		this.visibility = visibility;
		return this;
	}


	public Method withReturn(Types typeReturn) {
		this.returnMethod = typeReturn;
		return this;
	}
	

	/**
	 * @return the isAbstract
	 */
	public String isAbstract() {
		return isAbstract ? "true" : "false";
	}

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}


	/**
	 * @return the visibility
	 */
	public String getVisibility() {
		return visibility.getName();
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the arguments
	 */
	public List<Argument> getArguments() {
		return arguments;
	}


	/**
	 * @return the returnMethod
	 */
	public String getReturnMethod() {
		return returnMethod.getName();
	}


	public Method build() {
		return this;
	}


	public static Method create() {
		Method method = new Method();
		method.setId(UtilResources.getRandonUUID());
		return method;
	}

	private void setId(String randonUUID) {
		this.id = randonUUID;
	}

	public Method withName(String name) {
		this.name = name;
		return this;
	}

	public Method abstractMethod() {
		this.isAbstract = true;
		return this;
	}

	public String getId() {
		return this.id;
	}
	
}