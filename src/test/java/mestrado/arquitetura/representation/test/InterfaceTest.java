package mestrado.arquitetura.representation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.junit.Before;
import org.junit.Test;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;

public class InterfaceTest extends TestHelper {
	
	private Architecture architecture;

	/**
	 * @see <a href="https://dl.dropbox.com/u/6730822/generico.png">Modelo usado para architecture (Imagem)</a>
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception{
		
		String uriToArchitecture = getUrlToModel("generico");
		architecture = new ArchitectureBuilder().create(uriToArchitecture);
	}
	
	@Test
	public void shouldCreateOperationInterface() throws Exception{
		Interface interfacee = architecture.createInterface("myInterface");
		assertNotNull(interfacee);
		
		assertEquals(0, interfacee.getOperations().size());
		assertNotNull(interfacee.createOperation("myOperation"));
		assertEquals(1, interfacee.getOperations().size());
	}
	
	@Test
	public void shouldRemoveOperationFromInterface() throws Exception{
		Architecture a = givenAArchitecture("interface");
		assertEquals(1, a.getInterfaces().size());
		
		Interface i = a.findInterfaceByName("myInterface");
		assertNotNull(i);
		
		Method o = i.getOperations().iterator().next();
		assertNotNull(o);
		assertEquals(1,i.getOperations().size());
		
		i.removeOperation(o);
		assertEquals(0, i.getOperations().size());
	}
	
	@Test
	public void shouldMoveOperationFromOneInterfaceToOther() throws Exception{
		Architecture a = givenAArchitecture("interface");
		Interface i = a.findInterfaceByName("myInterface");
		Interface i2 = a.createInterface("fooInterface");
		
		Method m = i2.createOperation("myOperation");
		
		assertEquals(1, i2.getOperations().size());
		assertEquals(1, i.getOperations().size());
		
		i2.moveOperationToInterface(m, i);
		assertEquals(2, i.getOperations().size());
		assertEquals(0, i2.getOperations().size());
	}
	
	@Test
	public void shouldGetImplementorsReturnAClass() throws Exception{
		Architecture a = givenAArchitecture("InterfaceImplementorsdi");
		Interface i = a.findInterfaceByName("Interface0");
		
		assertNotNull(i);
		assertEquals(1, i.getImplementors().size());
		assertEquals("Class1", i.getImplementors().iterator().next().getName());
	}
	
	/**
	 * 
	 * @see <a href="https://dl.dropbox.com/u/6730822/dependencyPackageInterface.png"> Modelo usado no teste (imagem)</a>
	 * @throws Exception
	 */
	@Test
	public void shouldGetDepedentsReturnAPackage() throws Exception{
		Architecture a = givenAArchitecture("dependencyPackageInterface");
		Interface i = a.findInterfaceByName("class1");
		assertNotNull(i);
		
		assertEquals(1, i.getDependents().size());
		assertEquals("Package1", i.getDependents().iterator().next().getName());
	}
	
}