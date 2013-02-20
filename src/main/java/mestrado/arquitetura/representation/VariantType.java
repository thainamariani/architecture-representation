package mestrado.arquitetura.representation;


public enum VariantType {
	
	NONE(""),
	MANDATORY("mandatory"),
	OPTIONAL("optional"),
	ALTERNATIVE_OR("alternative_OR"),
	ALTERNATIVE_XOR("alternative_XOR");
	
	private final String variantName;
	VariantType(String variantName){
		this.variantName = variantName;
	}
	
	@Override
	public String toString(){
		return variantName;
	}
	
	public static VariantType getByName(String name) {        
        for (VariantType e : VariantType.values()) {
            if (e.toString().equalsIgnoreCase(name))
                return e;
        }
        return NONE; 
    }
}