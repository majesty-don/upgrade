package upgrade.common;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public interface IQueryObject extends Serializable {
	public void init() throws UnsupportedEncodingException;

	public String[] getParams();

	public Object[] getValues();
}
