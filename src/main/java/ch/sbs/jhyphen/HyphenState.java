package ch.sbs.jhyphen;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class HyphenState extends Structure {
	
	public Pointer match;
	
	public Pointer repl;
	
	public byte replindex;
	
	public byte replcut;
	
	public int fallback_state;
	
	public int num_trans;
	
	public HyphenTrans.ByReference trans;
	
	public HyphenState() {
		super();
		initFieldOrder();
	}
	
	protected void initFieldOrder() {
		setFieldOrder(new String[]{"match", "repl", "replindex", "replcut",
				"fallback_state", "num_trans", "trans"});
	}
	
	public HyphenState(Pointer match, Pointer repl, byte replindex, byte replcut,
			int fallback_state, int num_trans, HyphenTrans.ByReference trans) {
		super();
		this.match = match;
		this.repl = repl;
		this.replindex = replindex;
		this.replcut = replcut;
		this.fallback_state = fallback_state;
		this.num_trans = num_trans;
		this.trans = trans;
		initFieldOrder();
	}
	
	public static class ByReference extends HyphenState implements Structure.ByReference {
		
	};
	
	public static class ByValue extends HyphenState implements Structure.ByValue {
		
	};
}
