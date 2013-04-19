package mestrado.arquitetura.genericsTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.StereotypeHelper;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.relationship.AssociationEnd;
import mestrado.arquitetura.representation.relationship.AssociationRelationship;
import mestrado.arquitetura.representation.relationship.DependencyRelationship;
import mestrado.arquitetura.representation.relationship.GeneralizationRelationship;
import mestrado.arquitetura.representation.relationship.UsageRelationship;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.junit.Test;

/**
 * 
 * @author edipofederle
 *
 */
public class GenericTest extends TestHelper {
	
	
	@Test
	public void shouldApplyStereotypePersistense() throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion{
		Package model = givenAModel("interface");
		
		NamedElement klass = modelHelper.getAllClasses(model).get(0);
		Profile profileConcern = (Profile) givenAModel("perfilConcerns.profile");
		model.applyProfile(profileConcern);
		assertNotNull(klass);
		assertEquals("myInterface", klass.getName());
		
		assertFalse("Nao deve possuir concern", StereotypeHelper.hasConcern(klass));
		
		Stereotype concern = profileConcern.getOwnedStereotype("Persistence");
		assertEquals("Persistence", concern.getName());
		assertNotNull(concern);
		
		klass.applyStereotype(concern);
		
		assertTrue("Deve possuir concern", StereotypeHelper.hasConcern(klass));
	}
	
	
	@Test
	public void shouldLoadDependencyInterClassWitoutPackageAndClassWithPackage() throws Exception{
		String uriToArchitecture = getUrlToModel("classPacote");
		Architecture architecture = new ArchitectureBuilder().create(uriToArchitecture);
		
		assertNotNull(architecture);
		assertEquals(2, architecture.getAllClasses().size());
		assertEquals(1, architecture.getInterClassRelationships().size());
		
		DependencyRelationship r = architecture.getAllDependencies().get(0);
		
		assertNotNull(r);
		assertEquals("Class1", r.getClient().getName());
		assertEquals("Class2", r.getSupplier().getName());
		
		assertEquals("model", r.getSupplier().getNamespace());
		assertEquals("model::Package1", r.getClient().getNamespace());
	}
	
	@Test
	public void testAll() throws Exception{
		Architecture a = givenAArchitecture("all");
		assertNotNull(a);
		
		assertEquals(3, a.getAllRelationships().size());
		assertEquals(1, a.getAllGeneralizations().size());
		assertEquals(1, a.getAllAssociations().size());
		assertEquals(1, a.getAllUsage().size());
		assertEquals(2, a.getAllPackages().size());
		assertEquals(4, a.getAllClasses().size());
		
		GeneralizationRelationship g = a.getAllGeneralizations().get(0);
		assertEquals("Class2",g.getChild().getName());
		assertEquals("Class1", g.getParent().getName());
		assertNotNull(g.getId());
		
		UsageRelationship usage = a.getAllUsage().get(0);
		assertNotNull(usage.getId());
		assertEquals("Class2", usage.getClient().getName());
		assertEquals("Class3", usage.getSupplier().getName());
		
		AssociationRelationship association = a.getAllAssociations().get(0);
		assertEquals(2, association.getParticipants().size());
		
		AssociationEnd p1 = association.getParticipants().get(0);
		AssociationEnd p2 = association.getParticipants().get(1);
		assertNotNull(p1);
		assertNotNull(p2);
		assertEquals("Class3", p1.getCLSClass().getName());
		assertEquals("Class1", p2.getCLSClass().getName());
		
		mestrado.arquitetura.representation.Package server = a.findPackageByName("Server");
		assertNotNull(server);
		assertEquals(3, server.getClasses().size());
		assertContains(server.getClasses(), "Class1", "Class2", "Class3");
		
		mestrado.arquitetura.representation.Package client = a.findPackageByName("Client");
		assertNotNull(client);
		assertEquals(1, client.getClasses().size());
		assertContains(client.getClasses(), "Class1");
		
		assertEquals(9, a.getNumberOfElements());
	}
	
	@Test
	public void testeGenerico() throws Exception{
		Architecture a = givenAArchitecture("simples");
		assertNotNull(a);
		assertEquals(1 ,a.getAllClasses().size());
		assertEquals(1, a.getNumberOfElements());
	}
	
}