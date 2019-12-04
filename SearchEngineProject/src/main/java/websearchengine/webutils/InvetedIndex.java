package websearchengine.webutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import websearchengine.models.IndexNode;
import websearchengine.models.SortNode;
import websearchengine.models.TSTNode;

public class InvetedIndex {

	private static final Logger LOGGER = LogManager.getLogger(InvetedIndex.class);

	private IndexNode[][] indexT = new IndexNode[SearchEngineConstants.FILEMAX][SearchEngineConstants.WORDMAX];

	public void addToTable(TSTNode tNode, int wordIdx) {
		if (tNode.fileIdx >= SearchEngineConstants.FILEMAX || wordIdx >= SearchEngineConstants.WORDMAX) {
			LOGGER.warn("Invalid file index " + tNode.fileIdx + " or word index " + wordIdx);
			return;
		}
		IndexNode idxNode = indexT[tNode.fileIdx][wordIdx];
		// If the node doesn't exist, create it
		if (idxNode == null) {
			idxNode = new IndexNode();
			indexT[tNode.fileIdx][wordIdx] = idxNode;
			idxNode.setLine(tNode.line);
		}
		idxNode.cnt++;
	}

	public SortNode[] getCntArray(int wordIdx) {
		SortNode[] cntArray = new SortNode[SearchEngineConstants.FILEMAX];
		IndexNode idxNode;

		for (int i = 0; i < cntArray.length; i++) {
			cntArray[i] = new SortNode();

			idxNode = indexT[i][wordIdx];
			if (idxNode != null) {
				cntArray[i].cnt = idxNode.getCnt();
				cntArray[i].fileIdx = i;
				cntArray[i].line = idxNode.getLine();
			} else {
				cntArray[i].cnt = 0;
				cntArray[i].fileIdx = i;
				cntArray[i].line = 0;
			}
		}
		return cntArray;
	}

}
