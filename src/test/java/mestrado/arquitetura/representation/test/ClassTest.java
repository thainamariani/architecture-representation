package mestrado.arquitetura.representation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import main.GenerateArchitecture;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.junit.Before;
import org.junit.Test;

import arquitetura.builders.ClassBuilder;
import arquitetura.exceptions.AttributeNotFoundException;
import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;

public class ClassTest extends TestHelper {
	
	private arquitetura.representation.Class class1, class2;
	
	private Architecture architecture;

	@Before
	public void setUp()	throws Exception {
		
		Package model = modelHelper.getModel("src/test/java/resources/completeClass.uml");
		Class klass = modelHelper.getAllClasses(model).get(0);
		Class klass2 = modelHelper.getAllClasses(model).get(1);
		
		architecture = mock(Architecture.class);
		when(architecture.getName()).thenReturn("MyArch");
		ClassBuilder classBuilder = new ClassBuilder(architecture);
		class1 = classBuilder.create(klass);
		class2 = classBuilder.create(klass2);
		
	}
	
	@Test
	public void shouldHaveAName() throws Exception {
		assertNotNull(class1);
		assertNotNull("Class1",class1.getName());
	}
	
	@Test
	public void shouldUpdateNameOfClass() throws Exception{
		class1.setName("newNameClass");
		assertEquals("newNameClass", class1.getName());
	}
	
	@Test
	public void classShouldBeEqualsWhenNameAndNamespaceAreEquals(){
		class1.setName("Foo");
		class2.setName("Foo");
		class1.setName("foo.bar");
		class2.setName("foo.bar");
		
		assertTrue(class1.equals(class2));
	}
	
	@Test
	public void classShouldNotEqualsWhenNameAndNamespaceAreEquals(){
		class1.setName("Foo");
		class2.setName("Foo");
		class1.setName("foo.bar1");
		class2.setName("foo.bar");
		
		assertFalse(class1.equals(class2));
	}
	
	@Test
	public void shouldHaveAAttribute() throws Exception{
		assertEquals(1, class1.getAllAttributes().size());
	}
	
	@Test
	public void shouldHaveAMethod() throws Exception{
		assertEquals(1, class1.getAllMethods().size());
	}
	
	@Test
	public void shouldRemoveAAttribute() throws Exception{
		assertEquals(1, class1.getAllAttributes().size());
		
		arquitetura.representation.Attribute att = class1.getAllAttributes().iterator().next();
		assertTrue(class1.removeAttribute(att));
		
		assertEquals(0, class1.getAllAttributes().size());
	}
	
	@Test
	public void shouldReturnsFalseWhenTryRemoveAttributeNotExistOnClass(){
		arquitetura.representation.Attribute att = class1.getAllAttributes().iterator().next();
		assertFalse(class2.removeAttribute(att));
	}
	
	@Test
	public void shouldMoveAttribute() throws AttributeNotFoundException{
		assertEquals(3, class2.getAllAttributes().size());
		assertEquals(1, class1.getAllAttributes().size());
		
		arquitetura.representation.Attribute property1 = class1.getAllAttributes().iterator().next();
		assertTrue(class1.moveAttributeToClass(property1, class2));
		
		assertEquals(4, class2.getAllAttributes().size());
		assertEquals(0, class1.getAllAttributes().size());
		
		assertEquals("MyArch::Class2",class2.findAttributeByName("Property1").getNamespace());
	}
	
	@Test
	public void moveattR() throws Exception{
		Architecture a = givenAArchitecture("moveAttr");
		
		Attribute attr = a.findClassByName("Class1").get(0).getAllAttributes().iterator().next();
		a.findClassByName("Class1").get(0).moveAttributeToClass(attr, a.findClassByName("Class2").get(0));
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "movendoAttrEntreClasses");
		
