package upgrade.pojo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import upgrade.common.IQueryObject;

public class CityQuery implements IQueryObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] params;

	private Object[] values;
	
	private String nameLike;
	
	public String getNameLike() throws UnsupportedEncodingException {
		return nameLike!=null ? URLDecoder.decode(nameLike,"UTF-8"):null;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}

	@Override
	public void init() throws UnsupportedEncodingException {
		List<String> paramList = new ArrayList<String>();
		List<Object> valueList = new ArrayList<Object>();
		if(this.getNameLike()!=null && this.getNameLike().trim().length()>0){
			paramList.add("citynamecn");
			valueList.add("%" + this.getNameLike().trim() + "%");
			
		}
		this.params = paramList.toArray(new String[paramList.size()]);
		this.values = valueList.toArray();
		
	}

	@Override
	public String[] getParams() {
		// TODO Auto-generated method stub
		return params;
	}

	@Override
	public Object[] getValues() {
		// TODO Auto-generated method stub
		return values;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String msg="";
		try {
			msg= "nameLike:"+this.getNameLike();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}
}
