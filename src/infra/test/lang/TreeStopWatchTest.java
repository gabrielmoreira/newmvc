package lang;

import static org.junit.Assert.*;

import org.junit.Test;

public class TreeStopWatchTest {
	@Test
	public void deveRetornarString() throws Exception {
		TreeStopWatch treeStopWatch = new TreeStopWatch();
		treeStopWatch.startEvent("a");
		treeStopWatch.startEvent("a.1");
		Thread.sleep(100);
		treeStopWatch.startEvent("a.1.1");
		Thread.sleep(10);
		treeStopWatch.stopEvent();
		treeStopWatch.startEvent("a.1.2").stopEvent();
		treeStopWatch.stopEvent();
		treeStopWatch.startEvent("a.2");
		treeStopWatch.stopEvent().stopEvent();
		String resultado = treeStopWatch.prettyPrint().replaceAll("[0-9]+ ms", "IGNORED ms");
		//@formatter:off
		assertEquals("a : IGNORED ms\n" + 
				"	a.1 : IGNORED ms\n" + 
				"		a.1.1 : IGNORED ms\n" + 
				"		a.1.2 : IGNORED ms\n" + 
				"	a.2 : IGNORED ms\n", resultado);
		//@formatter:on
	}
	
	@Test
	public void deveRetornarStringComWarning() throws Exception {
		TreeStopWatch treeStopWatch = new TreeStopWatch();
		treeStopWatch.startEvent("a");
		treeStopWatch.startEvent("a.1");
		Thread.sleep(100);
		treeStopWatch.startEvent("a.1.1");
		Thread.sleep(10);
		treeStopWatch.stopEvent();
		treeStopWatch.startEvent("a.1.2").stopEvent();
		treeStopWatch.stopEvent();
		treeStopWatch.startEvent("a.2");
		treeStopWatch.stopEvent();
		String resultado = treeStopWatch.prettyPrint().replaceAll("[0-9]+ ms", "IGNORED ms");
		//@formatter:off
		assertEquals("WARNING: same events have not been marked as closed, the summary might contain wrong data...\n" +
				"a : IGNORED ms\n" + 
				"	a.1 : IGNORED ms\n" + 
				"		a.1.1 : IGNORED ms\n" + 
				"		a.1.2 : IGNORED ms\n" + 
				"	a.2 : IGNORED ms\n", resultado);
		//@formatter:on
	}
}