		Architecture ag = givenAArchitecture2("movendoAttrEntreClasses");
		assertEquals(2,ag.findClassByName("Class2").get(0).getAllAttributes().size());
		assertEquals(0,ag.findClassByName("Class1").get(0).getAllAttributes().size());
		
		
	}
	
	@Test
	public void shouldReturnsFalseWhenTryMoveAttributeNotExistOnClass(){
		arquitetura.representation.Attribute att = class1.getAllAttributes().iterator().next();
		assertFalse(class2.moveAttributeToClass(att, class1));
	}
	
	
	@Test
	public void shouldRemoveAMethod(){
		assertEquals(1, class1.getAllMethods().size());
		Method method = class1.getAllMethods().iterator().next();
		
		assertTrue(class1.removeMethod(method));
		assertEquals(0, class1.getAllMethods().size());
	}
	
	@Test
	public void shouldMoveAMethod() throws ClassNotFound{
		assertEquals(1, class1.getAllMethods().size());
		assertEquals(0, class2.getAllMethods().size());
		
		Method method = class1.getAllMethods().iterator().next();
		assertTrue(class1.moveMethodToClass(method, class2));
		
		assertEquals(0, class1.getAllMethods().size());
		assertEquals(1, class2.getAllMethods().size());
	}
	
	@Test
	public void testMovendoMetodo() throws Exception{
		Architecture a = givenAArchitecture("movendoMethod");
		arquitetura.representation.Class k1 = a.findClassByName("Class1").get(0);
		arquitetura.representation.Class k2 = a.findClassByName("Class2").get(0);
		
		Method m = k2.getAllMethods().iterator().next();
		k2.moveMethodToClass(m, k1);
		
		assertEquals(2,a.findClassByName("Class1").get(0).getAllMethods().size());
		
		GenerateArchitecture g = new GenerateArchitecture();
		g.generate(a, "metodoMovidoComSucesso");
		
	}
	
	@Test
	public void shouldReturnFalseWhenTryRemoveMothodNotExists(){
		Method method = class1.getAllMethods().iterator().next();
		
		assertFalse(class2.removeMethod(method));
		assertEquals(1, class1.getAllMethods().size());
	}
	
	@Test
	public void shouldReturnFalseWhenTryMoveMothodNotExists(){
		Method method = class1.getAllMethods().iterator().next();
		assertFalse(class2.moveMethodToClass(method, class1));
	}
	
	@Test(expected=ConcernNotFoundException.class)
	public void shouldNotAddConcernWhenNotExists() throws Exception{
		Architecture a = givenAArchitecture("concerns/completeClass");
		arquitetura.representation.Class klass1 = a.findClassByName("Class1").get(0);

		assertEquals(1, klass1.getOwnConcerns().size());
		klass1.addConcern("foo666");
		assertEquals(1, klass1.getOwnConcerns().size());
	}
	
	@Test
	public void shouldAddConcernToClass() throws Exception{
		Architecture a = givenAArchitecture("concerns/completeClass");
		arquitetura.representation.Class klass1 = a.findClassByName("Class1").get(0);
		
		assertEquals(1, klass1.getOwnConcerns().size());
		klass1.addConcern("play");
		assertEquals(2, klass1.getOwnConcerns().size());
	}
	
	@Test
	public void shouldRemoveConcernFromClass() throws Exception{
		Architecture a = givenAArchitecture("concerns/completeClass");
		arquitetura.representation.Class klass1 = a.findClassByName("Class1").get(0);
		
		assertEquals(1, klass1.getOwnConcerns().size());
		klass1.removeConcern("action");
		assertEquals(0, klass1.getOwnConcerns().size());
	}
	
	@Test
	public void shouldNotRemoveConcernFromClassWhenNotExists() throws Exception{
		Architecture a = givenAArchitecture("concerns/completeClass");
		
		arquitetura.representation.Class klass1 = a.findClassByName("Class1").get(0);
		assertEquals(1, klass1.getOwnConcerns().size());
		
		klass1.removeConcern("xpto");
		assertEquals(1, klass1.getOwnConcerns().size());
	}
	
	@Test
	public void testGetImplementedInterfaces() throws Exception {
		Architecture a = givenAArchitecture("classRealizationInterface");
		arquitetura.representation.Class klass1 = a.findClassByName("Class2").get(0);
		assertEquals("Deve retornar 2 interfaces", 2, klass1.getImplementedInterfaces().size());
	}
	
	@Test
	public void testGetRequiredInterfaces() throws Exception{
		Architecture a = givenAArchitecture("classInterface/classrealizationInterface");
		arquitetura.representation.Class klass1 = a.findClassByName("Class1").get(0);
		
		Set<Interface> requiredInterface = klass1.getRequiredInterfaces();
		
		assertNotNull(requiredInterface);
		assertEquals(1,requiredInterface.size());
		assertEquals("Class2", requiredInterface.iterator().next().getName());
	}
	
	@Test
	public void shouldReturnAllConcerns() throws Exception{
		Architecture a = givenAArchitecture("ClassAllConcern");
		arquitetura.representation.Class foo = a.findClassByName("Foo").get(0);
		assertEquals("Deve retornar 8 concerns", 8, foo.getAllConcerns().size());
	}
	
	@Test
	public void shouldReturnOwnedConcerns() throws Exception{
		Architecture a = givenAArchitecture("ClassAllConcern");
		arquitetura.representation.Class foo = a.findClassByName("Foo").get(0);
		
		assertEquals(1, foo.getOwnConcerns().size());
		
		
	}
	
}