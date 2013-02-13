package mestrado.arquitetura.helpers.test;

import java.io.File;
import java.util.List;

import mestrado.arquitetura.helpers.ModelHelper;
import mestrado.arquitetura.helpers.ModelHelperFactory;
import mestrado.arquitetura.helpers.ModelIncompleteException;
import mestrado.arquitetura.helpers.ModelNotFoundException;
import mestrado.arquitetura.helpers.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.Uml2Helper;
import mestrado.arquitetura.helpers.Uml2HelperFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;

public abstract class TestHelper {

	protected static Uml2Helper uml2Helper;
	protected static ModelHelper modelHelper;

	static{
		try {
			uml2Helper = Uml2HelperFactory.getUml2Helper();
		} catch (ModelNotFoundException e) {
			// TODO AJUSTAR
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			// TODO AJUSTAR
			e.printStackTrace();
		}
		try {
			modelHelper = ModelHelperFactory.getModelHelper();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (ModelIncompleteException e) {
			e.printStackTrace();
		}
		
	}

	public static Package givenAModel(String modelName) throws ModelNotFoundException , ModelIncompleteException , SMartyProfileNotAppliedToModelExcepetion {
		//TODO Move from Here, problemas dos estereotipos
		String uri = getUrlToModel(modelName);
		String absolutePath = new File(uri).getAbsolutePath();
		Package epo2Model = uml2Helper.load(absolutePath);
		return epo2Model;
	}

	public static Classifier givenAClass() throws ModelNotFoundException  , ModelIncompleteException , SMartyProfileNotAppliedToModelExcepetion{
		Package content = givenAModel("ExtendedPO2");
		List<Classifier> elementsClass = modelHelper.getAllClasses(content);
		return elementsClass.get(0);
	}

	protected static String getUrlToModel(String modelName) {
		return "src/test/java/resources/" + modelName + ".uml";
	}

	protected static String getUriToResource(String resourcesNamee) {
		return URI.createFileURI(getUrlToModel(resourcesNamee)).toString();
	}
	
	public static Stereotype getStereotypeByName(String name) throws ModelNotFoundException , ModelIncompleteException , SMartyProfileNotAppliedToModelExcepetion{
		Package perfil = givenAModel("smartyProfile");
		return  perfil.getOwnedStereotype(name);
		
	}

}