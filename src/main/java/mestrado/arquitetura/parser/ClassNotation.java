package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.NodeNotFound;
import mestrado.arquitetura.exceptions.NullReferenceFoundException;
import mestrado.arquitetura.helpers.UtilResources;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author edipofederle
 *
 */
public class ClassNotation extends XmiHelper {
	
	private static final String SHOW_TYPE_OF_ATTRIBUTE = "7066";
	
	private String xmitype = "notation:Shape";
	private final String fontName = "Lucida Grande";
	private final String fontHeight = "11";
	private final String lineColor = "0";
	private Node notatioChildren;
	private Element notationBasicProperty;
	private static final String LOCATION_TO_ADD_ATTR_IN_NOTATION_FILE = "7017";
	
	private static final String TYPE_CLASS = "2008";
	private static final String TYPE_PACKAGE = "2007";
	
	private DocumentManager documentManager;

	public ClassNotation(DocumentManager documentManager, Node notatioChildren){
		this.documentManager = documentManager;
		this.notatioChildren = notatioChildren;
		this.notatioChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
	}

	public void createNodeForElementType(String idProperty, String type, String typeElement, Element appendTo) {
		Element node = documentManager.getDocNotation().createElement("children");
		
		node.setAttribute("xmi:type", this.xmitype);
		node.setAttribute("xmi:id", UtilResources.getRandonUUID());
		node.setAttribute("type", type);
		node.setAttribute("fontName", this.fontName);
		node.setAttribute("fontHeight", this.fontHeight);
		node.setAttribute("lineColor", this.lineColor);
		
      	
      	Element details = documentManager.getDocNotation().createElement("details");
      	details.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
      	details.setAttribute("xmi:id", UtilResources.getRandonUUID());
      	details.setAttribute("key", "CustomAppearance_MaskValue");
      	details.setAttribute("value", SHOW_TYPE_OF_ATTRIBUTE);
      	
		Element element = documentManager.getDocNotation().createElement("element");
		element.setAttribute("xmi:type", typeElement);
		element.setAttribute("href", documentManager.getModelName()+"#"+ idProperty);
		node.appendChild(element);
		
		Element layoutConstraint = documentManager.getDocNotation().createElement("layoutConstraint");
		layoutConstraint.setAttribute("xmi:type", "notation:Location");
		layoutConstraint.setAttribute("xmi:id", UtilResources.getRandonUUID());
		node.appendChild(layoutConstraint);
		
		if(appendTo != null)
			appendTo.appendChild(node);
		else
			notationBasicProperty.appendChild(node);
	}
	
	public void createXmiForClassInNotationFile(String id, String idPackage) throws NullReferenceFoundException, NodeNotFound {
		
		Element node = documentManager.getDocNotation().createElement("children");
		node.setAttribute("xmi:type", this.xmitype);
		node.setAttribute("xmi:id", UtilResources.getRandonUUID());
		node.setAttribute("type", TYPE_CLASS);
		node.setAttribute("fontName", this.fontName);
		node.setAttribute("fontHeight", this.fontHeight);
		node.setAttribute("lineColor", this.lineColor);
		
		Element notationDecoratioNode = documentManager.getDocNotation().createElement("children");
		notationDecoratioNode.setAttribute("xmi:type", "notation:DecorationNode");
		notationDecoratioNode.setAttribute("xmi:id", UtilResources.getRandonUUID());
		notationDecoratioNode.setAttribute("type", "5029");
		node.appendChild(notationDecoratioNode);
		
	    Element klass = documentManager.getDocNotation().createElement("element");
	    
	    if (id == null){
	    	throw new NullReferenceFoundException("A null reference found when try access attribute id. Executation will be interrupted.");
	    }
	    
	   	klass.setAttribute("href", documentManager.getModelName()+".uml#"+ id);
	    	
	    klass.setAttribute("xmi:type", "uml:Class");
		
	    this.notationBasicProperty = createChildrenComportament(documentManager.getDocNotation(), node, LOCATION_TO_ADD_ATTR_IN_NOTATION_FILE); //onde vai as props
	    node.appendChild(klass);
	    
	    if((idPackage != null) && !(idPackage.isEmpty())){
	    	Node nodeToAppend = findByIDInNotationFile(documentManager.getDocNotation(), idPackage);
	    	nodeToAppend.appendChild(node);
	    }else{
	    	notatioChildren.appendChild(node);
	    }
	    
	}
	

