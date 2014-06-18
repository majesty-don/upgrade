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
	
	private String ipLike;
	
	private String port22Like;
	
	private String port10Like;
	
	private String port15Like;
	
	private String port16Like;
	
	public String getNameLike() throws UnsupportedEncodingException {
		return URLDecoder.decode(nameLike,"UTF-8");
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public String getIpLike() throws UnsupportedEncodingException {
		return URLDecoder.decode(ipLike,"UTF-8");
	}

	public void setIpLike(String ipLike) {
		this.ipLike = ipLike;
	}

	public String getPort22Like() {
		return port22Like;
	}

	public void setPort22Like(String port22Like) {
		this.port22Like = port22Like;
	}

	public String getPort10Like() {
		return port10Like;
	}

	public void setPort10Like(String port10Like) {
		this.port10Like = port10Like;
	}

	public String getPort15Like() {
		return port15Like;
	}

	public void setPort15Like(String port15Like) {
		this.port15Like = port15Like;
	}

	public String getPort16Like() {
		return port16Like;
	}

	public void setPort16Like(String port16Like) {
		this.port16Like = port16Like;
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
			paramList.add("name");
			valueList.add("%" + this.getNameLike().trim() + "%");
			
		}
		
		if(this.getIpLike()!=null && this.getIpLike().trim().length()>0){
			paramList.add("remote");
			valueList.add("%"+this.getIpLike().trim()+"%");
		}
		
		if(this.getPort22Like()!=null && this.getPort22Like().trim().length()>0){
			paramList.add("port22");
			valueList.add(this.getPort22Like().trim());
		}
		
		if(this.getPort10Like()!=null && this.getPort10Like().trim().length()>0){
			paramList.add("port10");
			valueList.add(this.getPort10Like().trim());
		}
		
		if(this.getPort15Like()!=null && this.getPort15Like().trim().length()>0){
			paramList.add("port15");
			valueList.add(this.getPort15Like().trim());
		}
		
		if(this.getPort16Like()!=null && this.getPort16Like().trim().length()>0){
			paramList.add("port16");
			valueList.add(this.getPort16Like().trim());
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
			msg= "nameLike:"+this.getNameLike()+",ipLike:"+this.getIpLike()+",port22Like:"+this.getPort10Like()+",port15Like:"+this.getPort15Like()+",port16Like:"+this.getPort16Like();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}
}
