package com.hit.memoryunits;

@SuppressWarnings("serial")
public class Page<T> implements java.io.Serializable
{
	private T content;
	private java.lang.Long pageId;
	
	Page(java.lang.Long id,T content)  //getting the page ID and content
	{
		this.pageId = id;
		this.content = content;
	}
	
	public T getContent()
	{return content;}
	
	public java.lang.Long getPageId()
	{return pageId;}
	
	public void setContent(T content)
	{this.content = content;}
	
	public void setPageId(java.lang.Long pageId)
	{this.pageId = pageId;}
	
	@Override
	public java.lang.String toString()  //printing the page Id number
	{
		System.out.println(this.pageId);
		return null;
	}
	
	@Override
	public int hashCode()
	{
		long id = (long)pageId;  //casting in order to return integer
		return (int)id;
	}
	
	@Override
	public boolean equals(java.lang.Object obj)  //if the pageId of the pages are the same,return true
	{
		if (this.hashCode()==obj.hashCode())
			return true;
		return false;
	}
}