	private Element createChildrenComportament(Document doc, Element node, String type) {
		Element element = doc.createElement("children");
		element.setAttribute("xmi:type", "notation:BasicCompartment");
		element.setAttribute("xmi:id", UtilResources.getRandonUUID());
		element.setAttribute("type", type);
		node.appendChild(element);
		
		Element notationTitleStyle = doc.createElement("styles");
		notationTitleStyle.setAttribute("xmi:type", "notation:TitleStyle");
		notationTitleStyle.setAttribute("xmi:id", UtilResources.getRandonUUID());
		element.appendChild(notationTitleStyle);
		
		Element notationSortingStyle = doc.createElement("styles");
		notationSortingStyle.setAttribute("xmi:type", "notation:SortingStyle");
		notationSortingStyle.setAttribute("xmi:id", UtilResources.getRandonUUID());
		element.appendChild(notationSortingStyle);
		
		Element notationFilteringStyle = doc.createElement("styles");
		notationFilteringStyle.setAttribute("xmi:type", "notation:FilteringStyle");
		notationFilteringStyle.setAttribute("xmi:id", UtilResources.getRandonUUID());
		element.appendChild(notationFilteringStyle);
		
		Element notationBounds = doc.createElement("layoutConstraint");
		notationBounds.setAttribute("xmi:type", "notation:Bounds");
		notationBounds.setAttribute("xmi:id", UtilResources.getRandonUUID());
		element.appendChild(notationBounds);
		
	    Element layoutConstraint = doc.createElement("layoutConstraint");
	    layoutConstraint.setAttribute("x", randomNum());
	    layoutConstraint.setAttribute("xmi:id", UtilResources.getRandonUUID());
	    layoutConstraint.setAttribute("xmi:type", "notation:Bounds");
	    layoutConstraint.setAttribute("y", randomNum());
	    node.appendChild(layoutConstraint);
	    
	    return element;
	}

	public void createXmiForPackageInNotationFile(String id) {
		
		Element nodeChildren = documentManager.getDocNotation().createElement("children");
		nodeChildren.setAttribute("xmi:type", this.xmitype);
		nodeChildren.setAttribute("xmi:id", UtilResources.getRandonUUID());
		nodeChildren.setAttribute("type", TYPE_PACKAGE);
		nodeChildren.setAttribute("fontName", this.fontName);
		nodeChildren.setAttribute("fontHeight", this.fontHeight);
		nodeChildren.setAttribute("lineColor", this.lineColor);
      	
    	
      	Element childrenDecorationnode5026 = createChildrenDecorationnode("5026");
      	Element childrenDecorationnode7016 = createChildrenDecorationnode("7016");
      	
      	Element layoutConstraint = documentManager.getDocNotation().createElement("layoutConstraint");
      	layoutConstraint.setAttribute("xmi:type", "notation:Bounds");
    	layoutConstraint.setAttribute("xmi:id",  UtilResources.getRandonUUID());
      	
      	Element styles = documentManager.getDocNotation().createElement("styles");
      	styles.setAttribute("xmi:type", "notation:TitleStyle");
      	styles.setAttribute("xmi:id", UtilResources.getRandonUUID());
      	childrenDecorationnode7016.appendChild(styles);
      	childrenDecorationnode7016.appendChild(layoutConstraint);
      	
      	nodeChildren.appendChild(childrenDecorationnode5026);
      	nodeChildren.appendChild(childrenDecorationnode7016);
      	
      	
        Element elementPackage = documentManager.getDocNotation().createElement("element");
        elementPackage.setAttribute("xmi:type", "uml:Package");
        elementPackage.setAttribute("href", documentManager.getModelName()+"#"+ id);
        nodeChildren.appendChild(elementPackage);
        
        //TODO mover comum
	    Element layoutConstraint2 = documentManager.getDocNotation().createElement("layoutConstraint");
	    layoutConstraint2.setAttribute("x", randomNum());
	    layoutConstraint2.setAttribute("xmi:id", UtilResources.getRandonUUID());
	    layoutConstraint2.setAttribute("xmi:type", "notation:Bounds");
	    layoutConstraint2.setAttribute("y", randomNum());
	    layoutConstraint2.setAttribute("width", "450"); //TODO ver uma maneira de criar conforme necessidade	
	    layoutConstraint2.setAttribute("height", "450"); //TODO ver uma maneira de criar conforme necessidade	
	    nodeChildren.appendChild(layoutConstraint2);
	    
	    notatioChildren.appendChild(nodeChildren);
		
	}

	private Element createChildrenDecorationnode(String type) {
		Element childrenDecorationnode = documentManager.getDocNotation().createElement("children");
      	childrenDecorationnode.setAttribute("xmi:type", "notation:DecorationNode");
      	childrenDecorationnode.setAttribute("xmi:id", UtilResources.getRandonUUID());
      	childrenDecorationnode.setAttribute("type", type);
		return childrenDecorationnode;
	}
		
}