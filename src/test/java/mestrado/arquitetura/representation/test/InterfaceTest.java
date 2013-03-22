package mestrado.arquitetura.representation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mestrado.arquitetura.builders.ArchitectureBuilder;
import mestrado.arquitetura.helpers.test.TestHelper;
import mestrado.arquitetura.representation.Architecture;
import mestrado.arquitetura.representation.Interface;
import mestrado.arquitetura.representation.Method;

import org.junit.Before;
import org.junit.Test;

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
		assertEquals(1, a.getAllInterfaces().size());
		
		Interface i = a.getAllInterfaces().get(0);
		assertNotNull(i);
		
		Method o = i.getOperations().get(0);
		assertNotNull(o);
		assertEquals(1,i.getOperations().size());
		
		i.removeOperation(o);
		assertEquals(0, i.getOperations().size());
	}
	
	@Test
	public void shouldMoveOperationFromOneInterfaceToOther() throws Exception{
		Architecture a = givenAArchitecture("interface");
		Interface i = a.getAllInterfaces().get(0);
		Interface i2 = a.createInterface("fooInterface");
		
		Method m = i2.createOperation("myOperation");
		
		assertEquals(1, i2.getOperations().size());
		assertEquals(1, i.getOperations().size());
		
		i2.moveOperationToInterface(m, i);
		assertEquals(2, i.getOperations().size());
		assertEquals(0, i2.getOperations().size());
	}
	
	@Test
	public void shouldGetImplementors() throws Exception{
		Architecture a = givenAArchitecture("abstractionInterElement");
		Interface i = a.findInterfaceByName("myInterfaceClient");
		
		assertNotNull(i);
		assertEquals(1, i.getImplementors().size());
	}
	
	/**
	 * 
	 * @see <a href="https://dl.dropbox.com/u/6730822/dependencyPackageInterface.png"> Modelo usado no teste (imagem)</a>
	 * @throws Exception
	 */
	@Test
	public void shouldshouldGetImplementors() throws Exception{
		Architecture a = givenAArchitecture("dependencyPackageInterface");
		Interface i = a.findInterfaceByName("class1");
		assertNotNull(i);
		
		assertEquals(1,i.getDependents().size());
	}

}