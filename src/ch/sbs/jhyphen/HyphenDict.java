package ch.sbs.jhyphen;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class HyphenDict extends Structure {
	
	public byte lhmin;
	
	public byte rhmin;
	
	public byte clhmin;
	
	public byte crhmin;
	
	public Pointer nohyphen;
	
	public int nohyphenl;
	
	public int num_states;
	
	public byte[] cset = new byte[(20)];
	
	public int utf8;
	
	public HyphenState.ByReference states;
	
	public HyphenDict.ByReference nextlevel;
	
	public HyphenDict() {
		super();
		initFieldOrder();
	}
	
	protected void initFieldOrder() {
		setFieldOrder(new String[]{"lhmin", "rhmin", "clhmin", "crhmin", "nohyphen", 
				"nohyphenl", "num_states", "cset", "utf8", "states", "nextlevel"});
	}
	
	public static class ByReference extends HyphenDict implements Structure.ByReference {
		
	};
	
	public static class ByValue extends HyphenDict implements Structure.ByValue {
		
	};
}
