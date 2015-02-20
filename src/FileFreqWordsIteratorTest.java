import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;

import org.junit.Test;


public class FileFreqWordsIteratorTest {

	@Test
	public void testConstructor() {
		FileFreqWordsIterator test = new FileFreqWordsIterator("sample_files/Kaleidoscope.txt", 5);
		ArrayList list = test.getAryList();
		assertTrue(list.size() != 0);
	}
	
	@Test
	public void testHasNext(){
		FileFreqWordsIterator test = new FileFreqWordsIterator("sample_files/Kaleidoscope.txt", 5);
		assertTrue(test.hasNext());
		ArrayList list = test.getAryList();
		while(list.size() != 0){
			test.next();
		}
		assertFalse(test.hasNext());
	}
	
	@Test
	public void testNext(){
		FileFreqWordsIterator test = new FileFreqWordsIterator("sample_files/Kaleidoscope.txt", 0);
		FileCharIterator test2 = new FileCharIterator("sample_files/Kaleidoscope.txt");
		assertTrue(test.hasNext());
		while(test.hasNext() && test2.hasNext()){
			assertEquals(test.next(), test2.next());
		}
		assertFalse(test.hasNext());
	
		FileFreqWordsIterator test3 = new FileFreqWordsIterator("sample_files/Kaleidoscope.txt", 5);
		assertTrue(test3.hasNext());
		while(test3.hasNext()){
			test3.next();
		}
		assertFalse(test3.hasNext());
		
	}
	
	@Test
	public void testRemove(){
		
		try{
			FileFreqWordsIterator test = new FileFreqWordsIterator("sample_files/Kaleidoscope.txt", 6);
			assertTrue(test.hasNext());
			test.next();
			test.remove();
		}
		catch(UnsupportedOperationException e){
			System.err.print("Remove is not implemented in this iterator!");
		}
	}

}
