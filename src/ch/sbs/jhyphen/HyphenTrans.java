package ch.sbs.jhyphen;

import com.sun.jna.Structure;

public class HyphenTrans extends Structure {
	
	public byte ch;
	
	public int new_state;
	
	public HyphenTrans() {
		super();
		initFieldOrder();
	}
	
	protected void initFieldOrder() {
		setFieldOrder(new String[]{"ch", "new_state"});
	}
	
	public HyphenTrans(byte ch, int new_state) {
		super();
		this.ch = ch;
		this.new_state = new_state;
		initFieldOrder();
	}
	
	public static class ByReference extends HyphenTrans implements Structure.ByReference {
		
	};
	
	public static class ByValue extends HyphenTrans implements Structure.ByValue {
		
	};
}
