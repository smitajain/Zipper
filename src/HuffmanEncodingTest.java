import static org.junit.Assert.*;

import java.util.Hashtable;

import org.junit.Test;


public class HuffmanEncodingTest {
	
	@Test
	public void testTreeConstructor(){ 
		HuffmanEncoding txt = new HuffmanEncoding();
		txt.findFrequencyTable("sample_files/SmallFile.txt");
		Hashtable<String,Integer> Freq = txt.getFreqTable();
		assertEquals(Freq.size(), 3);
		
	}
	
	@Test 
	public void testFindFrequencyTable(){ 
		HuffmanEncoding txt = new HuffmanEncoding();
		txt.findFrequencyTable("sample_files/SmallFile.txt");
		Hashtable h = txt.getCodeTable();
		assertEquals(h.get("EOF"), 1);
		assertEquals(h.get("01100001"), 2);
		assertEquals(h.get("01100010"), 1);
		
	}
	
	@Test
	public void testBuildByteCodeTable(){
		HuffmanEncoding txt = new HuffmanEncoding();
		txt.findFrequencyTable("sample_files/SmallFile.txt");
		Hashtable h = txt.getCodeTable();
		assertEquals(h.size(), 3);
		
	}
	
	@Test
	public void testEncodeSetUp(){
		HuffmanEncoding txt = new HuffmanEncoding();
		txt.encodeSetUp("sample_files/SmallFile.txt");
		Hashtable table = txt.getByteCodeTable();
		assertTrue(table.containsKey("01100010"));
		assertTrue(table.containsKey("EOF"));
		assertTrue(table.containsKey("01100001"));
	}
	
	@Test
	public void testEncodeHelper(){
		HuffmanEncoding txt = new HuffmanEncoding();
		txt.encodeSetUp("sample_files/SmallFile.txt");
		Hashtable table = txt.getByteCodeTable();
		StringBuilder outputStr = txt.encodeHelper("sample_files/SmallFile.txt");
		assertEquals(outputStr.length() % 8, 0);
		assertEquals((outputStr.substring(outputStr.length()-8)),"00001010");
	}
	
	@Test
	public void testEncode(){
		HuffmanEncoding txt = new HuffmanEncoding();
		txt.encode("sample_files/SmallFile.txt", "outputSmallFile1.txt");
		txt.decode("outputSmallFile1.txt", "SmallFilecopy.txt");
		
		FileCharIterator myIter = new FileCharIterator("sample_files/SmallFile.txt");
		FileCharIterator myIter2 = new FileCharIterator("SmallFilecopy.txt");
		while(myIter.hasNext() && myIter2.hasNext()){
			assertTrue((myIter.next()).equals( myIter2.next()));
		}
		
		
		HuffmanEncoding txt2 = new HuffmanEncoding();
		txt2.encode("sample_files/Kaleidoscope.txt", "outputKaleidoscope1.txt");
		txt2.decode("outputKaleidoscope1.txt", "Kaleidoscopecopy.txt");
		FileCharIterator myIter3 = new FileCharIterator("sample_files/Kaleidoscope.txt");
		FileCharIterator myIter4 = new FileCharIterator("Kaleidoscopecopy.txt");
		while(myIter3.hasNext() && myIter4.hasNext()){
			assertTrue((myIter3.next()).equals( myIter4.next()));
		}
	}	
		
		@Test
		public void testEncodeSetUp2(){
			HuffmanEncoding txt = new HuffmanEncoding();
			txt.encodeSetUp2("sample_files/SmallFile.txt", 1);
			Hashtable table = txt.getByteCodeTable();
			assertTrue(table.containsKey("011000010110001001100001"));
			assertTrue(table.containsKey("EOF"));
		}
		
		@Test
		public void testEncodeHelper2(){
			HuffmanEncoding txt = new HuffmanEncoding();
			txt.encodeSetUp2("sample_files/SmallFile.txt",1);
			Hashtable table = txt.getByteCodeTable();
			StringBuilder outputStr = txt.encodeHelper2("sample_files/SmallFile.txt",1);
			assertEquals(outputStr.length() % 8, 0);
			assertEquals((outputStr.substring(outputStr.length()-8)),"00001010");
		}
		
		@Test
		public void testEncode2(){
			HuffmanEncoding txt2 = new HuffmanEncoding();
			txt2.encode2("sample_files/Kaleidoscope.txt", "outputK.txt", 6);
			txt2.decode("outputK.txt", "KaleidoscopeOutput.txt");
			FileCharIterator myIter3 = new FileCharIterator("sample_files/Kaleidoscope.txt");
			FileCharIterator myIter4 = new FileCharIterator("KaleidoscopeOutput.txt");
			while(myIter3.hasNext() && myIter4.hasNext()){
				assertTrue((myIter3.next()).equals( myIter4.next()));
			}
		}
		@Test
		public void testDecode(){
			HuffmanEncoding txt = new HuffmanEncoding();
			txt.encode("sample_files/Kaleidoscope.txt", "outputKaleidosCope1.txt");
			txt.decode("outputKaleidosCope1.txt", "KaleidoscopeCopy.txt");
			FileCharIterator myIter = new FileCharIterator("sample_files/Kaleidoscope.txt");
			FileCharIterator myIter2 = new FileCharIterator("KaleidoscopeCopy.txt");
			while(myIter.hasNext() && myIter2.hasNext()){
				assertTrue((myIter.next()).equals( myIter2.next()));
			}
			
			HuffmanEncoding txt2 = new HuffmanEncoding();
			txt2.encode2("sample_files/Kaleidoscope.txt", "outputKaleidoscope1.txt", 4);
			txt2.decode("outputKaleidoscope1.txt", "Kaleidoscopecopy.txt");
			FileCharIterator myIter3 = new FileCharIterator("sample_files/Kaleidoscope.txt");
			FileCharIterator myIter4 = new FileCharIterator("Kaleidoscopecopy.txt");
			while(myIter3.hasNext() && myIter4.hasNext()){
				assertTrue((myIter3.next()).equals( myIter4.next()));
			
			}
		
		}
}