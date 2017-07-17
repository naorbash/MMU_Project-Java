package com.hit.memoryunits;
import org.junit.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;

public class MMUTest 
{
	@Test
	public void testPageImpl()
	{
		
	}
	
	@Test
	public void testRAMImpl()
	{
		
	}
	
	@Test
	public void testHardDiskImpl()
	{
		

	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void testMemoryManagmentUnitImpl() throws IOException
	{
	
		HardDisk HD = HardDisk.getInstance();
		Map<java.lang.Long,Page<byte[]>> HDpages = HD.getMAP();
		Long pageIds[] = new Long[6];
		Long pageIds2[] = new Long[3];
		
		@SuppressWarnings("unchecked")
		Page<byte[]> expected[] = (Page<byte[]>[]) new Page[6];
		@SuppressWarnings("unchecked")
		Page<byte[]> expected2[] = (Page<byte[]>[]) new Page[3];

		IAlgoCache<java.lang.Long,java.lang.Long> cache = new LRUAlgoCacheImpl<>(5);
		MemoryManagementUnit MMU = new MemoryManagementUnit(5,cache);
		int i=0;
		for(Entry<Long,Page<byte[]>> entry : HDpages.entrySet())
		{
			if(i<6)
			{
				Long key = entry.getKey();
				pageIds[i]= key;
				expected[i] = HD.pageFault(key);
				i++;
			}
			else
			{
				Long key = entry.getKey();
				pageIds2[i-6]= key;
				expected2[i-6] = HD.pageFault(key);
				i++;
			}
			if(i==9)
				break;
		}
		Assert.assertEquals(expected,MMU.getPages(pageIds));
		
		//Assert.assertEquals(expected2,MMU.getPages(pageIds2));
		
		//Assert.assertEquals(expected3,MMU.getPages(pageIds));
		/*for(int j=0;j<5;j++)
			System.out.println(expected[j]);
		for(int j=0;j<3;j++)
			System.out.println(expected2[j]);*/
		
		
		
		
	}
}
