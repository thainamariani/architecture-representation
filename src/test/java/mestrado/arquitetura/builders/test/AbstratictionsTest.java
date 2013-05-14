package mestrado.arquitetura.builders.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.relationship.AbstractionRelationship;

import org.junit.Test;

public class AbstratictionsTest  extends TestHelper{
	
	
	@Test
	public void shouldLoadAbstractionInterElement() throws Exception {
		Architecture architecture7 = givenAArchitecture("abstractionInterElement");

		assertNotNull(architecture7);

		AbstractionRelationship abstractionInterElement = architecture7.getAllAbstractions().get(0);

		assertNotNull(abstractionInterElement);
		assertEquals("Client should be myInterfaceSupplier", "myInterfaceClient",	abstractionInterElement.getClient().getName());
		assertEquals("Supplier should be Package1Supplier", "Package1Supplier", abstractionInterElement.getSupplier().getName());
		//assertTrue(abstractionInterElement.getClient().isInterface());
	}
	
	@Test
	public void shouldLoadAbstractionPackagePackage() throws Exception{
		Architecture a = givenAArchitecture("abstractionPackagePackage");
		
		List<AbstractionRelationship> ab = a.getAllAbstractions();
		assertEquals(1, ab.size());
		
		assertEquals("Package1", ab.get(0).getClient().getName());
		assertEquals("Package2", ab.get(0).getSupplier().getName());
	}

}