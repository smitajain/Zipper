import java.io.File;
import java.util.*;

import org.junit.Test;

import junit.framework.TestCase;


public class ZipperTest extends TestCase {
	
	
//	public void ZipperTestDir(){
//		HuffmanEncoding <String, String> huffie = new HuffmanEncoding<String, String>();
//		Zipper zip = new Zipper();
//		zip.zipper("sample_files/example_dir/" , "outputName" );
//		
//	}

//needed to make getter method for nameandPath, make sure to add to zipper file

	@Test
	public void testTOC(){
		HuffmanEncoding <String, String> huffie = new HuffmanEncoding<String, String>();
		Zipper zip = new Zipper();
		zip.zipper("sample_files/example_dir/","Hello" );
		System.out.println(zip.getTOC());
	}

}
